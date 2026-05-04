package com.bulain.mybatis.sys.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dao.SysPermissionMapper;
import com.bulain.mybatis.sys.dao.SysRoleMapper;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dao.SysUserRoleMapper;
import com.bulain.mybatis.sys.dto.CreateUserDTO;
import com.bulain.mybatis.sys.dto.UpdateUserDTO;
import com.bulain.mybatis.sys.dto.UserQueryDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.entity.SysUserRole;
import com.bulain.mybatis.sys.excel.ImportResultVO;
import com.bulain.mybatis.sys.excel.SysUserExcel;
import com.bulain.mybatis.sys.excel.UserImportListener;
import com.bulain.mybatis.sys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private PasswordPolicyService passwordPolicyService;

    @Autowired
    private LoginSecurityService loginSecurityService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUser createUser(CreateUserDTO dto) {
        // 验证密码策略
        String passwordError = passwordPolicyService.validatePassword(dto.getPassword(), dto.getUsername());
        if (passwordError != null) {
            throw new RuntimeException(passwordError);
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setStatus(1);
        baseMapper.insert(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUser updateUser(String id, UpdateUserDTO dto) {
        SysUser user = baseMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        baseMapper.updateById(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(String id, Integer status) {
        SysUser user = baseMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus(status);
        baseMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String id, String newPassword) {
        SysUser user = baseMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码策略
        String passwordError = passwordPolicyService.validatePassword(newPassword, user.getUsername());
        if (passwordError != null) {
            throw new RuntimeException(passwordError);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        baseMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(String userId, String oldPassword, String newPassword) {
        SysUser user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码不正确");
        }

        // 验证新密码策略
        String passwordError = passwordPolicyService.validatePassword(newPassword, user.getUsername());
        if (passwordError != null) {
            throw new RuntimeException(passwordError);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        baseMapper.updateById(user);
    }

    @Override
    public void unlockUser(String userId) {
        loginSecurityService.unlockUser(userId);
    }

    @Override
    public List<SysRole> getUserRoles(String userId) {
        List<SysUserRole> userRoles = sysUserRoleService.list(
            new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)
        );
        List<String> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        if (roleIds.isEmpty()) {
            return List.of();
        }
        return sysRoleService.listByIds(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(String userId, List<String> roleIds) {
        // 删除现有角色关联
        sysUserRoleService.remove(
            new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)
        );
        // 添加新角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            List<SysUserRole> userRoles = roleIds.stream().map(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                return userRole;
            }).collect(Collectors.toList());
            sysUserRoleService.saveBatch(userRoles);
        }
    }

    @Override
    public Paged<SysUser> pageUsers(UserQueryDTO query) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getUsername())) {
            wrapper.like(SysUser::getUsername, query.getUsername());
        }
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SysUser::getName, query.getName());
        }
        if (StringUtils.hasText(query.getPhone())) {
            wrapper.like(SysUser::getPhone, query.getPhone());
        }
        if (query.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(SysUser::getCreatedAt);

        Page<SysUser> page = new Page<>(query.getCurrent() != null ? query.getCurrent() : 1,
                query.getSize() != null ? query.getSize() : 10);
        Page<SysUser> result = baseMapper.selectPage(page, wrapper);
        return Paged.from(result);
    }

    @Override
    public Set<String> getUserPermissionCodes(String userId) {
        // 查询用户的角色
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
            new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)
        );
        if (userRoles.isEmpty()) {
            return Collections.emptySet();
        }

        List<String> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());

        // 查询角色对应的权限
        List<SysRole> roles = sysRoleMapper.selectBatchIds(roleIds);
        List<String> enabledRoleIds = roles.stream()
                .filter(r -> r.getDr() == 0) // 只考虑未删除的角色
                .map(SysRole::getId)
                .collect(Collectors.toList());

        if (enabledRoleIds.isEmpty()) {
            return Collections.emptySet();
        }

        // 查询所有权限
        Set<String> permissionCodes = new HashSet<>();
        for (String roleId : enabledRoleIds) {
            List<SysPermission> permissions = sysPermissionMapper.selectList(
                new LambdaQueryWrapper<SysPermission>().inSql(SysPermission::getId,
                        "SELECT permission_id FROM sys_role_permissions WHERE role_id = '" + roleId + "'")
            );
            permissions.stream()
                    .map(SysPermission::getCode)
                    .filter(Objects::nonNull)
                    .forEach(permissionCodes::add);
        }

        return permissionCodes;
    }

    @Override
    public SysUser getByUsername(String username) {
        return baseMapper.selectOne(
            new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
        );
    }

    @Override
    public SysUser getByPhone(String phone) {
        return baseMapper.selectOne(
            new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, phone)
        );
    }

    @Override
    public SysUser getByWechatOpenid(String openid) {
        return baseMapper.selectOne(
            new LambdaQueryWrapper<SysUser>().eq(SysUser::getWechatOpenid, openid)
        );
    }

    @Override
    public SysUser getByDingtalkUserid(String userid) {
        return baseMapper.selectOne(
            new LambdaQueryWrapper<SysUser>().eq(SysUser::getDingtalkUserid, userid)
        );
    }

    @Override
    public ImportResultVO importExcel(InputStream inputStream) {
        UserImportListener listener = new UserImportListener(this::processImportBatch);
        EasyExcel.read(inputStream, SysUserExcel.class, listener).sheet().doRead();
        return listener.getResult();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public ImportResultVO processImportBatch(List<SysUserExcel> batch) {
        ImportResultVO batchResult = new ImportResultVO();

        List<SysUser> insertList = new ArrayList<>();
        List<SysUser> updateList = new ArrayList<>();

        for (SysUserExcel excel : batch) {
            SysUser existing = getByUsername(excel.getUsername());
            if (existing != null) {
                // 更新
                existing.setName(excel.getName());
                existing.setEmail(excel.getEmail());
                existing.setPhone(excel.getPhone());
                existing.setStatus("启用".equals(excel.getStatus()) ? 1 : 0);
                updateList.add(existing);
            } else {
                // 新增
                SysUser user = new SysUser();
                user.setUsername(excel.getUsername());
                user.setName(excel.getName());
                user.setEmail(excel.getEmail());
                user.setPhone(excel.getPhone());
                user.setStatus("启用".equals(excel.getStatus()) ? 1 : 0);
                // 默认密码：手机号后6位，无手机号则为 "123456"
                String defaultPassword = (excel.getPhone() != null && excel.getPhone().length() >= 6)
                        ? excel.getPhone().substring(excel.getPhone().length() - 6)
                        : "123456";
                user.setPassword(passwordEncoder.encode(defaultPassword));
                insertList.add(user);
            }
        }

        if (!insertList.isEmpty()) {
            saveBatch(insertList);
            batchResult.setSuccessCount(insertList.size());
        }
        if (!updateList.isEmpty()) {
            updateBatchById(updateList);
            batchResult.setUpdateCount(updateList.size());
        }

        return batchResult;
    }

}