package com.bulain.mybatis.sys.controller;

import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.common.Result;
import com.bulain.mybatis.sys.dto.*;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/api/sys/roles")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping
    public Result<SysRole> createRole(@RequestBody CreateRoleDTO dto) {
        SysRole role = sysRoleService.createRole(dto);
        return Result.success(role);
    }

    @GetMapping("/{id}")
    public Result<SysRole> getRoleById(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        return Result.success(role);
    }

    @PutMapping("/{id}")
    public Result<SysRole> updateRole(@PathVariable Long id, @RequestBody UpdateRoleDTO dto) {
        SysRole role = sysRoleService.updateRole(id, dto);
        return Result.success(role);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        sysRoleService.removeById(id);
        return Result.success();
    }

    @GetMapping
    public Result<Paged<SysRole>> listRoles(RoleQueryDTO query) {
        Paged<SysRole> page = sysRoleService.pageRoles(query);
        return Result.success(page);
    }

    @GetMapping("/{id}/permissions")
    public Result<List<SysPermission>> getRolePermissions(@PathVariable Long id) {
        List<SysPermission> permissions = sysRoleService.getRolePermissions(id);
        return Result.success(permissions);
    }

    @PutMapping("/{id}/permissions")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody RolePermissionAssignDTO dto) {
        sysRoleService.assignPermissions(id, dto.getPermissionIds());
        return Result.success();
    }

}
