<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="renderer" content="webkit">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="1">
	<link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css?1535796120647" rel="stylesheet">
	<link href="//cdn.bootcss.com/font-awesome/4.3.0/css/font-awesome.min.css?1535796120647" rel="stylesheet">
	<sitemesh:write property='head' />
	<title><sitemesh:write property='title' /></title>
</head>
<body>
    <sitemesh:write property='body' />
    <script src="//cdn.bootcss.com/jquery/2.0.3/jquery.min.js?1535796120647"></script>
	<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js?1535796120647"></script>
    <sitemesh:write property='jsscript' />
</body>
</html>