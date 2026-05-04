package com.bulain.mybatis.sys.dao;

import com.bulain.mybatis.core.dao.DirectMapper;
import com.bulain.mybatis.core.dao.PagedMapper;
import com.bulain.mybatis.sys.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysRolePermissionMapper extends PagedMapper<SysRolePermission>, DirectMapper<SysRolePermission> {

}
