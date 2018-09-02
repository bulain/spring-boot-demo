<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<!DOCTYPE html>
<html>
<head>
	<title>首页</title>
</head>
<body>
	<div class="container">
		<div class="row text-center">恭喜你，成功登录了</div>
		<div class="row text-center"><shiro:authenticated>认证用户</shiro:authenticated></div>
		<div class="row text-center"><shiro:guest>匿名用户</shiro:guest></div>
		<div class="row text-center"><shiro:notAuthenticated>未认证用户</shiro:notAuthenticated></div>
		<div class="row text-center"><shiro:principal></shiro:principal></div>
		<div class="row text-center"><shiro:user>访问用户</shiro:user></div>
		<div class="row text-center"><shiro:hasAnyRoles name="ROLE_A,ROLE_C">任意角色(ROLE_A,ROLE_C)用户</shiro:hasAnyRoles></div>
		<div class="row text-center"><shiro:hasRole name="ROLE_A">有角色(ROLE_A)用户</shiro:hasRole></div>
		<div class="row text-center"><shiro:lacksRole name="ROLE_C">无角色(ROLE_C)用户</shiro:lacksRole></div>
		<div class="row text-center"><shiro:hasPermission name="PERM_A">有权限(PERM_A)用户</shiro:hasPermission></div>
		<div class="row text-center"><shiro:lacksPermission name="PERM_C">无权限(PERM_C)用户</shiro:lacksPermission></div>
	</div>
</body>