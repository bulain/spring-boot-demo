package com.bulain.shiro.conf;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;

import com.bulain.shiro.pojo.User;
import com.bulain.shiro.service.UserService;

public class ShiroRealm extends AuthorizingRealm {

	@Autowired
    @Lazy
	private UserService userService;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		if (username == null) {
			throw new AccountException("用户名不能为空");
		}

		User user = null;
		try {
			user = userService.getUserByLoginName(username);
		} catch (Exception e) {
			final String message = "查找用户[" + username + "]信息时发生错误";
			throw new AuthenticationException(message, e);
		}

		if (user == null) {
			throw new UnknownAccountException("用户[" + username + "]信息不存在");
		}

		String activeFlag = user.getActiveFlag();
		if (!"Y".equals(activeFlag)) {
			throw new LockedAccountException("用户[" + username + "]不允许登录");
		}

		String passwordHash = user.getPasswordHash();
		String slat = user.getSlat();
		if (StringUtils.isEmpty(passwordHash) || StringUtils.isEmpty(slat)) {
			throw new DisabledAccountException("用户[" + username + "]无密码，不允许登录");
		}

		AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, passwordHash.toCharArray(),
				Sha1Hash.Util.bytes(slat), getName());

		return authenticationInfo;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		// 验证
		if (principals == null) {
			throw new AuthorizationException("用户名不能为空");
		}

		String username = (String) getAvailablePrincipal(principals);

		Set<String> roleNames = new HashSet<String>();
		Set<String> permissions = new HashSet<String>();

		try {
			List<String> roles = userService.findRoleByLoginName(username);
			List<String> perms = userService.findPermissionByLoginName(username);

			roleNames.addAll(roles);
			permissions.addAll(perms);
		} catch (Exception e) {
			final String message = "查找用户[" + username + "]信息时发生错误";
			throw new AuthorizationException(message, e);
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		info.setStringPermissions(permissions);
		return info;
	}

}
