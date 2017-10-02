/**
 * 
 */
var charts = {};
var points = {};
var types = {
	"空气温度" : 1,
	"空气湿度" : 2,
	"光照强度" : 3,
	"二氧化碳浓度" : 4,
	"土壤温度" : 5,
	"土壤湿度" : 6,
	"工作电压" : 7
};
var myChart = null;
/*
 * 主菜单跳转控制
 * **/
function edit(dom) {

	var id = $(dom).attr("id");

	if (id == "a1") {
		$("#newchart").attr("style", "display:inline;");
		$("#loadchart").attr("style", "display:none;");
		$("#savechart").attr("style", "display:none;");
		$("#deletechart").attr("style", "display:none;");
		$("#photos").attr("style", "display:none;");
	}
	if (id == "a2") {
		$("#newchart").attr("style", "display:none;");
		$("#loadchart").attr("style", "display:inline;");
		$("#savechart").attr("style", "display:none;");
		$("#deletechart").attr("style", "display:none;");
		$("#photos").attr("style", "display:none;");
		$("#loadcharttbody tr").remove();
		$.post("loadchart.action", {"platform":"web"},function(data) {
			if (data != null) {
				var json = eval('(' + data + ')');
				for (var i = 0; i < json.length; i++) {
					var chartname = json[i]["name"];
					points[chartname] = json[i]["point"];
					var datatype = json[i]["datatype"];
					var tr = $("<tr>");
					var td1 = $("<td><input type='checkbox' /></td>");
					var td2 = $("<td>");
					td2.text(chartname);
					var td3 = $("<td>");
					td3.text(datatype);

					tr.append(td1);
					tr.append(td2);
					tr.append(td3);
					$("#loadcharttbody").append(tr);

				}
			}
		});
	}
	if (id == "a3") {
		$("#newchart").attr("style", "display:none;");
		$("#loadchart").attr("style", "display:none;");
		$("#savechart").attr("style", "display:inline;");
		$("#deletechart").attr("style", "display:none;");
		$("#photos").attr("style", "display:none;");
		$("#savecharttbody tr").remove();
		for ( var w in charts) {
			var chart = charts[w];
			var tr = $("<tr>");
			var td1 = $("<td><input type='checkbox' id='" + w + "' /></td>");
			var td2 = $("<td>");
			td2.text(w);
			var td3 = $("<td>");
			td3.text(chart.getOption().legend.data);

			tr.append(td1);
			tr.append(td2);
			tr.append(td3);
			$("#savecharttbody").append(tr);
		}
	}
	if (id == "a4") {
		$("#newchart").attr("style", "display:none;");
		$("#loadchart").attr("style", "display:none;");
		$("#savechart").attr("style", "display:none;");
		$("#deletechart").attr("style", "display:inline;");
		$("#photos").attr("style", "display:none;");
		$("#deletecharttbody tr").remove();
		$.post("loadchart.action", function(data) {
			if (data != null) {
				var json = eval('(' + data + ')');
				for (var i = 0; i < json.length; i++) {
					var chartname = json[i]["name"];
					points[chartname] = json[i]["point"];
					var datatype = json[i]["datatype"];
					var tr = $("<tr>");
					var td1 = $("<td><input type='checkbox' /></td>");
					var td2 = $("<td>");
					td2.text(chartname);
					var td3 = $("<td>");
					td3.text(datatype);

					tr.append(td1);
					tr.append(td2);
					tr.append(td3);
					$("#deletecharttbody").append(tr);

				}
			}
		});
	}
	if(id == "a5"){
		$("#newchart").attr("style", "display:none;");
		$("#loadchart").attr("style", "display:none;");
		$("#savechart").attr("style", "display:none;");
		$("#deletechart").attr("style", "display:none;");
		$("#addnewchart").attr("style", "display:none;");
		$("#photos").attr("style", "display:inline;");
		connect();
	}else{
		$("#addnewchart").attr("style", "display:inline;");
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
/*
 * 隐藏窗口
 * **/
function cancle(flag) {
	switch (flag) {
	case 1:
		$("#newchart").attr("style", "display:none;");
		break;
	case 2:
		$("#loadchart").attr("style", "display:none;");
		break;
	case 3:
		$("#savechart").attr("style", "display:none;");
		break;
	case 4:
		$("#deletechart").attr("style", "display:none;");
		break;
	default:
		break;
	}

}
/*
 * 创建图表
 * **/
function addchart(flag) {
	var choosepoint = $("#pointtype :selected").text();
	var modletype = $("#modeltype").val();
	var pointtype = $("#pointtype").val();
	var datatype = $("#datatype").val();
	var legend = $("#datatype :selected").text();
	
	if(modletype!=""&&pointtype!=""&&datatype!=""){		
		if (flag) {
			oldchart(choosepoint,modletype, pointtype, datatype, legend);
		} else {
			if (charts[choosepoint] != null) {
				var num = Math.floor(Math.random() * 10000);
				choosepoint = choosepoint + "-" + num;
			}
			newchart(choosepoint,modletype,pointtype, datatype, legend);
		}
	}else{
		alert("请完成所有选项！");
	}
}
/*
 * 创建一个新的图表
 * **/
function newchart(chartname, modletype,pointtype, datatype, legend) {
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
		myChart = ec.init(newchart.get(0),'macarons');
		newchart.attr("id", chartname);

		// 基于准备好的dom，初始化echarts图表
		// 过渡---------------------
		myChart.showLoading({
			text : '正在努力的读取数据中...', // loading话术
		});
		var xdata = new Array();
		var ydata = new Array();
		var ajaxurl;
		if(modletype=="house"){
			ajaxurl = "history_point.action";
		}else{
			ajaxurl = "weather_history.action";
		}
		
		$.ajax({
			type : "post",
			url : ajaxurl,
			data : "point=" + pointtype + "&datatype=" + datatype,
			async : false,
			success : function(data) {
				if (data != "") {
					var datas = data.split("&");
					xdata = eval('(' + datas[1] + ')');
					ydata = eval('(' + datas[0] + ')');

					myChart.hideLoading();
				}
			}
		});

		var option = {
			title : {
				show : true,
				text : chartname.split("-")[0] + "节点数据",
				x : "center",
				y : "bottom",
				textStyle : {
					fontSize : 18,
					fontWeight : 'bolder',
					color : '#ff0000'
				}
			},
			tooltip : {// Option config. Can be overwrited by series or data
				trigger : 'item',
				show : true, // default true
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
				data : [ legend ]
			},
			xAxis : [ {
				type : 'category',
				data : xdata

			} ],
			yAxis : [ {
				type : 'value', // 坐标轴类型，纵轴默认为数值轴，类目轴则参考xAxis说明
				boundaryGap : [ 0.1, 0.1 ], // 坐标轴两端空白策略，数组内数值代表百分比
				splitNumber : 5, // 数值轴用，分割段数，默认为5
				scale : true
			}, ],
			axis : {

			},
			series : [ {
				name : legend, // 系列名称
				type : 'line', // 图表类型，折线图line、散点图scatter、柱状图bar、饼图pie、雷达图radar
				data : ydata
			} ],
		
		  color : [
		 
		 'rgba(255,0,0,1)', 'rgba(0,255,0,1)', 'rgba(0,0,255,1)', 'rgba(0,0,0,1)' ]
		 
		};
		// 为echarts对象加载数据
		myChart.setOption(option);
		charts[chartname] = myChart;
		//alert(charts[chartname]);
		points[chartname] = pointtype;
		//console.log(charts[chartname]);

	});
}
/*
 * 在现有图表上增加图表
 * **/
function oldchart(chartname, modletype, pointtype, datatype, legend) {
	$("#newchart").attr("style", "display:none;");
	if (charts[chartname] != null) {
		var oldmyChart = charts[chartname];

		oldmyChart.showLoading({
			text : '正在努力的读取数据中...', // loading话术
		});
		var ydata = new Array();
		// alert(pointtype);
		
		var ajaxurl;
		if(modletype=="house"){
			ajaxurl = "history_point.action";
		}else{
			ajaxurl = "weather_history.action";
		}
		$.ajax({
			type : "post",
			url : ajaxurl,
			data : "point=" + pointtype + "&datatype=" + datatype,
			async : false,
			success : function(data) {
				if (data != "") {
					var datas = data.split("&");
					xdata = eval('(' + datas[1] + ')');
					ydata = eval('(' + datas[0] + ')');

					oldmyChart.hideLoading();
					var option = oldmyChart.getOption();
					option.legend.data.push(legend);
					option.series.push({
						name : legend, // 系列名称
						type : 'line', // 图表类型，折线图line、散点图scatter、柱状图bar、饼图pie、雷达图radar
						data : ydata
					});
					oldmyChart.setOption(option);
					oldmyChart.refresh();
				}
			}
		});
	} else {

		newchart(chartname, pointtype, datatype, legend);
	}
}
/*
 * 加载图表
 * **/
function loadchart() {
	$("#loadchart").attr("style", "display:none;");
	var checkbox = $("#loadcharttbody :checked");

	for (var j = 0; j < checkbox.length; j++) {
		// var name = $(checkbox[i]).attr("id");
		var chartname = $(checkbox[j]).parent().next().text();

		var datatype = $(checkbox[j]).parent().next().next().text();
		// var title = chartname.split("-")[0];
		var legend = datatype.split(",");
		var pointtype = points[chartname];
		// var datatype = $("#datatype").val();
		/*newchart(chartname, pointtype, types[legend[0]], legend[0]);
		if (legend.length > 1) {
			for (var i = 1; i < legend.length; i++) {
				var datatype = types[legend[i]];
				if (charts[chartname] != null) {
					alert(charts[chartname]);
					oldchart(chartname, pointtype, datatype, legend[i]);
				}
			}
		}*/
		

		for (var i = 0; i < legend.length; i++) {
			var datatype = types[legend[i]];
			if (i == 0 && charts[chartname] == null) {
				if(points[chartname]=="0300000000000000"){
					newchart(chartname, "weather",pointtype, datatype, legend[i]);
				}else{
					newchart(chartname,"house", pointtype, datatype, legend[i]);
				}
				
			}else{
				break;
			}
			if (i > 0 && charts[chartname] != null) {
				//alert(charts[chartname]);
				if(points[chartname]=="0300000000000000"){
					oldchart(chartname, "weather",pointtype, datatype, legend[i]);
				}else{
					oldchart(chartname,"house", pointtype, datatype, legend[i]);
				}
			}
		}
		 
	}
}
/*
 * 保存图表
 * **/
function savechart() {
	$("#savechart").attr("style", "display:none;");
	var checkbox = $("#savecharttbody :checked");
	for (var i = 0; i < checkbox.length; i++) {
		// var name = $(checkbox[i]).attr("id");
		var chartname = $(checkbox[i]).parent().next().text();
		var datatype = $(checkbox[i]).parent().next().next().text();
		var point = points[chartname];
		var para = {
			"platform":"web",
			"chartname" : chartname,
			"point" : point,
			"datatype" : datatype
		};
		$.post("savechart.action", para, function(data) {

		});
	}
}
/*
 * 删除图表
 * **/
function deletechart() {
	$("#deletechart").attr("style", "display:none;");
	var checkbox = $("#deletecharttbody :checked");
	for (var i = 0; i < checkbox.length; i++) {
		// var name = $(checkbox[i]).attr("id");
		var chartname = $(checkbox[i]).parent().next().text();
		var para = {
			"chartname" : chartname
		};
		$.post("deletechart.action", para, function(data) {

		});
	}
}
/*
 * 数据显示表格的切换
 * **/
function modelChange(dom){
	var value = $(dom).context.value;
	//console.log($(dom));
	//console.log(value);
	if(value=="weather"){
		$("#weather_point").attr("style", "display:inline;");
		$("#weather_data").attr("style", "display:inline;");
		$("#house_point").attr("style", "display:none;");
		$("#house_data").attr("style", "display:none;");
		$("#weather_point :first-child").attr("selected","selected");
		$("#weather_data :first-child").attr("selected","selected");
		
	}
	if(value=="house"){
		$("#house_point").attr("style", "display:inline;");
		$("#house_data").attr("style", "display:inline;");
		$("#weather_point").attr("style", "display:none;");
		$("#weather_data").attr("style", "display:none;");
		$("#house_point :first-child").attr("selected","selected");
		$("#house_data :first-child").attr("selected","selected");
	}
}
/*
 * websocket加载图片
 * **/
var ws = null;
var ip = "";
$.ajax({  
    type : "post",  
    url : "hostip.action?time="+ new Date().getTime(), 
    async : false,  
    success : function(data){
   	 ip = data;
    }  
});
//var url = "ws://"+ip+":8080/server/imageSocket/photo";
var url = "ws://1510902v9s.iok.la:41809/server/imageSocket/photo";//外网IP
//判断当前浏览器是否支持WebSocket
var photos = $("#photos");
//connect();
function connect(){
	photos.empty();
if ('WebSocket' in window) {
    ws = new WebSocket(url);
} else if ('MozWebSocket' in window) {
    ws = new MozWebSocket(url);
} else {
    alert('WebSocket is not supported by this browser.');
    return;
}
	//建立连接成功的回调函数
	ws.onopen = function() {
		
	};
	//接收到消息的回调函数
	ws.onmessage = function(evt) {
		
		if(evt.data!=null){
			//console.log(evt.data);
			if(evt.data=="end"){
				ws.close();
			}else{				
				var photo = $("<img  width='320' height='240'>");
				photo.attr("src","data:image/png;base64,"+evt.data);
				photos.append(photo);
			}
		}
	};
	//连接发生错误的回调方法
	ws.onerror = function() {
		
	}; 
	//连接关闭的回调函数
	ws.onclose = function(evt) {
		
	};
	
}
//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function() {
	
	if(ws!=null){				
		ws.close();
	}
};

//关闭连接
function closeWebSocket() {		
	if(ws!=null){				
		ws.close();
	}
}