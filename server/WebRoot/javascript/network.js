var realdatatimer;
var networktimer;
/*
 * 主菜单切换控制
 * **/
function edit(dom) {

	var id = $(dom).attr("id");

	if (id == "a1") {
		$("#networkstate").attr("style", "display:inline;");
		$("#realdata").attr("style", "display:none;");
		start1();
	}
	if (id == "a2") {
		$("#networkstate").attr("style", "display:none;");
		$("#realdata").attr("style", "display:inline;");
		start2();
	}
}
/*
 * 拓扑状态诊断
 * **/
function start1() {
	state();
	networktimer = window.setInterval("state()", 60 * 1000);
}
/*
 * 数据包诊断
 * **/
function start2() {
	realdatatimer = window.setInterval("read()", 1000);
}
/*
 * 更新拓扑状态
 * **/
function state() {
	
	$.post("open.action?time=" + new Date().getTime(),{platform:"web"}, function(data) {
		if (data != "") {
			window.setTimeout("gettopo()", 5000);
		}
	});

}
/*
 * 获取拓扑状态
 * **/
function gettopo(){
	var datajson = null;
	var row = 0;
	$.post("topology.action?time=" + new Date().getTime(),{platform:"web"}, function(
			data) {
		if (data != "") {
			datajson = eval('(' + data + ')');
			$("#networkbody tr").remove();
		    //console.log(datajson);
			var root = datajson["root"];
			var tr = $("<tr>");
			row++;
			var td1 = $("<td>");
			td1.text(root);
			var td2 = $("<td>");
			td2.text(root);
			var td3 = $("<td>");
			td3.text();
			tr.append(td1);
			tr.append(td2);
			tr.append(td3);
			if(row%2==0){					
				tr.attr("style","background-color: #5FE9FC;");
			}
			$("#networkbody").append(tr);
			var child = datajson[root.substring(0, 16)];
			getpoint(root, child);
		}

		function getpoint(father, child) {

			var points = child.split(",");
			for (var i = 0; i < points.length; i++) {
				var point = points[i].substring(0, 16);
				var quality = points[i].substring(16);
				var ptr = $("<tr>");
				row++;
				var ptd1 = $("<td>");
				ptd1.text(point);
				var ptd2 = $("<td>");
				ptd2.text(father);
				var ptd3 = $("<td>");
				ptd3.text(quality);
				ptr.append(ptd1);
				ptr.append(ptd2);
				ptr.append(ptd3);
				if(row%2==0){				
					ptr.attr("style","background-color: #5FE9FC;");
				}
				$("#networkbody").append(ptr);
				var hasChild = datajson[point];
				if (hasChild != null) {
					getpoint(point, hasChild);
				}
			}
		}
	});
}
/*
 * 获取实时数据包
 * **/
var row = 0;
function read() {
	$.post("realdata.action?time=" + new Date().getTime(), {platform:"web"},function(data) {
		if (data != "") {
			var datajson = eval('(' + data + ')');
			for (var i = 0; i < datajson.length; i++) {
				var date = datajson[i]["date"];
				var point = datajson[i]["point"];
				
				// console.log();
				var tr = $("<tr>");
				row++;
				var td1 = $("<td>");
				td1.text(date);
				var td2 = $("<td>");
				td2.text(point);
				var td3 = $("<td>");
				var td4 = $("<td>");
				var td5 = $("<td>");
				var td6 = $("<td>");
				var td7 = $("<td>");
				var td8 = $("<td>");
				var td9 = $("<td>");
				td3.text(datajson[i]["air_tem"]);
				td4.text(datajson[i]["air_hum"]);
				td5.text(datajson[i]["ill"]);
				td6.text(datajson[i]["co2"]);
				td7.text(datajson[i]["soil_tem"]);
				td8.text(datajson[i]["soil_hum"]);
				td9.text(datajson[i]["voltage"]);

				tr.append(td1);
				tr.append(td2);
				tr.append(td3);
				tr.append(td4);
				tr.append(td5);
				tr.append(td6);
				tr.append(td7);
				tr.append(td8);
				tr.append(td9);
				if(row%2==0){				
					tr.attr("style","background-color: #5FE9FC;");
				}
				$("#realdatatbody").append(tr);

			}
		}
	});
}
window.onbeforeunload = function() {
	
	window.clearInterval(realdatatimer);
	window.clearInterval(networktimer);
	
};