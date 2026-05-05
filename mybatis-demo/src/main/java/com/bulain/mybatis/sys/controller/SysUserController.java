package com.bulain.mybatis.sys.controller;

import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.common.Result;
import com.bulain.mybatis.sys.dto.*;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.excel.ImportResultVO;
import com.bulain.mybatis.sys.service.ExcelTemplateService;
import com.bulain.mybatis.sys.service.SysUserService;
import com.bulain.mybatis.sys.service.DingtalkLoginService;
import com.bulain.mybatis.sys.service.WechatLoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/sys/users")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private WechatLoginService wechatLoginService;

    @Autowired
    private DingtalkLoginService dingtalkLoginService;

    @Autowired
    private ExcelTemplateService excelTemplateService;

    @PostMapping
    public Result<SysUser> createUser(@RequestBody CreateUserDTO dto) {
        SysUser user = sysUserService.createUser(dto);
        return Result.success(user);
    }

    @GetMapping("/{id}")
    public Result<SysUser> getUserById(@PathVariable("id") String id) {
        SysUser user = sysUserService.getById(id);
        return Result.success(user);
    }

    @PutMapping("/{id}")
    public Result<SysUser> updateUser(@PathVariable("id") String id, @RequestBody UpdateUserDTO dto) {
        SysUser user = sysUserService.updateUser(id, dto);
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable("id") String id) {
        sysUserService.removeById(id);
        return Result.success();
    }

    @GetMapping
    public Result<Paged<SysUser>> listUsers(UserQueryDTO query) {
        Paged<SysUser> page = sysUserService.pageUsers(query);
        return Result.success(page);
    }

    @PutMapping("/{id}/status")
    public Result<Void> toggleStatus(@PathVariable("id") String id, @RequestBody Integer status) {
        sysUserService.toggleStatus(id, status);
        return Result.success();
    }

    @PutMapping("/{id}/password")
    public Result<Void> resetPassword(@PathVariable("id") String id, @RequestBody String newPassword) {
        sysUserService.resetPassword(id, newPassword);
        return Result.success();
    }

    @GetMapping("/{id}/roles")
    public Result<List<SysRole>> getUserRoles(@PathVariable("id") String id) {
        List<SysRole> roles = sysUserService.getUserRoles(id);
        return Result.success(roles);
    }

    @PutMapping("/{id}/roles")
    public Result<Void> assignRoles(@PathVariable("id") String id, @RequestBody UserRoleAssignDTO dto) {
        sysUserService.assignRoles(id, dto.getRoleIds());
        return Result.success();
    }

    @GetMapping("/{id}/permissions")
    public Result<Set<String>> getUserPermissions(@PathVariable("id") String id) {
        Set<String> permissionCodes = sysUserService.getUserPermissionCodes(id);
        return Result.success(permissionCodes);
    }

    @PostMapping("/export")
    public void exportUsers(HttpServletResponse response, UserQueryDTO query) {
        sysUserService.export(query, response);
    }

    @PostMapping("/export/selected")
    public void exportUsersByIds(@RequestBody List<String> ids, HttpServletResponse response) {
        sysUserService.exportByIds(ids, response);
    }

    @PostMapping("/import")
    public Result<ImportResultVO> importUsers(@RequestParam("file") MultipartFile file) throws IOException {
        ImportResultVO result = sysUserService.importExcel(file.getInputStream());
        return Result.success(result);
    }

    /**
     * 下载用户导入模板
     * 需管理员权限
     */
    @GetMapping("/template")
    public void downloadUserTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("用户导入模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        excelTemplateService.downloadUserTemplate(response.getOutputStream());
    }

    /**
     * 绑定微信账号
     */
    @PostMapping("/{id}/bind-wechat")
    public Result<Void> bindWechat(@PathVariable("id") String userId, @RequestBody BindWechatDTO dto) {
        try {
            wechatLoginService.bindWechat(userId, dto);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 解绑微信账号
     */
    @PostMapping("/{id}/unbind-wechat")
    public Result<Void> unbindWechat(@PathVariable("id") String userId, @RequestBody UnbindWechatDTO dto) {
        try {
            wechatLoginService.unbindWechat(userId, dto);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询微信绑定状态
     */
    @GetMapping("/{id}/wechat-status")
    public Result<Map<String, Object>> getWechatStatus(@PathVariable("id") String userId) {
        try {
            Map<String, Object> result = wechatLoginService.getWechatStatus(userId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 绑定钉钉账号
     */
    @PostMapping("/{id}/bind-dingtalk")
    public Result<Void> bindDingtalk(@PathVariable("id") String userId, @RequestBody BindDingtalkDTO dto) {
        try {
            dingtalkLoginService.bindDingtalk(userId, dto);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 解绑钉钉账号
     */
    @PostMapping("/{id}/unbind-dingtalk")
    public Result<Void> unbindDingtalk(@PathVariable("id") String userId, @RequestBody UnbindDingtalkDTO dto) {
        try {
            dingtalkLoginService.unbindDingtalk(userId, dto);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询钉钉绑定状态
     */
    @GetMapping("/{id}/dingtalk-status")
    public Result<Map<String, Object>> getDingtalkStatus(@PathVariable("id") String userId) {
        try {
            Map<String, Object> result = dingtalkLoginService.getDingtalkStatus(userId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}
