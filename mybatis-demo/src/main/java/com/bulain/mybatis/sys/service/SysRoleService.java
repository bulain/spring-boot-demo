package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dto.CreateRoleDTO;
import com.bulain.mybatis.sys.dto.RoleQueryDTO;
import com.bulain.mybatis.sys.dto.UpdateRoleDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.excel.ImportResultVO;
import com.bulain.mybatis.sys.excel.SysRoleExcel;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 角色服务接口
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 创建角色
     */
    SysRole createRole(CreateRoleDTO dto);

    /**
     * 更新角色
     */
    SysRole updateRole(String id, UpdateRoleDTO dto);

    /**
     * 查询角色权限列表
     */
    List<SysPermission> getRolePermissions(String roleId);

    /**
     * 分配角色权限
     */
    void assignPermissions(String roleId, List<String> permissionIds);

    /**
     * 根据编码查询角色
     */
    SysRole getByCode(String code);

    /**
     * 分页查询角色
     */
    Paged<SysRole> pageRoles(RoleQueryDTO query);

    /**
     * 导出角色
     */
    void export(RoleQueryDTO query, HttpServletResponse response);

    /**
     * 导出指定角色
     */
    void exportByIds(List<String> ids, HttpServletResponse response);

    /**
     * 导入角色
     */
    ImportResultVO importExcel(List<SysRoleExcel> dataList);

}