package com.bulain.mybatis.sys.controller;

import com.bulain.mybatis.sys.common.Result;
import com.bulain.mybatis.sys.dto.*;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.service.LoginSecurityService;
import com.bulain.mybatis.sys.service.SysJwtService;
import com.bulain.mybatis.sys.service.SysUserService;
import com.bulain.mybatis.sys.service.VerificationCodeService;
import com.bulain.mybatis.sys.service.WechatLoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/sys/auth")
public class SysAuthController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysJwtService sysJwtService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private LoginSecurityService loginSecurityService;

    @Autowired
    private WechatLoginService wechatLoginService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 获取图片验证码
     */
    @GetMapping("/image-captcha")
    public Result<ImageCaptchaResponse> getImageCaptcha(HttpServletRequest request) {
        String ip = getClientIp(request);
        ImageCaptchaResponse response = verificationCodeService.generateImageCaptcha(ip);
        return Result.success(response);
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/send-code")
    public Result<Void> sendSmsCode(@RequestBody SendCodeRequest dto, HttpServletRequest request) {
        String ip = getClientIp(request);

        // 先验证图片验证码
        boolean valid = verificationCodeService.validateImageCaptcha(dto.getCaptchaId(), dto.getCaptchaCode());
        if (!valid) {
            return Result.error("图片验证码错误或已过期");
        }

        // 发送短信验证码
        verificationCodeService.sendSmsCode(dto.getPhone(), ip);
        return Result.success(null);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO dto, HttpServletRequest request) {
        String ip = getClientIp(request);
        SysUser user = sysUserService.getByUsername(dto.getUsername());

        // 先检查 IP 是否被限流（不管用户是否存在）
        if (loginSecurityService.isIpLimited(ip)) {
            // 如果用户存在，也记录一次失败计数（针对该用户）
            if (user != null) {
                loginSecurityService.recordLoginFailure(user.getId(), ip);
            }
            return Result.error("登录过于频繁，请稍后再试");
        }

        // 检查账户是否被锁定
        if (user != null && loginSecurityService.isUserLocked(user.getId())) {
            loginSecurityService.recordLoginFailure(user.getId(), ip);
            return Result.error("账户已被锁定，请稍后再试或联系管理员");
        }

        // 检查是否需要验证码
        boolean captchaRequired = loginSecurityService.isCaptchaRequired(user != null ? user.getId() : null, ip);
        if (captchaRequired) {
            // 未提供验证码
            if (dto.getCaptchaId() == null || dto.getCaptchaCode() == null ||
                    dto.getCaptchaId().isEmpty() || dto.getCaptchaCode().isEmpty()) {
                // 返回需要验证码的标记，前端需要显示验证码输入框
                Map<String, Object> result = new HashMap<>();
                result.put("captchaRequired", true);
                // 生成一个新的验证码ID供前端使用
                ImageCaptchaResponse captcha = verificationCodeService.generateImageCaptcha(ip);
                result.put("captchaId", captcha.getCaptchaId());
                result.put("captchaImage", captcha.getImageBase64());
                return Result.build(428, "请输入验证码", result);
            }

            // 验证验证码
            boolean captchaValid = verificationCodeService.validateImageCaptcha(
                    dto.getCaptchaId(), dto.getCaptchaCode());
            if (!captchaValid) {
                // 验证码错误，返回新的验证码
                Map<String, Object> result = new HashMap<>();
                result.put("captchaRequired", true);
                ImageCaptchaResponse captcha = verificationCodeService.generateImageCaptcha(ip);
                result.put("captchaId", captcha.getCaptchaId());
                result.put("captchaImage", captcha.getImageBase64());
                return Result.build(428, "验证码错误或已过期", result);
            }
        }

        // 检查用户是否存在
        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            // 密码错误，记录失败计数
            loginSecurityService.recordLoginFailure(user.getId(), ip);

            // 检查是否触发验证码
            boolean nowRequiresCaptcha = loginSecurityService.isUserCaptchaThresholdReached(user.getId()) ||
                    loginSecurityService.isIpCaptchaThresholdReached(ip);

            if (nowRequiresCaptcha) {
                Map<String, Object> result = new HashMap<>();
                result.put("captchaRequired", true);
                ImageCaptchaResponse captcha = verificationCodeService.generateImageCaptcha(ip);
                result.put("captchaId", captcha.getCaptchaId());
                result.put("captchaImage", captcha.getImageBase64());
                return Result.build(428, "密码错误次数过多，请输入验证码", result);
            }
            return Result.error("用户名或密码错误");
        }

        // 登录成功，重置计数
        loginSecurityService.recordLoginSuccess(user.getId(), ip);

        Set<String> permissionCodes = sysUserService.getUserPermissionCodes(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", sysJwtService.generateToken(user.getId(), user.getUsername()));
        result.put("userInfo", user);
        result.put("permissionCodes", permissionCodes);

        return Result.success(result);
    }

    @PostMapping("/phone-login")
    public Result<Map<String, Object>> phoneLogin(@RequestBody PhoneLoginDTO dto) {
        SysUser user = sysUserService.getByPhone(dto.getPhone());
        if (user == null) {
            return Result.error("手机号未注册");
        }

        // 验证短信验证码
        boolean valid = verificationCodeService.validateSmsCode(dto.getPhone(), dto.getCode());
        if (!valid) {
            return Result.error("验证码错误或已过期");
        }

        Set<String> permissionCodes = sysUserService.getUserPermissionCodes(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", sysJwtService.generateToken(user.getId(), user.getUsername()));
        result.put("userInfo", user);
        result.put("permissionCodes", permissionCodes);

        return Result.success(result);
    }

    /**
     * 获取客户端 IP 地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理的情况，取第一个 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取微信登录二维码参数
     */
    @GetMapping("/wechat-qrcode")
    public Result<WechatQrCodeResponse> getWechatQrCode() {
        WechatQrCodeResponse qrCode = wechatLoginService.getQrCode();
        return Result.success(qrCode);
    }

    /**
     * 微信登录
     */
    @PostMapping("/wechat-login")
    public Result<Map<String, Object>> wechatLogin(@RequestBody WechatLoginDTO dto) {
        try {
            Map<String, Object> result = wechatLoginService.wechatLogin(dto);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/dingtalk-login")
    public Result<Map<String, Object>> dingtalkLogin(@RequestBody DingtalkLoginDTO dto) {
        // TODO: 实现钉钉OAuth登录逻辑
        SysUser user = sysUserService.getByDingtalkUserid("TODO: get userid from dingtalk code");
        if (user == null) {
            return Result.error("钉钉账号未绑定");
        }

        Set<String> permissionCodes = sysUserService.getUserPermissionCodes(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", sysJwtService.generateToken(user.getId(), user.getUsername()));
        result.put("userInfo", user);
        result.put("permissionCodes", permissionCodes);

        return Result.success(result);
    }

}
