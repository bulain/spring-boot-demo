package com.bulain.oauth.service;

import java.util.List;

import com.bulain.oauth.pojo.User;

public interface UserService {

	/** 根据用户名获得用户信息 */
	User getUserByLoginName(String loginName);

	/** 根据用户名获得角色列表 */
	List<String> findRoleByLoginName(String loginName);

	/** 根据用名获得权限列表 */
	List<String> findPermissionByLoginName(String loginName);

}
