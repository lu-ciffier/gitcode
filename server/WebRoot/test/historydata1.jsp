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
<script type="text/javascript" src="javascript/jquery.multi-select.js"></script>
<link rel="stylesheet" href="css/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" href="css/multi-select.css">
<link rel="stylesheet" type="text/css" href="css/historydata.css">
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
</style>
<script type="text/javascript">
	var size = 0;
	var page = 0;
	function time() {
		$("#tresult tr:gt(0)").remove();
		var year_from = $("#year_from").val();
		var month_from = $("#month_from").val();
		var day_from = $("#day_from").val();
		var year_to = $("#year_to").val();
		var month_to = $("#month_to").val();
		var day_to = $("#day_to").val();
		var para = {
			"date_from" : year_from + "-" + month_from + "-" + day_from,
			"date_to" : year_to + "-" + month_to + "-" + day_to
		};
		$.post("${pageContext.request.contextPath}/history_time.action?time="
				+ new Date().getTime(), para, function(data) {
			if (data != "") {

				drawtable(data, "tresult", false);
				size = Math.ceil($("#tresult tr").length / 10) - 1;
				page = 0;
				$("#page").html((page + 1) + "/" + (size + 1));
				$("#tresult tr").each(function(i) {
					if (i <= 10 * page + 10) {
						$(this).removeAttr("hidden"); //只显示10行
					}
				});
				$("#pages").removeAttr("hidden");
				setTimeout(function() {
					$("#chart").removeAttr("hidden");
					drawchart("tresult", true);
				}, 1000);
			}
		});
	}
	function point() {
		$("#tresult tr:gt(0)").remove();
		var point = $("#pointid").val();
		$.post("${pageContext.request.contextPath}/history_point.action?time="
				+ new Date().getTime(), {
			"pointnum" : point
		}, function(data) {
			if (data != "") {

				drawtable(data, "tresult", false);
				size = Math.ceil($("#tresult tr").length / 10) - 1;
				page = 0;
				$("#page").html((page + 1) + "/" + (size + 1));
				$("#tresult tr").each(function(i) {
					if (i <= 10 * page + 10) {
						$(this).removeAttr("hidden"); //只显示10行
					}
				});
				$("#pages").removeAttr("hidden");
				setTimeout(function() {
					$("#chart").removeAttr("hidden");
					drawchart("tresult", true);
				}, 1000);
			}
		});

	}
	function selectAll() {
		$("#tresult tr:gt(0)").remove();
		$.post("${pageContext.request.contextPath}/history_all.action?time="
				+ new Date().getTime(), function(data) {
			if (data != "") {

				drawtable(data, "tresult", false);
				size = Math.ceil($("#tresult tr").length / 10) - 1;
				page = 0;
				$("#page").html((page + 1) + "/" + (size + 1));
				$("#tresult tr").each(function(i) {
					if (i <= 10 * page + 10) {
						$(this).removeAttr("hidden"); //只显示10行
					}
				});
				$("#pages").removeAttr("hidden");
				setTimeout(function() {
					$("#chart").removeAttr("hidden");
					drawchart("tresult", true);
				}, 1000);
			}
		});
	}
	function getpage(id) {

		var text = $(id).html();
		if (text == "首页") {
			page = 0;
		} else if (text == "下一页") {
			if (page < size) {
				page = page + 1;
			}
		} else if (text == "上一页") {
			if (page > 0) {
				page = page - 1;
			}
		} else if (text == "尾页") {
			page = size;
		}
		$("#page").html((page + 1) + "/" + (size + 1));
		$("#tresult tr").each(function(i) {
			if (i > page * 10 && i <= page * 10 + 10) {
				$(this).removeAttr("hidden"); //只显示10行
			} else {
				if (i > 0) {
					$(this).attr("hidden", "hidden");
				}
			}
		});
	}
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
				<option value="73">73</option>
				<option value="44">44</option>
				<option value="32">32</option>
				<option value="7E">7E</option>
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
			<button data-inline="ture" onclick="addchart()">新建</button>
			<button data-inline="ture" onclick="cancle()">取消</button>
		</div>
	</fieldset>
	<table id="tmenu" data-role="none">
		<thead>
			<tr>
				<th><h1>条件</h1></th>
				<th><h1>指令</h1></th>
			</tr>
		</thead>
		<tr>
			<td><select id="year_from" data-inline="true">
					<option>2015</option>
					<option>2016</option>
			</select>年 <select id="month_from" data-inline="true">
					<option>1</option>
					<option>2</option>
					<option>3</option>
					<option>4</option>
					<option>5</option>
					<option>6</option>
					<option>7</option>
					<option>8</option>
					<option>9</option>
					<option>10</option>
					<option>11</option>
					<option>12</option>
			</select>月 <select id="day_from" data-inline="true">
					<option>1</option>
					<option>2</option>
					<option>3</option>
					<option>4</option>
					<option>5</option>
					<option>6</option>
					<option>1</option>
					<option>1</option>
					<option>1</option>
					<option>7</option>
					<option>8</option>
					<option>9</option>
					<option>10</option>
					<option>11</option>
					<option>12</option>
					<option>13</option>
					<option>14</option>
					<option>15</option>
					<option>16</option>
					<option>17</option>
					<option>18</option>
					<option>19</option>
					<option>20</option>
					<option>21</option>
					<option>22</option>
					<option>23</option>
					<option>24</option>
					<option>25</option>
					<option>26</option>
					<option>27</option>
					<option>28</option>
					<option>29</option>
					<option>30</option>
			</select>日 ——<select id="year_to" data-inline="true">
					<option>2015</option>
					<option>2016</option>
			</select>年 <select id="month_to" data-inline="true">
					<option>1</option>
					<option>2</option>
					<option>3</option>
					<option>4</option>
					<option>5</option>
					<option>6</option>
					<option>7</option>
					<option>8</option>
					<option>9</option>
					<option>10</option>
					<option>11</option>
					<option>12</option>
			</select>月 <select id="day_to" data-inline="true">
					<option>1</option>
					<option>2</option>
					<option>3</option>
					<option>4</option>
					<option>5</option>
					<option>6</option>
					<option>1</option>
					<option>1</option>
					<option>1</option>
					<option>7</option>
					<option>8</option>
					<option>9</option>
					<option>10</option>
					<option>11</option>
					<option>12</option>
					<option>13</option>
					<option>14</option>
					<option>15</option>
					<option>16</option>
					<option>17</option>
					<option>18</option>
					<option>19</option>
					<option>20</option>
					<option>21</option>
					<option>22</option>
					<option>23</option>
					<option>24</option>
					<option>25</option>
					<option>26</option>
					<option>27</option>
					<option>28</option>
					<option>29</option>
					<option>30</option>
			</select>日
			<td><input type="button" id="time" value="按时间查看"
				onclick="time()" data-inline="true" /></td>
		</tr>
		<tr>
			<td><select id="pointid" data-inline="true">
					<option>73</option>
					<option>44</option>
					<option>32</option>
					<option>7E</option>
			</select></td>
			<td><input type="button" id="point" value="按节点查看"
				onclick="point()" data-inline="true" /></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="button" id="all" value="全部查看"
				onclick="selectAll()" data-inline="true" /></td>
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
	<div id="addnewchart"></div>
	<table id="tresult" style="display:none" data-role="table"
		data-mode="columntoggle">
		<caption class="table-title">已经查询到的历史数据</caption>
		<thead>
			<tr>
				<th>节点</th>
				<th>空温</th>
				<th>空湿</th>
				<th>光强</th>
				<th>Co2</th>
				<th>土温</th>
				<th>土湿</th>
				<th>电压</th>
				<th>时间</th>
			</tr>
		</thead>
		<tbody id="tbody">
		</tbody>
	</table>
	<div id="pages" hidden="hidden">
		<button id="first" data-inline="true" onclick="getpage(this)">首页</button>
		<button id="add" data-inline="true" onclick="getpage(this)">下一页</button>
		<button id="page" data-inline="true" disabled="disabled">0/0</button>
		<button id="sub" data-inline="true" onclick="getpage(this)">上一页</button>
		<button id="last" data-inline="true" onclick="getpage(this)">尾页</button>
	</div>
	<script type="text/javascript">
		function edit(dom) {

			var id = $(dom).attr("id");

			if (id == "a1") {
				$("#newchart").attr("style", "display:inline;");

			}
			if (id == "a2") {

			}
			if (id == "a3") {

			}
			if (id == "a4") {

			}

		}
		function ms() {
			$("#pointtype").multiSelect();
			var saveb = $("<button onclick='msd(this)'>save</button>");
			$("#choosepoint").append(saveb);
		}
		function msd(dom) {
			$("#pointtype").multiSelect("destroy");
			$(dom).remove();
		}
		function cancle() {
			$("#newchart").attr("style", "display:none;");
		}
		function addchart() {
			$("#newchart").attr("style", "display:none;");
			var newchart = $("<div style='height:400px'></div>");
			$("#addnewchart").append(newchart);
			require.config({
				paths : {
					"echarts" : "./echarts/"
				}
			});
			require([ 'echarts', 'echarts/chart/line', 'echarts/chart/bar'
			// 使用柱状图就加载bar模块，按需加载
			], function(ec) {
				// 基于准备好的dom，初始化echarts图表
				var myChart = ec.init(newchart.get(0));
				// 过渡---------------------
				myChart.showLoading({
					text : '正在努力的读取数据中...', //loading话术				
				});				
				var pointtype = $("#pointtype").val();
				var datatype = $("#datatype").val();
				
				$.ajax({  
			         type : "post",  
			         url : "history_point.action",  
			         data : "pointnum=" + pointtype+"&datatype="+datatype,  
			         async : false,  
			         success : function(data){
			        	 if (data != "") {
							alert(data);
							myChart.hideLoading();
						} 
			         }  
			     });
				
				var option = {
					tooltip : {// Option config. Can be overwrited by series or data
						trigger : 'item',
						show : true, //default true						
					},
					toolbox : {
						show : true,
						feature : {
							mark : {
								show : true,
								color : [ '#ffffff' ]
							},
							magicType : {
								show : true,
								color : [ '#d2691e' ],
								type : [ 'line', 'bar', 'stack', 'tiled' ]
							},
							dataView : {
								show : true,
								readOnly : false,
								color : [ '#22bb22' ]
							},
							restore : {
								show : true,
								color : [ '#22bb22' ]
							},
							saveAsImage : {
								show : true,
								color : [ '#22bb22' ]
							}
						}
					},
					legend : {
						padding : 5, // 图例内边距，单位px，默认上下左右内边距为5
						itemGap : 10, // Legend各个item之间的间隔，横向布局时为水平间隔，纵向布局时为纵向间隔
						data : [ 'ios', 'android' ]
					},
					xAxis : [ {
						type : 'category',
						data : [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
								'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ]

					} ],
					yAxis : [ {
						type : 'value', // 坐标轴类型，纵轴默认为数值轴，类目轴则参考xAxis说明
						boundaryGap : [ 0.1, 0.1 ], // 坐标轴两端空白策略，数组内数值代表百分比
						splitNumber : 5
					// 数值轴用，分割段数，默认为5

					}, ],
					axis : {
						scale : true
					},
					series : [
							{
								name : 'ios', // 系列名称
								type : 'line', // 图表类型，折线图line、散点图scatter、柱状图bar、饼图pie、雷达图radar
								data : [ 112, 23, 45, 56, 233, 343, 454, 89,
										343, 123, 45, 123 ]
							},
							{
								name : 'android', // 系列名称
								type : 'line', // 图表类型，折线图line、散点图scatter、柱状图bar、饼图pie、雷达图radar
								data : [ 45, 123, 145, 526, 233, 343, 44, 829,
										33, 123, 45, 13 ]
							} ],
				/* color : [

				'rgba(255,0,0,1)' ] */
				};
				// 为echarts对象加载数据 
				myChart.setOption(option);
				// 增加些数据------------------
				option.legend.data.push('win');
				option.series.push({
					name : 'win', // 系列名称
					type : 'line', // 图表类型，折线图line、散点图scatter、柱状图bar、饼图pie、雷达图radar
					data : [ 112, 23, 45, 56, 233, 343, 454, 89, 343, 123, 45,
							123 ]
				});
				myChart.setOption(option);
			});
		}
	</script>
</body>
</html>
