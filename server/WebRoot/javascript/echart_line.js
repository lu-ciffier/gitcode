//jsonArray图形数据数组 reportName图形显示标题 chartId图形初始化的区域id
function initEchartsBar(json, reportName,chartId) {
	require.config({
		paths : {
			// 这里是你引用的echarts文件的路径
			echarts : 'js/echarts',
			// 这里需要注意的是除了mapchart使用的配置文件为echarts-map之外，
			//其他的图形引用的配置文件都为echarts，这也是一般的图形跟地图的区别
			'echarts/chart/bar' : 'js/echarts',
			'echarts/chart/line' : 'js/echarts',
			'echarts/chart/pie' : 'js/echarts'
		}
	});
	require([ 'echarts',// 这里定义项目中需要的类
	'echarts/chart/line', ],
	function(ec) {
		// 图表初始化的地方，页面中要有一个地方来显示图表的ID
		var myChart = ec.init(document.getElementById(chartId));
		option = getOptionByArray(json, reportName);// 得到option图形
		myChart.setOption(option); // 显示图形
	});
}
function getOptionByArray(json, reportName) {
	var option = {
		title : {
			text : reportName,
			x : 'center'
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a}<br/>{b} : {c} ({d}%)"
		},
		legend : {
			orient : 'vertical',
			x : 'left',
			data : json.getText
		},
		toolbox : {
			show : true,
			feature : {
				mark : {
					show : true
				},
				dataView : {
					show : true,
					readOnly : false
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
		calculable : true,
		series : [ {
			name : json.getText,
			type : 'line',
			radius : '55%',
			center : [ '50%', '60%' ],
			data : json.getValue
		} ]
	};
	return option;
}
