<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='s' uri='http://www.springframework.org/security/tags' %>
<!DOCTYPE html>
<html>
<head>
	<title>首页</title>
</head>
<body>
	<div class="container">
		<div class="row text-center">恭喜你，成功登录了</div>
		<div class="row text-center">
			<c:url value="/csrf" var="csrfUrl"/>
			<form action="${csrfUrl}" method="post">
				<s:csrfInput/>
				<button type="submit">重复提交</button>
			</form>
		</div>
	</div>
</body>