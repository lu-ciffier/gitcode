$.mobile.ajaxEnabled = false;
$(document).ready(function() {
	//自动加载服务器所有可用串口列表
	$("#port option:ge(0)").remove();
	$.post("portlist.action", {"platform":"web"},function(data) {
		if ($.trim(data) != "") {

			var $ports = ($.trim(data)).split(",");
			console.log($ports);
			for (var i = 0; i < $ports.length; i++) {
				if ($ports[i] != " ") {
					var $option = $("<option>" + $ports[i] + "</option>");
					$("#port").append($option);
				}
			}
		}
	});
});
/*
 * 主菜单切换
 * **/
function edit(dom) {

	var id = $(dom).attr("id");

	if (id == "a1") {
		$("#portconfig").attr("style", "display:inline;");
		$("#positionconfig").attr("style", "display:none;");
		$("#alertconfig").attr("style", "display:none;");
		$("#userconfig").attr("style", "display:none;");
		$("#deletealert").attr("style", "display:none;");
	}
	if (id == "a2") {
		$("#portconfig").attr("style", "display:none;");
		$("#positionconfig").attr("style", "display:inline;");
		$("#alertconfig").attr("style", "display:none;");
		$("#userconfig").attr("style", "display:none;");
		$("#deletealert").attr("style", "display:none;");
	}
	if (id == "a3") {
		$("#portconfig").attr("style", "display:none;");
		$("#positionconfig").attr("style", "display:none;");
		$("#alertconfig").attr("style", "display:inline;");
		$("#userconfig").attr("style", "display:none;");
		$("#deletealert").attr("style", "display:none;");

	}
	if (id == "a4") {
		$("#portconfig").attr("style", "display:none;");
		$("#positionconfig").attr("style", "display:none;");
		$("#alertconfig").attr("style", "display:none;");
		$("#userconfig").attr("style", "display:inline;");
		$("#deletealert").attr("style", "display:none;");
		userinfo();
	}

}
/*
 * 用户信息
 * **/
function userinfo() {

	$.post("${pageContext.request.contextPath}/userinfo.action?time="
			+ new Date().getTime(), {platform:"web"},function(data) {
		if (data != "") {
			var userinfo = data.split(",");
			$("#username").val("用户ID：" + userinfo[0]).button("refresh");
			$("#password").val("用户密码：" + userinfo[1]).button("refresh");
			$("#registdate").val("注册时间：" + userinfo[2]).button("refresh");
			if (userinfo[0] == "admin") {
				$("#level").val("用户级别：管理员").button("refresh");
			} else {
				$("#level").val("用户级别：普通用户").button("refresh");
			}
		}
	});
}
/*
 * 串口设置保存
 * **/
function save() {
	var type = $("#type").val();
	var port = $("#port").val();
	var botelv = $("#botelv").val();
	var para = {
		"type" : type,
		"port" : port,
		"botelv" : botelv,
		"platform":"web"
	};
	$.post("config.action", para, function(data) {
		top.location.href = "main.jsp";
	});
}
/*
 * 核对警报名称
 * **/
function checkalert() {
	var name = $("#alertname").val();
	if (name == "") {
		alert("请输入警报名称!");
		return;
	}
	$.post("checkalert.action", {
		"name" : name
	}, function(data) {
		if (data != "") {
			alert(data);
			$("#alertname").val(null);
		}
	});
}
/*
 * 警报保存
 * **/
function savealert() {
	var name = $("#alertname").val();
	var pointtype = $("#pointtype").val();
	var datatype = $("#datatype :selected").text();
	var min = $("#min").val();
	var max = $("#max").val();
	var para = {
		"name" : name,
		"point" : pointtype,
		"datatype" : datatype,
		"min" : min,
		"max" : max,
		"platform":"web"
	};
	// alert(para["min"]);
	$.post("savealert.action", para, function(data) {
		top.location.href = "main.jsp";
	});
}
/*
 * 加载警报
 * **/
function configalert() {
	$("#alertconfig").attr("style", "display:none;");
	$("#deletealert").attr("style", "display:inline;");
	$("#deletealerttbody tr").remove();
	$.post("loadalert.action", {platform:"web"},function(data) {
		if (data != null) {
			var json = eval('(' + data + ')');
			for (var i = 0; i < json.length; i++) {
				var alertname = json[i]["name"];
				var info = json[i]["datatype"] + "," + json[i]["min"] + ","
						+ json[i]["max"] + "," + json[i]["point"];
				var tr = $("<tr>");
				var td1 = $("<td><input type='checkbox' /></td>");
				var td2 = $("<td>");
				td2.text(alertname);
				var td3 = $("<td>");
				td3.text(info);

				tr.append(td1);
				tr.append(td2);
				tr.append(td3);
				$("#deletealerttbody").append(tr);

			}
		}
	});
}
/*
 * 删除警报
 * **/
function deletealert() {
	$("#deletealert").attr("style", "display:none;");
	var checkbox = $("#deletealerttbody :checked");
	for (var i = 0; i < checkbox.length; i++) {
		// var name = $(checkbox[i]).attr("id");
		var alertname = $(checkbox[i]).parent().next().text();
		var para = {
			"name" : alertname,
			"platform":"web"
		};
		$.post("deletealert.action", para, function(data) {

		});
	}
}
/*
 * 3D坐标配置
 * **/
function insert() {
	var name = $("#name").val();
	var ad = $("#address").val();
	var xp = $("#xposition").val();
	var yp = $("#yposition").val();
	var zp = $("#zposition").val();
	var para = {
		"name" : name,
		"address" : ad,
		"xposition" : xp,
		"yposition" : yp,
		"zposition" : zp,
		"platform":"web"
	};
	$.post("device.action", para, function(data) {
		alert(data);
		$("#name").val("");
		$("#address").val("");
		$("#xposition").val("");
		$("#yposition").val("");
		$("#zposition").val("");
	});
}