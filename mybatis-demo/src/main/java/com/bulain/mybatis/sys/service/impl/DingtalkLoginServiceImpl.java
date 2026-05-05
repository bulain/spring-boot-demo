package com.bulain.mybatis.sys.service.impl;

import com.bulain.mybatis.sys.common.DingtalkLoginConstants;
import com.bulain.mybatis.sys.config.DingtalkOpenPlatformConfig;
import com.bulain.mybatis.sys.dto.BindDingtalkDTO;
import com.bulain.mybatis.sys.dto.DingtalkLoginDTO;
import com.bulain.mybatis.sys.dto.DingtalkQrCodeResponse;
import com.bulain.mybatis.sys.dto.UnbindDingtalkDTO;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.service.DingtalkLoginService;
import com.bulain.mybatis.sys.service.SysJwtService;
import com.bulain.mybatis.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 钉钉登录服务实现类
 */
@Service
public class DingtalkLoginServiceImpl implements DingtalkLoginService {

    @Autowired
    private DingtalkOpenPlatformConfig dingtalkConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysJwtService sysJwtService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public DingtalkQrCodeResponse getQrCode() {
        String state = UUID.randomUUID().toString().replace("-", "");

        String redisKey = DingtalkLoginConstants.DINGTALK_LOGIN_STATE_PREFIX + state;
        redisTemplate.opsForValue().set(redisKey, "1", DingtalkLoginConstants.STATE_EXPIRE_SECONDS, TimeUnit.SECONDS);

        String qrCodeUrl = String.format("%s?client_id=%s&redirect_uri=%s&response_type=code&scope=openid&state=%s&prompt=consent",
                DingtalkLoginConstants.DINGTALK_QRCODE_URL,
                dingtalkConfig.getAppId(),
                dingtalkConfig.getRedirectUri(),
                state);

        DingtalkQrCodeResponse response = new DingtalkQrCodeResponse();
        response.setQrCodeUrl(qrCodeUrl);
        response.setState(state);
        response.setExpireSeconds(DingtalkLoginConstants.STATE_EXPIRE_SECONDS);

        return response;
    }

    @Override
    public Map<String, Object> dingtalkLogin(DingtalkLoginDTO dto) {
        String stateKey = DingtalkLoginConstants.DINGTALK_LOGIN_STATE_PREFIX + dto.getState();
        Boolean hasKey = redisTemplate.hasKey(stateKey);
        if (hasKey == null || !hasKey) {
            throw new RuntimeException("授权已过期，请重新扫码");
        }

        redisTemplate.delete(stateKey);

        Map<String, Object> tokenResult = getUserAccessToken(dto.getAuthCode());
        String accessToken = (String) tokenResult.get("accessToken");

        Map<String, Object> userInfo = getUserInfo(accessToken);
        String userId = (String) userInfo.get("unionId");
        String nick = (String) userInfo.get("nick");

        SysUser user = sysUserService.getByDingtalkUserid(userId);

        if (user == null) {
            user = new SysUser();
            user.setDingtalkUserid(userId);
            user.setName(nick);
            user.setUsername(DingtalkLoginConstants.DEFAULT_USERNAME_PREFIX + userId.substring(0, 8));
            user.setPassword(passwordEncoder.encode(DingtalkLoginConstants.DEFAULT_PASSWORD_PREFIX + UUID.randomUUID()));
            user.setStatus(1);
            sysUserService.save(user);
        }

        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }

        String token = sysJwtService.generateToken(user.getId(), user.getUsername());
        Set<String> permissionCodes = sysUserService.getUserPermissionCodes(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", user);
        result.put("permissionCodes", permissionCodes);

        return result;
    }

    @Override
    public void bindDingtalk(String userId, BindDingtalkDTO dto) {
        String stateKey = DingtalkLoginConstants.DINGTALK_LOGIN_STATE_PREFIX + dto.getState();
        Boolean hasKey = redisTemplate.hasKey(stateKey);
        if (hasKey == null || !hasKey) {
            throw new RuntimeException("授权已过期，请重新扫码");
        }
        redisTemplate.delete(stateKey);

        Map<String, Object> tokenResult = getUserAccessToken(dto.getCode());
        String accessToken = (String) tokenResult.get("accessToken");
        Map<String, Object> userInfo = getUserInfo(accessToken);
        String dingtalkUserId = (String) userInfo.get("unionId");

        SysUser existingUser = sysUserService.getByDingtalkUserid(dingtalkUserId);
        if (existingUser != null && !existingUser.getId().equals(userId)) {
            throw new RuntimeException("该钉钉已被其他账号绑定");
        }

        SysUser currentUser = sysUserService.getById(userId);
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        }
        if (currentUser.getDingtalkUserid() != null && !currentUser.getDingtalkUserid().isEmpty()) {
            throw new RuntimeException("当前账号已绑定钉钉，请先解绑");
        }

        currentUser.setDingtalkUserid(dingtalkUserId);
        sysUserService.updateById(currentUser);
    }

    @Override
    public void unbindDingtalk(String userId, UnbindDingtalkDTO dto) {
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        boolean hasPhone = user.getPhone() != null && !user.getPhone().isEmpty();
        boolean hasWechat = user.getWechatOpenid() != null && !user.getWechatOpenid().isEmpty();
        boolean hasPassword = user.getPassword() != null && !user.getPassword().isEmpty();
        if (!hasPhone && !hasWechat && !hasPassword) {
            throw new RuntimeException("解绑后无法登录，请先设置密码或绑定手机号");
        }

        user.setDingtalkUserid(null);
        sysUserService.updateById(user);
    }

    @Override
    public Map<String, Object> getDingtalkStatus(String userId) {
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        Map<String, Object> result = new HashMap<>();
        boolean binded = user.getDingtalkUserid() != null && !user.getDingtalkUserid().isEmpty();
        result.put("binded", binded);

        return result;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getUserAccessToken(String authCode) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("clientId", dingtalkConfig.getAppId());
        requestBody.put("clientSecret", dingtalkConfig.getAppSecret());
        requestBody.put("code", authCode);
        requestBody.put("grantType", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            Map<String, Object> result = restTemplate.postForObject(
                    DingtalkLoginConstants.DINGTALK_USER_ACCESS_TOKEN_URL,
                    entity,
                    Map.class
            );
            if (result == null) {
                throw new RuntimeException("钉钉接口调用失败");
            }
            return result;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("网络繁忙，请稍后重试");
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            Map<String, Object> result = restTemplate.postForObject(
                    DingtalkLoginConstants.DINGTALK_USER_INFO_URL,
                    entity,
                    Map.class
            );
            if (result == null) {
                throw new RuntimeException("获取用户信息失败");
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
