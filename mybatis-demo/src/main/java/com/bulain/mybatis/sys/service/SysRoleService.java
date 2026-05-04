package com.bulain.mybatis.sys.service;

import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.core.service.PagedService;
import com.bulain.mybatis.sys.dto.CreateRoleDTO;
import com.bulain.mybatis.sys.dto.RoleQueryDTO;
import com.bulain.mybatis.sys.dto.UpdateRoleDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.entity.SysRole;

import java.util.List;

/**
 * 角色服务接口
 */
public interface SysRoleService extends PagedService<SysRole> {

    /**
     * 创建角色
     */
    SysRole createRole(CreateRoleDTO dto);

    /**
     * 更新角色
     */
    SysRole updateRole(Long id, UpdateRoleDTO dto);

    /**
     * 查询角色权限列表
     */
    List<SysPermission> getRolePermissions(Long roleId);

    /**
     * 分配角色权限
     */
    void assignPermissions(Long roleId, List<Long> permissionIds);

    /**
     * 根据编码查询角色
     */
    SysRole getByCode(String code);

    /**
     * 分页查询角色
     */
    Paged<SysRole> pageRoles(RoleQueryDTO query);

}
