package com.bulain.mybatis.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.core.service.PagedServiceImpl;
import com.bulain.mybatis.sys.dao.SysRoleMapper;
import com.bulain.mybatis.sys.dto.CreateRoleDTO;
import com.bulain.mybatis.sys.dto.RoleQueryDTO;
import com.bulain.mybatis.sys.dto.UpdateRoleDTO;
import org.springframework.util.StringUtils;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysRolePermission;
import com.bulain.mybatis.sys.service.SysPermissionService;
import com.bulain.mybatis.sys.service.SysRolePermissionService;
import com.bulain.mybatis.sys.service.SysRoleService;
import com.bulain.mybatis.sys.entity.SysPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Service
public class SysRoleServiceImpl extends PagedServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRole createRole(CreateRoleDTO dto) {
        SysRole role = new SysRole();
        role.setName(dto.getName());
        role.setCode(dto.getCode());
        role.setDescription(dto.getDescription());
        baseMapper.insert(role);
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRole updateRole(Long id, UpdateRoleDTO dto) {
        SysRole role = baseMapper.selectById(id);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        if (dto.getName() != null) {
            role.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            role.setDescription(dto.getDescription());
        }
        baseMapper.updateById(role);
        return role;
    }

    @Override
    public List<SysPermission> getRolePermissions(Long roleId) {
        List<SysRolePermission> rolePermissions = sysRolePermissionService.list(
            new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId)
        );
        List<Long> permissionIds = rolePermissions.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());
        if (permissionIds.isEmpty()) {
            return List.of();
        }
        return sysPermissionService.listByIds(permissionIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // 删除现有权限关联
        sysRolePermissionService.remove(
            new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId)
        );
        // 添加新权限关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<SysRolePermission> rolePermissions = permissionIds.stream().map(permissionId -> {
                SysRolePermission rolePermission = new SysRolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                return rolePermission;
            }).collect(Collectors.toList());
            sysRolePermissionService.saveBatch(rolePermissions);
        }
    }

    @Override
    public SysRole getByCode(String code) {
        return baseMapper.selectOne(
            new LambdaQueryWrapper<SysRole>().eq(SysRole::getCode, code)
        );
    }

    @Override
    public Paged<SysRole> pageRoles(RoleQueryDTO query) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getCode())) {
            wrapper.like(SysRole::getCode, query.getCode());
        }
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SysRole::getName, query.getName());
        }
        wrapper.eq(SysRole::getDr, 0);
        wrapper.orderByDesc(SysRole::getCreatedAt);

        Page<SysRole> page = new Page<>(query.getCurrent() != null ? query.getCurrent() : 1,
                query.getSize() != null ? query.getSize() : 10);
        Page<SysRole> result = baseMapper.selectPage(page, wrapper);
        return Paged.from(result);
    }

}
