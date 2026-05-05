package com.bulain.mybatis.sys.service.impl;

import com.bulain.mybatis.sys.common.WechatLoginConstants;
import com.bulain.mybatis.sys.config.WechatOpenPlatformConfig;
import com.bulain.mybatis.sys.dto.BindWechatDTO;
import com.bulain.mybatis.sys.dto.UnbindWechatDTO;
import com.bulain.mybatis.sys.dto.WechatLoginDTO;
import com.bulain.mybatis.sys.dto.WechatQrCodeResponse;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.service.SysJwtService;
import com.bulain.mybatis.sys.service.SysUserService;
import com.bulain.mybatis.sys.service.WechatLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 微信登录服务实现类
 */
@Service
public class WechatLoginServiceImpl implements WechatLoginService {

    @Autowired
    private WechatOpenPlatformConfig wechatConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysJwtService sysJwtService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public WechatQrCodeResponse getQrCode() {
        // 生成随机 state
        String state = UUID.randomUUID().toString().replace("-", "");

        // 存入 Redis，设置 5 分钟过期
        String redisKey = WechatLoginConstants.WECHAT_LOGIN_STATE_PREFIX + state;
        redisTemplate.opsForValue().set(redisKey, "1", WechatLoginConstants.STATE_EXPIRE_SECONDS, TimeUnit.SECONDS);

        // 构建二维码 URL
        String qrCodeUrl = String.format("%s?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect",
                WechatLoginConstants.WECHAT_QRCODE_URL,
                wechatConfig.getAppId(),
                wechatConfig.getRedirectUri(),
                state);

        WechatQrCodeResponse response = new WechatQrCodeResponse();
        response.setQrCodeUrl(qrCodeUrl);
        response.setState(state);
        response.setExpireSeconds(WechatLoginConstants.STATE_EXPIRE_SECONDS);

        return response;
    }

    @Override
    public Map<String, Object> wechatLogin(WechatLoginDTO dto) {
        // 验证 state
        String stateKey = WechatLoginConstants.WECHAT_LOGIN_STATE_PREFIX + dto.getState();
        Boolean hasKey = redisTemplate.hasKey(stateKey);
        if (hasKey == null || !hasKey) {
            throw new RuntimeException("授权已过期，请重新扫码");
        }

        // 使用后立即删除 state，防止重放攻击
        redisTemplate.delete(stateKey);

        // 通过 code 换取 access_token 和 openid
        Map<String, Object> tokenResult = getAccessToken(dto.getCode());
        String openid = (String) tokenResult.get("openid");
        String accessToken = (String) tokenResult.get("access_token");

        // 根据 openid 查询用户
        SysUser user = sysUserService.getByWechatOpenid(openid);

        // 用户不存在，自动创建
        if (user == null) {
            // 获取用户信息（昵称、头像等）
            Map<String, Object> userInfo = getUserInfo(accessToken, openid);
            String nickname = (String) userInfo.get("nickname");

            user = new SysUser();
            user.setWechatOpenid(openid);
            user.setName(nickname);
            user.setUsername(WechatLoginConstants.DEFAULT_USERNAME_PREFIX + openid.substring(0, 8));
            user.setPassword(passwordEncoder.encode(WechatLoginConstants.DEFAULT_PASSWORD_PREFIX + UUID.randomUUID()));
            user.setStatus(1);
            sysUserService.save(user);
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }

        // 生成 token
        String token = sysJwtService.generateToken(user.getId(), user.getUsername());

        // 获取用户权限
        Set<String> permissionCodes = sysUserService.getUserPermissionCodes(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", user);
        result.put("permissionCodes", permissionCodes);

        return result;
    }

    @Override
    public void bindWechat(String userId, BindWechatDTO dto) {
        // 验证 state
        String stateKey = WechatLoginConstants.WECHAT_LOGIN_STATE_PREFIX + dto.getState();
        Boolean hasKey = redisTemplate.hasKey(stateKey);
        if (hasKey == null || !hasKey) {
            throw new RuntimeException("授权已过期，请重新扫码");
        }
        redisTemplate.delete(stateKey);

        // 通过 code 换取 openid
        Map<String, Object> tokenResult = getAccessToken(dto.getCode());
        String openid = (String) tokenResult.get("openid");

        // 检查该微信是否已被其他账号绑定
        SysUser existingUser = sysUserService.getByWechatOpenid(openid);
        if (existingUser != null && !existingUser.getId().equals(userId)) {
            throw new RuntimeException("该微信已被其他账号绑定");
        }

        // 检查当前账号是否已绑定微信
        SysUser currentUser = sysUserService.getById(userId);
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        }
        if (currentUser.getWechatOpenid() != null && !currentUser.getWechatOpenid().isEmpty()) {
            throw new RuntimeException("当前账号已绑定微信，请先解绑");
        }

        // 绑定微信
        currentUser.setWechatOpenid(openid);
        sysUserService.updateById(currentUser);
    }

    @Override
    public void unbindWechat(String userId, UnbindWechatDTO dto) {
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 检查是否有其他登录方式
        boolean hasPhone = user.getPhone() != null && !user.getPhone().isEmpty();
        boolean hasPassword = user.getPassword() != null && !user.getPassword().isEmpty();
        if (!hasPhone && !hasPassword) {
            throw new RuntimeException("解绑后无法登录，请先设置密码或绑定手机号");
        }

        // 解绑微信
        user.setWechatOpenid(null);
        sysUserService.updateById(user);
    }

    @Override
    public Map<String, Object> getWechatStatus(String userId) {
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        Map<String, Object> result = new HashMap<>();
        boolean binded = user.getWechatOpenid() != null && !user.getWechatOpenid().isEmpty();
        result.put("binded", binded);

        return result;
    }

    /**
     * 通过 code 换取 access_token 和 openid
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getAccessToken(String code) {
        String url = String.format("%s?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                WechatLoginConstants.WECHAT_ACCESS_TOKEN_URL,
                wechatConfig.getAppId(),
                wechatConfig.getAppSecret(),
                code);

        try {
            Map<String, Object> result = restTemplate.getForObject(url, Map.class);
            if (result == null) {
                throw new RuntimeException("微信接口调用失败");
            }

            // 检查错误码
            if (result.containsKey("errcode") && result.get("errcode") != null) {
                Integer errcode = (Integer) result.get("errcode");
                if (errcode == WechatLoginConstants.ERROR_CODE_INVALID_CODE) {
                    throw new RuntimeException("授权码无效或已过期，请重新扫码");
                }
                String errmsg = (String) result.get("errmsg");
                throw new RuntimeException("微信登录失败: " + errmsg);
            }

            return result;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("网络繁忙，请稍后重试");
        }
    }

    /**
     * 获取用户信息
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getUserInfo(String accessToken, String openid) {
        String url = String.format("%s?access_token=%s&openid=%s",
                WechatLoginConstants.WECHAT_USER_INFO_URL,
                accessToken,
                openid);

        try {
            Map<String, Object> result = restTemplate.getForObject(url, Map.class);
            if (result == null) {
                throw new RuntimeException("获取用户信息失败");
            }

            // 检查错误码
            if (result.containsKey("errcode") && result.get("errcode") != null) {
                String errmsg = (String) result.get("errmsg");
                throw new RuntimeException("获取用户信息失败: " + errmsg);
            }

            return result;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("网络繁忙，请稍后重试");
        }
    }

}
