package com.bulain.mybatis.sys.controller;

import com.bulain.mybatis.sys.common.Result;
import com.bulain.mybatis.sys.dto.*;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.service.SysJwtService;
import com.bulain.mybatis.sys.service.SysUserService;
import com.bulain.mybatis.sys.service.VerificationCodeService;
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
    public Result<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        SysUser user = sysUserService.getByUsername(dto.getUsername());
        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }

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

    @PostMapping("/wechat-login")
    public Result<Map<String, Object>> wechatLogin(@RequestBody WechatLoginDTO dto) {
        // TODO: 实现微信OAuth登录逻辑
        SysUser user = sysUserService.getByWechatOpenid("TODO: get openid from wechat code");
        if (user == null) {
            return Result.error("微信账号未绑定");
        }

        Set<String> permissionCodes = sysUserService.getUserPermissionCodes(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", sysJwtService.generateToken(user.getId(), user.getUsername()));
        result.put("userInfo", user);
        result.put("permissionCodes", permissionCodes);

        return Result.success(result);
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
