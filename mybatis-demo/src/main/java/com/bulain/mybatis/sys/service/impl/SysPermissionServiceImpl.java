package com.bulain.mybatis.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.core.service.PagedServiceImpl;
import com.bulain.mybatis.sys.dao.SysPermissionMapper;
import com.bulain.mybatis.sys.dto.CreatePermissionDTO;
import com.bulain.mybatis.sys.dto.PermissionQueryDTO;
import com.bulain.mybatis.sys.dto.UpdatePermissionDTO;
import org.springframework.util.StringUtils;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.service.SysPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权限服务实现类
 */
@Service
public class SysPermissionServiceImpl extends PagedServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysPermission createPermission(CreatePermissionDTO dto) {
        SysPermission permission = new SysPermission();
        permission.setName(dto.getName());
        permission.setCode(dto.getCode());
        permission.setResourceType(dto.getResourceType());
        permission.setDescription(dto.getDescription());
        baseMapper.insert(permission);
        return permission;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysPermission updatePermission(Long id, UpdatePermissionDTO dto) {
        SysPermission permission = baseMapper.selectById(id);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }
        if (dto.getName() != null) {
            permission.setName(dto.getName());
        }
        if (dto.getResourceType() != null) {
            permission.setResourceType(dto.getResourceType());
        }
        if (dto.getDescription() != null) {
            permission.setDescription(dto.getDescription());
        }
        baseMapper.updateById(permission);
        return permission;
    }

    @Override
    public SysPermission getByCode(String code) {
        return baseMapper.selectOne(
            new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getCode, code)
        );
    }

    @Override
    public Paged<SysPermission> pagePermissions(PermissionQueryDTO query) {
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getCode())) {
            wrapper.like(SysPermission::getCode, query.getCode());
        }
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SysPermission::getName, query.getName());
        }
        if (StringUtils.hasText(query.getResourceType())) {
            wrapper.eq(SysPermission::getResourceType, query.getResourceType());
        }
        wrapper.orderByDesc(SysPermission::getCreatedAt);

        Page<SysPermission> page = new Page<>(query.getCurrent() != null ? query.getCurrent() : 1,
                query.getSize() != null ? query.getSize() : 10);
        Page<SysPermission> result = baseMapper.selectPage(page, wrapper);
        return Paged.from(result);
    }

}
