<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>远程温室数据中心</title>
<meta name="viewport" content="width=device-width,initial-scale=1"
	charset="UTF-8">
<script type="text/javascript" src="javascript/jquery-1.8.2.js"></script>
<script type="text/javascript" src="javascript/jquery.mobile-1.4.5.js"></script>
<script type="text/javascript" src="echarts/echarts.js"></script>
<script type="text/javascript" src="javascript/drawutils.js"></script>
<script type="text/javascript" src="javascript/drawchart.js"></script>
<script type="text/javascript" src="javascript/jquery.multi-select.js"></script>
<link rel="stylesheet" href="css/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" href="css/multi-select.css">
<link rel="stylesheet" type="text/css" href="css/historydata.css">
<script type="text/javascript">
		$.mobile.ajaxEnabled = false;		
</script>
<style type="text/css">
/* h3{
			margin: auto !important;
			width: 20% !important;
		} */
.table-title {
	font-size: 2px;
	color: red;
}
/* fieldset {
			
            text-align:center;
            
            padding: 5px;
            margin: auto;
            border-width:2px;
            border-color:red;
            font-weight: bold;
		} */
#chartmenu a {
	text-decoration: none;
}

#newchart {
	width: 50%;
	height: auto;
	margin-top: 3%;
	border-style: solid;
	border-width: 2px;
}
#loadchart {
	width: 50%;
	height: auto;
	margin-top: 3%;
	border-style: solid;
	border-width: 2px;
}
#savechart {
	width: 50%;
	height: auto;
	margin-top: 3%;
	border-style: solid;
	border-width: 2px;
}
#deletechart {
	width: 50%;
	height: auto;
	margin-top: 3%;
	border-style: solid;
	border-width: 2px;
}
</style>
<script type="text/javascript">

</script>
</head>
<body>

	<div data-role="navbar" class="nav" data-theme='b'>

		<ul id="navlist">
			<li><a href="" id="a1" onclick="edit(this)">新建</a></li>

			<li><a href="" id="a2" onclick="edit(this)">加载</a></li>

			<li><a href="" id="a3" onclick="edit(this)">保存</a></li>

			<li><a href="" id="a4" onclick="edit(this)">删除</a></li>
		</ul>
	</div>
	<fieldset id="newchart" style="display: none;">
		<legend>新建图表</legend>
		<div id="choosepoint" data-role="fieldcontain">
			选择节点: <select name="point" id="pointtype" data-inline="true">
				<option value="739E3E01004B1200">73</option>
				<option value="449E3E01004B1200">44</option>
				<option value="32A23E01004B1200">32</option>
				<option value="7E9F3E01004B1200">7E</option>
				<option value="2A9F3E01004B1200">2A</option>
			</select>
		</div>
		<div id="choosedata">
			选择数据类型：<select id="datatype" data-inline="true">
				<option value="1">空气温度</option>
				<option value="2">空气湿度</option>
				<option value="3">光照强度</option>
				<option value="4">二氧化碳浓度</option>
				<option value="5">土壤温度</option>
				<option value="6">土壤湿度</option>
				<option value="7">工作电压</option>
			</select>
		</div>
		<div>
			<button data-inline="ture" onclick="addchart(false)">新建</button>
			<button data-inline="ture" onclick="addchart(true)">添加</button>
			<button data-inline="ture" onclick="cancle(1)">取消</button>
		</div>
	</fieldset>
	
	<fieldset id="loadchart" style="display: none;">
		<legend>加载图表</legend>
		<div id="chooseloadchart" data-role="fieldcontain">
			<table>
			<thead>
				<tr>
					<td>选择</td>
					<td>图表名称</td>
					<td>说明</td>
				</tr>
			</thead>
			<tbody id="loadcharttbody">
			
			</tbody>
			</table>
		</div>
		
		<div>
			<button data-inline="ture" onclick="loadchart()">加载</button>			
			<button data-inline="ture" onclick="cancle(2)">取消</button>
		</div>
	</fieldset>
	
	<fieldset id="savechart" style="display: none;">
		<legend>保存图表</legend>
		<div id="choosechart" data-role="fieldcontain">
			<table>
			<thead>
				<tr>
					<td>选择</td>
					<td>图表名称</td>
					<td>说明</td>
				</tr>
			</thead>
			<tbody id="savecharttbody">
			
			</tbody>
			</table>
		</div>
		
		<div>
			<button data-inline="ture" onclick="savechart()">保存</button>			
			<button data-inline="ture" onclick="cancle(3)">取消</button>
		</div>
	</fieldset>
	
	<fieldset id="deletechart" style="display: none;">
		<legend>删除图表</legend>
		<div id="choosedeletechart" data-role="fieldcontain">
			<table>
			<thead>
				<tr>
					<td>选择</td>
					<td>图表名称</td>
					<td>说明</td>
				</tr>
			</thead>
			<tbody id="deletecharttbody">
			
			</tbody>
			</table>
		</div>
		
		<div>
			<button data-inline="ture" onclick="deletechart()">删除</button>			
			<button data-inline="ture" onclick="cancle(4)">取消</button>
		</div>
	</fieldset>

	<div id="addnewchart"></div>

</body>
</html>
