/**
 * tableId:作为数据来源的table Id
 * cover:是否覆盖已有的数据
 * **/
var tim = new Array();
var air_tem = new Array();
var air_hum = new Array();
var ill = new Array();
var co2 = new Array();
var soil_tem = new Array();
var soil_hum = new Array();
var vol = new Array();
function drawchart(tableId,cover) {
	var pointnum = new Array();
	if(cover){
		tim = new Array();
		air_tem = new Array();
		air_hum = new Array();
		ill = new Array();
		co2 = new Array();
		soil_tem = new Array();
		soil_hum = new Array();
		vol = new Array();
	}
	$("#"+tableId+" tr:gt(0)").each(function() {
		//读取节点
		if ($(this).find("td").eq(0).html() != "") {
			pointnum.push($(this).find("td").eq(0).html());
		}
		//读取空气温度
		if ($(this).find("td").eq(1).html() != "") {
			air_tem.push($(this).find("td").eq(1).html());
		}
		//读取空气湿度
		if ($(this).find("td").eq(2).html() != "") {
			air_hum.push($(this).find("td").eq(2).html());
		}
		//读取光照强度
		if ($(this).find("td").eq(3).html() != null) {
			ill.push($(this).find("td").eq(3).html());
		}
		//读取二氧化碳浓度
		if ($(this).find("td").eq(4).html() != null) {
			co2.push($(this).find("td").eq(4).html());
		}
		//读取土壤温度
		if ($(this).find("td").eq(5).html() != null) {
			soil_tem.push($(this).find("td").eq(5).html());
		}
		//读取土壤湿度
		if ($(this).find("td").eq(6).html() != null) {
			soil_hum.push($(this).find("td").eq(6).html());
		}
		//读取电压
		if ($(this).find("td").eq(7).html() != "") {
			vol.push($(this).find("td").eq(7).html());
		}
		//读取时间
		if ($(this).find("td").eq(8).html() != null) {
			tim.push($(this).find("td").eq(8).html());
		}
		//alert($(this).find("td").eq(1).html());
	});
	if(pointnum.length>0){
		var d = pointnum[0];
		for(var i=0;i<pointnum.length;i++){
			if(d==pointnum[i]){
				d = pointnum[i];
			}else{				
				pointnum[0] = "all";
				break;
			}
		}
		pointnum[0] = pointnum[0]+"节点:";
	}
	air_temshow(tim, air_tem, pointnum);
	air_humshow(tim, air_hum, pointnum);
	illshow(tim, ill, pointnum);
	co2show(tim, co2, pointnum);
	soil_temshow(tim, soil_tem, pointnum);
	soil_humshow(tim, soil_hum, pointnum);
	volshow(tim, vol, pointnum);
}
function illshow(xdata, ydata, pointnum) {
	require.config({
		paths : {
			"echarts" : "./echarts/"
		}
	});
	require([ 'echarts', 'echarts/chart/line', 'echarts/chart/bar'
	// 使用柱状图就加载bar模块，按需加载
	], function(ec) {
		// 基于准备好的dom，初始化echarts图表
		var myChart = ec.init($("#ill").get(0));

		var option = {
			tooltip : {// Option config. Can be overwrited by series or data
				show : true, //default true
			/* trigger: 'axis',
			showDelay: 0,
			hideDelay: 50,
			transitionDuration:0,
			backgroundColor : 'rgba(255,255,255,0.7)',
			borderColor : '#f50',
			borderRadius : 8,
			borderWidth: 2,
			padding: 10,    // [5, 10, 15, 20]
			position : function(p) {
			        // 位置回调
			        // console.log && console.log(p);
				return [p[0] + 10, p[1] - 10];
			}, */
			/* 						textStyle : {
			 color: 'yellow',
			 decoration: 'none',
			 fontFamily: 'Verdana, sans-serif',
			 fontSize: 15,
			 fontStyle: 'italic',
			 fontWeight: 'bold'
			 } */
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
				data : [ pointnum[0] + "光照强度" ]
			},
			xAxis : [ {
				type : 'category',
				data : xdata
			//data : [ "衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子" ]
			} ],
			yAxis : [ {
				type : 'value'
			}, {
				type : 'value'
			} ],
			axis : {
				scale:true
			},
			series : [ {
				"name" : pointnum[0] + "光照强度",
				"type" : "line",
				"data" : ydata
			//"data" : [ 5, 20, 40, 10, 10, 20 ]
			} ],
			color : [

			'rgba(255,0,0,1)' ]
		};
		// 为echarts对象加载数据 
		myChart.setOption(option);
	});
}
function air_temshow(xdata, ydata, pointnum) {
	require.config({
		paths : {
			"echarts" : "./echarts/"
		}
	});
	require([ 'echarts', 'echarts/chart/line', 'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	], function(ec) {
		// 基于准备好的dom，初始化echarts图表
		var myChart = ec.init($("#air_tem").get(0));

		var option = {
			tooltip : {
				show : true
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
				data : [ pointnum[0] + "空气温度" ]
			},
			xAxis : [ {
				type : 'category',
				data : xdata
			//data : [ "衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子" ]
			} ],
			yAxis : [ {
				type : 'value'
			}, {
				type : 'value'
			} ],
			series : [ {
				"name" : pointnum[0] + "空气温度",
				"type" : "line",
				"data" : ydata
			//"data" : [ 5, 20, 40, 10, 10, 20 ]
			} ],
			color : [

			'rgba(0,255,0,1)' ]
		};
		// 为echarts对象加载数据 
		myChart.setOption(option);
	});
}
function air_humshow(xdata, ydata, pointnum) {
	require.config({
		paths : {
			"echarts" : "./echarts/"
		}
	});
	require([ 'echarts', 'echarts/chart/line', 'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	], function(ec) {
		var myChart = ec.init($("#air_hum").get(0));
		var option = {
			tooltip : {
				show : true
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
				data : [ pointnum[0] + "空气湿度" ]
			},
			xAxis : [ {
				type : 'category',
				data : xdata
			} ],
			yAxis : [ {
				type : 'value'
			}, {
				type : 'value'
			} ],
			series : [ {
				"name" : pointnum[0] + "空气湿度",
				"type" : "line",
				"data" : ydata
			} ],
			color : [

			'rgba(0,0,255,1)' ]
		};
		myChart.setOption(option);
	});
}
function co2show(xdata, ydata, pointnum) {
	require.config({
		paths : {
			"echarts" : "./echarts/"
		}
	});
	require([ 'echarts', 'echarts/chart/line', 'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	], function(ec) {
		var myChart = ec.init($("#co2").get(0));
		var option = {
			tooltip : {
				show : true
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
				data : [ pointnum[0] + " :CO2" ]
			},
			xAxis : [ {
				type : 'category',
				data : xdata
			} ],
			yAxis : [ {
				type : 'value'
			}, {
				type : 'value'
			} ],
			series : [ {
				"name" : pointnum[0] + "CO2",
				"type" : "line",
				"data" : ydata
			} ],
			color : [

			'rgba(0,0,255,1)' ]
		};
		myChart.setOption(option);
	});
}
function soil_temshow(xdata, ydata, pointnum) {
	require.config({
		paths : {
			"echarts" : "./echarts/"
		}
	});
	require([ 'echarts', 'echarts/chart/line', 'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	], function(ec) {
		var myChart = ec.init($("#soil_tem").get(0));
		var option = {
			tooltip : {
				show : true
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
				data : [ pointnum[0] + "土壤温度" ]
			},
			xAxis : [ {
				type : 'category',
				data : xdata
			} ],
			yAxis : [ {
				type : 'value'
			}, {
				type : 'value'
			} ],
			series : [ {
				"name" : pointnum[0] + "土壤温度",
				"type" : "line",
				"data" : ydata
			} ],
			color : [

			'rgba(0,0,255,1)' ]
		};
		myChart.setOption(option);
	});
}
function soil_humshow(xdata, ydata, pointnum) {
	require.config({
		paths : {
			"echarts" : "./echarts/"
		}
	});
	require([ 'echarts', 'echarts/chart/line', 'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	], function(ec) {
		var myChart = ec.init($("#soil_hum").get(0));
		var option = {
			tooltip : {
				show : true
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
				data : [ pointnum[0] + "土壤湿度" ]
			},
			xAxis : [ {
				type : 'category',
				data : xdata
			} ],
			yAxis : [ {
				type : 'value'
			}, {
				type : 'value'
			} ],
			series : [ {
				"name" : pointnum[0] + "土壤湿度",
				"type" : "line",
				"data" : ydata
			} ],
			color : [

			'rgba(0,0,255,1)' ]
		};
		myChart.setOption(option);
	});
}
function volshow(xdata, ydata, pointnum) {
	require.config({
		paths : {
			"echarts" : "./echarts/"
		}
	});
	require([ 'echarts', 'echarts/chart/line', 'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	], function(ec) {
		// 基于准备好的dom，初始化echarts图表
		var myChart = ec.init($("#vol").get(0));

		var option = {
			tooltip : {
				show : true
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
				data : [ pointnum[0] + "电压" ]
			},
			xAxis : [ {
				type : 'category',
				data : xdata
			//data : [ "衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子" ]
			} ],
			yAxis : [ {
				type : 'value'
			}, {
				type : 'value'
			} ],
			series : [ {
				"name" : pointnum[0] + "电压",
				"type" : "line",
				"data" : ydata
			//"data" : [ 5, 20, 40, 10, 10, 20 ]
			} ],
			color : [

			'rgba(255,100,100,1)' ]
		};

		// 为echarts对象加载数据 
		myChart.setOption(option);
	});
}
/**
 * json:数据json
 * tableId:需要绘制的table Id
 * cover:是否覆盖已有节点的数据
 * **/

function drawtable(json,tableId,cover) {
	var d = eval('(' + json + ')');
	for (var i = 0; i < d.length; i++) {
		if (d[i]["ill"] != 0) {
			var $tr;
			if(cover){			
				var id = d[i]["pointnum"];
				$tr = $("#" + id);
				//判断该节点所对应的行对象是否存在，存在就覆盖，不存在新建
				if ($tr.length <= 0) {
					$tr = $("<tr id='" + id + "'>");
				} else {
					$tr.empty();
				}
			}else{
				$tr = $("<tr hidden='hidden'>");
			}
			
			//给该节点对应行添加节点数据
			var $td1 = $("<td align='center'>" + d[i]["pointnum"] + "</td>");
			var $td2 = $("<td align='center'>" + d[i]["air_tem"] + "</td>");
			var $td3 = $("<td align='center'>" + d[i]["air_hum"] + "</td>");
			var $td4 = $("<td align='center'>" + d[i]["ill"] + "</td>");
			var $td5 = $("<td align='center'>" + d[i]["co2"] + "</td>");
			var $td6 = $("<td align='center'>" + d[i]["soil_tem"] + "</td>");
			var $td7 = $("<td align='center'>" + d[i]["soil_hum"] + "</td>");
			var $td8 = $("<td align='center'>" + d[i]["voltage"] + "</td>");
			var $td9 = $("<td align='center'>" + d[i]["date"]["hours"] + ":"
					+ d[i]["date"]["minutes"] + "</td>");
			$tr.append($td1);
			$tr.append($td2);
			$tr.append($td3);
			$tr.append($td4);
			$tr.append($td5);
			$tr.append($td6);
			$tr.append($td7);
			$tr.append($td8);
			$tr.append($td9);
			$("#"+tableId).removeAttr("style");
			$("#"+tableId).append($tr);
			//$("#"+tableId).basictable();
		}
	}
}
function drawtopology() {
	var canvasWidth = $("#canvas").attr("width");
	var canvasHeight = $("#canvas").attr("height");
	var canvas = document.getElementById("canvas");
	var context = canvas.getContext("2d");
	var height = 80;
	var boxWidth = 40;
	var boxHeight = 20;
	var d = null;
	$.post("topology.action", function(data) {
		if ($.trim(data) != "") {
			alert(data);
			d = eval('(' + data + ')');
			var root = d["root"];
			drawRoot(context, (canvasWidth - boxWidth) / 2, height, boxWidth,
					boxHeight, root.substring(0, 2));
			var Child = d[root];
			drawNodes(context, Child, 1, 0, canvasWidth - boxWidth);

			drawSimple(context);
		}
	});
	function drawRoot(context, x, y, width, height, text) {
		context.fillStyle = "#FFD700";
		context.fillRect(x - width / 2, y - height / 2, width, height);
		context.fillStyle = "rgb(0,0,0)";
		context.fillText(text, x - 11, y + 3);
	}
	/**
	 * 绘制子节点矩形 context:得到画布画笔 father:父节点的地址 level:子节点的等级 fleft:父节点的左边距
	 * width:父节点的的宽度大小
	 */
	function drawNodes(context, father, level, fleft, width) {
		var Childs = father.split(",");
		var length = Childs.length;
		var size = width / (length + 1);
		var left = fleft + size / 2;
		for (var i = 0; i < length; i++) {
			var child = Childs[i];
			// 子节点中心的x坐标 = 左边距 + 子节点宽度的一半
			// 子节点中心的y坐标 = 初始高度 + 固定高度*子节点等级
			var quality = child.substring(16);
			node(context, left + size / 2, height + 70 * level, boxWidth,
					boxHeight, child.substring(0, 2));
			// 开始x坐标 = 父节点中心x坐标
			// 开始y坐标 = 父节点中心y坐标 + 矩形高度的一半
			// 结束x坐标 = 子节点的中心坐标
			// 结束y坐标 = 子节点中心y坐标 - 矩形高度的一半
			drawLine(context, fleft + width / 2, height + 70 * (level - 1)
					+ boxHeight / 2, left + size / 2, height + 70 * level
					- boxHeight / 2, quality);
			var hasChild = d[child.substring(0, 16)];
			if (hasChild != null) {
				drawNodes(context, hasChild, level + 1, left, size);
			}
			left = left + size;
		}
	}
	/**
	 * 绘制矩形框 context:得到画布画笔 x:矩形中心x坐标 y:矩形中心y坐标 width:矩形的宽度 height:矩形的高度
	 * text:矩形显示的文本
	 */
	function node(context, x, y, width, height, text) {
		context.fillStyle = "#CDB38B";
		context.fillRect(x - width / 2, y - height / 2, width, height);
		context.fillStyle = "rgb(0,0,0)";
		context.fillText(text, x - 10, y + 3);
	}
	function drawSimple(context) {
		context.fillStyle = "#FFD700";
		context.fillRect(canvasWidth - boxWidth * 3, canvasHeight - boxHeight
				* 4, boxWidth, boxHeight);
		context.fillStyle = "rgb(0,0,0)";
		context.fillText("网关", canvasWidth - boxWidth * 1.5, canvasHeight
				- boxHeight * 3.3);
		context.fillStyle = "#CDB38B";
		context.fillRect(canvasWidth - boxWidth * 3, canvasHeight - boxHeight
				* 2.5, boxWidth, boxHeight);
		context.fillStyle = "rgb(0,0,0)";
		context.fillText("节点", canvasWidth - boxWidth * 1.5, canvasHeight
				- boxHeight * 1.8);
	}
	/**
	 * 绘制直线 context:得到画布画笔 beginX:直线开始x坐标 beginY:直线开始y坐标 endX:直线结束x坐标
	 * endY:直线结束y坐标
	 */
	function drawLine(context, beginX, beginY, endX, endY, quality) {
		context.fillStyle = "rgb(0,0,0)";
		context.fillText(quality, (endX + beginX) / 2, (endY + beginY) / 2);
		if (quality >= 100) {
			// 通信质量最好为绿色
			context.strokeStyle = "rgb(0,255,0)";
		} else if (quality >= 50 && quality < 100) {
			//通信质量良好为蓝色
			context.strokeStyle = "rgb(0,0,255)";
		} else {
			//通信质量一般为红色
			context.strokeStyle = "rgb(255,0,0)";
		}
		context.beginPath();
		context.moveTo(beginX, beginY);
		context.lineTo(endX, endY);
		context.stroke();
	}
}