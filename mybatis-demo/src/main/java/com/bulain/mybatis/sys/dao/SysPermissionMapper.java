package com.bulain.mybatis.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bulain.mybatis.core.dao.DirectDeleteMapper;
import com.bulain.mybatis.sys.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission>, DirectDeleteMapper<SysPermission> {

}