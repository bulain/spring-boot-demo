package com.bulain.mybatis.sys.controller;

import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.common.Result;
import com.bulain.mybatis.sys.dto.*;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 权限控制器
 */
@RestController
@RequestMapping("/api/sys/permissions")
public class SysPermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;

    @PostMapping
    public Result<SysPermission> createPermission(@RequestBody CreatePermissionDTO dto) {
        SysPermission permission = sysPermissionService.createPermission(dto);
        return Result.success(permission);
    }

    @GetMapping("/{id}")
    public Result<SysPermission> getPermissionById(@PathVariable("id") String id) {
        SysPermission permission = sysPermissionService.getById(id);
        return Result.success(permission);
    }

    @PutMapping("/{id}")
    public Result<SysPermission> updatePermission(@PathVariable("id") String id, @RequestBody UpdatePermissionDTO dto) {
        SysPermission permission = sysPermissionService.updatePermission(id, dto);
        return Result.success(permission);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePermission(@PathVariable("id") String id) {
        sysPermissionService.removeById(id);
        return Result.success();
    }

    @GetMapping
    public Result<Paged<SysPermission>> listPermissions(PermissionQueryDTO query) {
        Paged<SysPermission> page = sysPermissionService.pagePermissions(query);
        return Result.success(page);
    }

}
