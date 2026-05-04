package com.bulain.mybatis.sys.controller;

import com.bulain.mybatis.sys.common.Result;
import com.bulain.mybatis.sys.dto.*;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.service.SysJwtService;
import com.bulain.mybatis.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        // TODO: 实现用户名密码登录逻辑
        SysUser user = sysUserService.getByUsername(dto.getUsername());
        if (user == null) {
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
        // TODO: 实现手机号验证码登录逻辑
        SysUser user = sysUserService.getByPhone(dto.getPhone());
        if (user == null) {
            return Result.error("手机号未注册");
        }

        // TODO: 验证验证码

        Set<String> permissionCodes = sysUserService.getUserPermissionCodes(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", sysJwtService.generateToken(user.getId(), user.getUsername()));
        result.put("userInfo", user);
        result.put("permissionCodes", permissionCodes);

        return Result.success(result);
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
