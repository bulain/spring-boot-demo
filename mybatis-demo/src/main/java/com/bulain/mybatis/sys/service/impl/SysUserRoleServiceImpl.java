package com.bulain.mybatis.sys.service.impl;

import com.bulain.mybatis.core.service.PagedServiceImpl;
import com.bulain.mybatis.sys.dao.SysUserRoleMapper;
import com.bulain.mybatis.sys.entity.SysUserRole;
import com.bulain.mybatis.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色关联服务实现类
 */
@Service
public class SysUserRoleServiceImpl extends PagedServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

}
