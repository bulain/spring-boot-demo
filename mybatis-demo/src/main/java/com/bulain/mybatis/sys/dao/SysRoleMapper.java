package com.bulain.mybatis.sys.dao;

import com.bulain.mybatis.core.dao.DirectMapper;
import com.bulain.mybatis.core.dao.PagedMapper;
import com.bulain.mybatis.sys.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysRoleMapper extends PagedMapper<SysRole>, DirectMapper<SysRole> {

}
