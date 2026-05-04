package com.bulain.mybatis.sys.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dao.SysRoleMapper;
import com.bulain.mybatis.sys.dto.CreateRoleDTO;
import com.bulain.mybatis.sys.dto.RoleQueryDTO;
import com.bulain.mybatis.sys.dto.UpdateRoleDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysRolePermission;
import com.bulain.mybatis.sys.excel.ImportResultVO;
import com.bulain.mybatis.sys.excel.SysRoleExcel;
import com.bulain.mybatis.sys.service.SysPermissionService;
import com.bulain.mybatis.sys.service.SysRolePermissionService;
import com.bulain.mybatis.sys.service.SysRoleService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

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
    public SysRole updateRole(String id, UpdateRoleDTO dto) {
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
    public List<SysPermission> getRolePermissions(String roleId) {
        List<SysRolePermission> rolePermissions = sysRolePermissionService.list(
            new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId)
        );
        List<String> permissionIds = rolePermissions.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());
        if (permissionIds.isEmpty()) {
            return List.of();
        }
        return sysPermissionService.listByIds(permissionIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(String roleId, List<String> permissionIds) {
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
        wrapper.orderByDesc(SysRole::getCreatedAt);

        Page<SysRole> page = new Page<>(query.getCurrent() != null ? query.getCurrent() : 1,
                query.getSize() != null ? query.getSize() : 10);
        Page<SysRole> result = baseMapper.selectPage(page, wrapper);
        return Paged.from(result);
    }

    @Override
    public void export(RoleQueryDTO query, HttpServletResponse response) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getCode())) {
            wrapper.like(SysRole::getCode, query.getCode());
        }
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SysRole::getName, query.getName());
        }
        wrapper.orderByDesc(SysRole::getCreatedAt);

        List<SysRole> list = baseMapper.selectList(wrapper);
        List<SysRoleExcel> excelList = list.stream().map(role -> {
            SysRoleExcel excel = new SysRoleExcel();
            BeanUtils.copyProperties(role, excel);
            return excel;
        }).collect(Collectors.toList());

        writeExcel(response, excelList);
    }

    @Override
    public void exportByIds(List<String> ids, HttpServletResponse response) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("请选择要导出的角色");
        }
        List<SysRole> list = baseMapper.selectBatchIds(ids);
        List<SysRoleExcel> excelList = list.stream().map(role -> {
            SysRoleExcel excel = new SysRoleExcel();
            BeanUtils.copyProperties(role, excel);
            return excel;
        }).collect(Collectors.toList());

        writeExcel(response, excelList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResultVO importExcel(List<SysRoleExcel> dataList) {
        ImportResultVO result = new ImportResultVO();
        if (dataList == null || dataList.isEmpty()) {
            return result;
        }

        List<SysRole> insertList = new ArrayList<>();
        List<SysRole> updateList = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            SysRoleExcel excel = dataList.get(i);
            SysRole existing = getByCode(excel.getCode());
            if (existing != null) {
                // 更新
                existing.setName(excel.getName());
                existing.setDescription(excel.getDescription());
                updateList.add(existing);
                result.incrementUpdate();
            } else {
                // 新增
                SysRole role = new SysRole();
                BeanUtils.copyProperties(excel, role);
                insertList.add(role);
                result.incrementSuccess();
            }
        }

        if (!insertList.isEmpty()) {
            saveBatch(insertList);
        }
        if (!updateList.isEmpty()) {
            updateBatchById(updateList);
        }

        return result;
    }

    private void writeExcel(HttpServletResponse response, List<SysRoleExcel> data) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("角色数据", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            EasyExcel.write(response.getOutputStream(), SysRoleExcel.class)
                    .sheet("角色")
                    .doWrite(data);
        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }

}