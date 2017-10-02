<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户和数据库管理中心</title>
	<meta name="viewport" content="width=device-width,initial-scale=1"charset="UTF-8">
	<script type="text/javascript" src="javascript/jquery-1.8.2.js"></script>
	<script type="text/javascript" src="javascript/jquery.mobile-1.4.5.js"></script>
	<link rel="stylesheet" href="css/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="css/manage.css">
	<script type="text/javascript">
	function userinfo() {
		$("#userinfo tr:gt(0)").remove();
		$.post("${pageContext.request.contextPath}/userinfo.action?time="
				+ new Date().getTime(), function(data) {
			if (data != "") {
				var $tr = $("<tr>");
				var userinfo = data.split(",");
				for(var i=0;i<userinfo.length;i++){
					var $td = $("<td align='center'>"+userinfo[i]+"</td>");
					$tr.append($td);
				}
				$("#userinfo").removeAttr("style");
				$("#userinfo").append($tr);
			}
		});
	}
	function dropdata() {
		$.post("${pageContext.request.contextPath}/dropdata.action?time="
				+ new Date().getTime(), function(data) {
		});
	}

	function quit() {
		$.post("${pageContext.request.contextPath}/userquit.action?time="
				+ new Date().getTime(), function(data) {
			top.location.href = "${pageContext.request.contextPath}/index.jsp";
		});
	}
	</script>
</head>
<body>		
		<h3>用户信息管理</h3>
		<hr>
		
		<div data-role="header">
			<div data-role="navbar">
				<ul>
				<li>
					<input type="button" id="button1" value="用户信息" onclick="userinfo()"
					data-inline="true"/>
				</li>
				<li>
					<input type="button" id="button2" value="清除数据" onclick="dropdata()"
					data-inline="true"/>
				</li>
				<li>
					<input type="button" id="button3" value="注销登陆" onclick="quit()"
					data-inline="true"/>
				</li>
				</ul>
			</div>
		</div>

		<table id="userinfo" style="display:none">
			<tr>
				<th>用户名</th>
				<th>密码</th>
				<th>注册时间</th>
			</tr>
		</table>	
</body>
</html>
