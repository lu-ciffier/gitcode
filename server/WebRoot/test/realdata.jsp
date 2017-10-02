<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>远程温室数据实时操作中心</title>
	<meta name="viewport" content="width=device-width,initial-scale=1"charset="UTF-8">
	<script type="text/javascript" src="javascript/jquery-1.8.2.js"></script>
	<script type="text/javascript" src="javascript/jquery.mobile-1.4.5.js"></script>
	<script type="text/javascript" src="./echarts/echarts.js"></script>
	<script type="text/javascript" src="javascript/drawutils.js"></script>
	<link rel="stylesheet" href="css/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" href="css/realdata.css">
	<script type="text/javascript">
		
	</script>
	<style type="text/css">
		#tpoint{
		
		font-size: 20px;
		}
	
	</style>
</head>
<body>
		<div>
		<h3>实时数据采集</h3>
		<hr>
		<table id="tstate">
			<tr id="operatebutton">
				<td><input type="button" id="start" value="获取拓扑" onclick="start()"
					style="background-color:green" data-inline="true"/>
				</td>
				<td><input type="button" id="stop" value="停止刷新" onclick="stop()"
					 style="background-color:red" data-inline="true"/>
				</td>
			</tr>
			<tr id="systemstate">
				<td><input type="text" disabled="disabled" id="statep" value="当前串口："></td>
				<td><input type="text" disabled="disabled" id="stateu" value="当前用户："></td>
			</tr>
		</table>
		<table  id="tpoint" style="display:none">
			
			<tr id="pointlist">
				<th>节点代号</th>
				<th>物理地址</th>
				<th>节点操作</th>
			</tr>
		</table>
		
		<table id="tresult" style="display:none">			
			<tr>
				<th>节点/N</th>
				<th>空气温度/C</th>
				<th>空气湿度/%</th>
				<th>光照强度/T</th>
				<th>CO2浓度/PPM</th>
				<th>土壤温度/C</th>
				<th>土壤湿度/%</th>
				<th>电压/V</th>
				<th>时间/H:M</th>
			</tr>
		</table>
		<div id="chart" hidden="hidden">
		<div id="air_tem" style="height:200px"></div>
		<div id="air_hum" style="height:200px"></div>
		<div id="ill" style="height:200px"></div>
		<div id="co2" style="height:200px"></div>
		<div id="soil_tem" style="height:200px"></div>
		<div id="soil_hum" style="height:200px"></div>
		<div id="vol" style="height:200px"></div>
		</div>
		</div>	
	<script type="text/javascript">

		var head = "26525341";
		var foot = "000000000000000000000000000000000000002A";
		var point = "";
		var pointnum = "";
		var data = "";
		var port = "";
		var readtimer;
		var statetimer;
		
		function state() {			
			var td1 = $("#statep");
			var td2 = $("#stateu");
			td1.val("当前串口：" + port);
			td2.val("当前用户：${sessionScope.username}");		
		}
		function start() {
			getPort();
			readtimer = window.setInterval("read()", 3000);
			$("#start").attr("disabled", "disabled");
			$("#stop").removeAttr("disabled");
			$.post("${pageContext.request.contextPath}/open.action?time="
					+ new Date().getTime(), function(data) {
				if ($.trim(data) != "") {
					$("#tpoint tr:gt(0)").remove();
					tpoint(data);
				}
			});		
		}
		function stop() {
					window.clearInterval(readtimer);						
					/* $.post("${pageContext.request.contextPath}/stop?time="
							+ new Date().getTime(), function() {
					}); */
					$("#start").removeAttr("disabled");
					$("#stop").attr("disabled", "disabled");
		}

		
		function read() {
			$.post("${pageContext.request.contextPath}/realdata.action?time="
					+ new Date().getTime(), function(data) {
				if (data != "") {
					//tresult(data);
					drawtable(data,"tresult",true);
					$("#chart").removeAttr("hidden");
					drawchart("tresult",false);
				}
			});
		}
		function getPort(){
			$.post("${pageContext.request.contextPath}/systemconfig.action?time="
					+ new Date().getTime(), function(data) {
				if (data != "") {
					port = data;
					state();
				}
			});
		}
		function tpoint(data) {
			var $sends = data.split("<br>");
			var button_collect = "<input type='button' value='采集' onclick='point_start(this)' />";
			var button_puase = "<input type='button' value='暂停'  disabled onclick='point_stop(this)' />";
			for (var i = 0; i < $sends.length; i++) {
				if ($sends[i] != "") {
					var $tr = $("<tr>");
					var $points = $sends[i].split(",");
					var $td1 = $("<td align='center'>" + $points[0] + "</td>");
					var $td2 = $("<td align='center'>" + $points[1] + "</td>");
					var $td3 = $("<td align='center'>" + button_collect
							+ button_puase + "</td>");
					$tr.append($td1);
					$tr.append($td2);
					$tr.append($td3);
					$("#tpoint").removeAttr("style");
					$("#tpoint").append($tr);
				}
			}
		}			
		function point_start(obj) {
			$(obj).attr("disabled", "disabled");
			$(obj).next().removeAttr("disabled");
			point = $(obj).parent().prev().html();
			pointnum = $(obj).parent().prev().prev().html();
			start = "26525341" + point + foot;
			var para = {
				"data" : start
			};
			$.post("${pageContext.request.contextPath}/begin.action?time="
					+ new Date().getTime(), para, function(data) {
			});
		};
		function point_stop(obj) {
			$(obj).attr("disabled", "disabled");
			$(obj).prev().removeAttr("disabled");
			point = $(obj).parent().prev().html();
			stop = "26545341" + point + foot;
			var para = {
				"data" : stop
			};
			$.post("${pageContext.request.contextPath}/stop.action?time="
					+ new Date().getTime(), para, function(data) {
			});
		};	
	</script>
</body>
</html>
