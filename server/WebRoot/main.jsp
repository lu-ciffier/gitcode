<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>	
<!DOCTYPE html>
<html>
<head> 
  	<title>远程温室数据监测系统</title>
  	<meta name="viewport" content="width=device-width,initial-scale=1"charset="UTF-8">
  	<script type="text/javascript" src="javascript/jquery-1.8.2.js"></script>
  	<script type="text/javascript" src="javascript/jquery.mobile-1.4.5.js"></script>
	<link rel="stylesheet" href="css/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" href="css/center.css">
	
	<script type="text/javascript">
		$.mobile.ajaxEnabled = false;
		var username = "${sessionScope.username}";
		if(username!="admin"){		
			//$("#config").attr("style","display:inline;");
			//$("#navlist").
		}
		var windowwidth = window.innerWidth;
		var windowheight = window.innerHeight;
		function frame(obj){				
			$(obj).attr("style","width:"+windowwidth*0.9+"px;height:"+windowheight*0.7+"px");			
		}
	</script>
	<style type="text/css">
#header {
	
	background-color: #88B855;
	width: 100%;
	height: 20%;
	
}

#content {
	position: fixed;
	background-color: #88B8F7;
	width: 100%;
	height: 77%;
}

#footer {
	position: fixed;
	left: 0;
	right: 0;
	bottom: 0;
	height: auto;
	background-color: #78B855;
	width: 100%;
	height: 3%;
}

.title {
	
	white-space: normal !important;
	font-family: Microsoft YaHei;
	font-size:x-large;
	margin-top: 2%;
}

.userstate {
	/* font-family: Microsoft YaHei; */
	font-size:small;
	margin-top: 2%;
	margin-bottom: 1%;
}


.nav {
	margin-bottom: 2px;
}
iframe {
	margin: 1%;
	
}
.content {
	
}

.footer {
	
}

.ui-navbar li .ui-btn {
	background-color: #5FE9FC;
	font-family: Microsoft YaHei;
}

</style>
</head>
<body>
	<div id="header" data-role="header" data-position="fixed">
		<div class="title">可视化温室监测系统</div>
		<div class="userstate">
			欢迎:${sessionScope.username}用户 &nbsp;<a href="#" onclick="quit()">退出</a>
		</div>
		<div data-role="navbar" class="nav" data-theme='b'>

			<ul id="navlist">
				<li><a href="map.html" target="iframe" id="a1">主页</a>
				</li>

				<li><a href="chart.html" target="iframe" id="a2">图表</a>
				</li>

				<li id="config">
					<a href="config.html" target="iframe" id="a3">设置</a>
				</li>

				<li><a href="network.html"
					target="iframe" id="a4">网络</a>
				</li>
			</ul>
		</div>
	</div>

	<div id="content" data-role="content" >
		<iframe name="iframe" onload="frame(this)"></iframe>
	</div>

	<div id="footer" data-role="footer" data-position="fixed">
		&copy;Copyright 2015-2017 Southwest University</div>
<script type="text/javascript">
	function quit(){
		$.post("${pageContext.request.contextPath}/userquit.action?time="
				+ new Date().getTime(), {platform:"web"},function(data) {			
			top.location.href = "index.html";
		});
	}
	</script>
</body>
</html>
