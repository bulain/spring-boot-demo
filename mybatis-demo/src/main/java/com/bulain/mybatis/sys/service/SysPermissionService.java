package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dto.CreatePermissionDTO;
import com.bulain.mybatis.sys.dto.PermissionQueryDTO;
import com.bulain.mybatis.sys.dto.UpdatePermissionDTO;
import com.bulain.mybatis.sys.entity.SysPermission;

/**
 * 权限服务接口
 */
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 创建权限
     */
    SysPermission createPermission(CreatePermissionDTO dto);

    /**
     * 更新权限
     */
    SysPermission updatePermission(String id, UpdatePermissionDTO dto);

    /**
     * 根据编码查询权限
     */
    SysPermission getByCode(String code);

    /**
     * 分页查询权限
     */
    Paged<SysPermission> pagePermissions(PermissionQueryDTO query);

}