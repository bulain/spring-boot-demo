<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<link href="<c:url value='/static/css/login.css?1535796154772'/>" rel="stylesheet">
	<title>登录</title>
</head>
<body>
	<div class="container">
		<div class="form row">
			<form class="form-horizontal col-sm-offset-3 col-md-offset-3" id="login_form" action="" method="post">
				<h3 class="form-title">用户登录</h3>
				<div class="col-sm-9 col-md-9">
					<div class="form-group">
						<i class="fa fa-user fa-lg"></i>
						<input class="form-control required" type="text" placeholder="请输入用户名" name="username" autofocus="autofocus" maxlength="20"/>
					</div>
					<div class="form-group">
						<i class="fa fa-lock fa-lg"></i>
						<input class="form-control required" type="password" placeholder="请输入密码" name="password" maxlength="8"/>
					</div>
					<div class="form-group">
						<button type="submit" class="btn btn-success pull-right">登录</button>
					</div>
				</div>
			</form>
		</div>
	</div>
<jsscript>
	<script src="//cdn.bootcss.com/jquery-validate/1.17.0/jquery.validate.min.js"></script>
	<script src="//cdn.bootcss.com/jquery-validate/1.17.0/additional-methods.min.js"></script>
	<script type="text/javascript" src="<c:url value='/static/js/login.js?1535796154772'/>" ></script>
</jsscript>
</body>
</html>