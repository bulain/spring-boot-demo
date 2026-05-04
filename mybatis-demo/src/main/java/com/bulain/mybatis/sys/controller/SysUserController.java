package com.bulain.mybatis.sys.controller;

import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.common.Result;
import com.bulain.mybatis.sys.dto.*;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.excel.SysUserExcel;
import com.bulain.mybatis.sys.service.SysExcelService;
import com.bulain.mybatis.sys.service.SysUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/sys/users")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysExcelService sysExcelService;

    @PostMapping
    public Result<SysUser> createUser(@RequestBody CreateUserDTO dto) {
        SysUser user = sysUserService.createUser(dto);
        return Result.success(user);
    }

    @GetMapping("/{id}")
    public Result<SysUser> getUserById(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.success(user);
    }

    @PutMapping("/{id}")
    public Result<SysUser> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
        SysUser user = sysUserService.updateUser(id, dto);
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.success();
    }

    @GetMapping
    public Result<Paged<SysUser>> listUsers(UserQueryDTO query) {
        Paged<SysUser> page = sysUserService.pageUsers(query);
        return Result.success(page);
    }

    @PutMapping("/{id}/status")
    public Result<Void> toggleStatus(@PathVariable Long id, @RequestBody Integer status) {
        sysUserService.toggleStatus(id, status);
        return Result.success();
    }

    @PutMapping("/{id}/password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestBody String newPassword) {
        sysUserService.resetPassword(id, newPassword);
        return Result.success();
    }

    @GetMapping("/{id}/roles")
    public Result<List<SysRole>> getUserRoles(@PathVariable Long id) {
        List<SysRole> roles = sysUserService.getUserRoles(id);
        return Result.success(roles);
    }

    @PutMapping("/{id}/roles")
    public Result<Void> assignRoles(@PathVariable Long id, @RequestBody UserRoleAssignDTO dto) {
        sysUserService.assignRoles(id, dto.getRoleIds());
        return Result.success();
    }

    @GetMapping("/{id}/permissions")
    public Result<Set<String>> getUserPermissions(@PathVariable Long id) {
        Set<String> permissionCodes = sysUserService.getUserPermissionCodes(id);
        return Result.success(permissionCodes);
    }

    @PostMapping("/export")
    public void exportUsers(HttpServletResponse response, UserQueryDTO query) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("用户列表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        Paged<SysUser> page = sysUserService.pageUsers(query);
        List<SysUserExcel> excelList = page.getData().stream()
                .map(this::convertToExcel)
                .collect(Collectors.toList());

        sysExcelService.exportUsers(response.getOutputStream(), excelList);
    }

    @PostMapping("/import")
    public Result<Integer> importUsers(@RequestParam("file") MultipartFile file) throws IOException {
        List<SysUser> users = sysExcelService.importUsers(file.getInputStream());
        sysUserService.saveBatch(users);
        return Result.success(users.size());
    }

    @GetMapping("/template")
    public void downloadUserTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("用户导入模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        sysExcelService.downloadUserTemplate(response.getOutputStream());
    }

    private SysUserExcel convertToExcel(SysUser user) {
        SysUserExcel excel = new SysUserExcel();
        excel.setUsername(user.getUsername());
        excel.setName(user.getName());
        excel.setEmail(user.getEmail());
        excel.setPhone(user.getPhone());
        excel.setStatus(user.getStatus() == 1 ? "启用" : "禁用");
        return excel;
    }

}
