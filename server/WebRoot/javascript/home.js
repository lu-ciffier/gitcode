/**
 * 
 */

    var container;

    var camera, scene, renderer;

    var raycaster;
    var points = [];
    var mouse;
    var mtlLoader;
    var texture,onProgress,onError,loader;
    //var isworking = false;
    var workingstate = {};
    var quality = 0;

    var windowHalfX = window.innerWidth / 2;
    var windowHalfY = window.innerHeight / 2;
    //详细信息控件的尺寸
    var dboxwidth = $("#controls").width();
	var dboxheight = $("#controls").height();
	
    start();
    init();
    animate();
    
    var alertinfotimer;
    /**
     * 初始化各种变量
     * **/
    function init() {

        container = document.getElementById('container');
        document.body.appendChild( container );
        var info = document.getElementById('info');
        info.style.position = 'absolute';
        info.style.top = '10px';
        info.style.width = '100%';
        info.style.textAlign = 'center';
        info.innerHTML = '请点击温室里的模型进行操作！<br>'+
        	'使用鼠标可以对温室的视角进行改变,滚轮缩放,左键旋转，右键移动';

        container.appendChild( info );

        camera = new THREE.PerspectiveCamera( 45, 
        		window.innerWidth / window.innerHeight, 1, 2000 );

        camera.position.x = 10;
        camera.position.y = 100;
        camera.position.z = 100;


        controls = new THREE.TrackballControls( camera );

        controls.rotateSpeed = 5.0;
        controls.zoomSpeed = 5;
        controls.panSpeed = 2;

        controls.noZoom = false;
        controls.noPan = false;

        controls.staticMoving = true;
        controls.dynamicDampingFactor = 0.3;

        // scene

        scene = new THREE.Scene();

        var ambient = new THREE.AmbientLight( 0xffffff );
        scene.add( ambient );

        /*var directionalLight = new THREE.DirectionalLight( 0xffeedd );
        directionalLight.position.set( 0, 0, 1 );
        scene.add( directionalLight );*/

        // texture
        texture = new THREE.Texture();

        onProgress = function ( xhr ) {
            if ( xhr.lengthComputable ) {
                var percentComplete = xhr.loaded / xhr.total * 100;
                console.log( Math.round(percentComplete, 2) + '% downloaded' );
            }
        };

        onError = function ( xhr ) {
        };
        loader = new THREE.ImageLoader();
        /* loader.load( 'obj/green_grass.jpg', function ( image ) {

            texture.image = image;
            texture.needsUpdate = true;

        } ); */

        // model
        mtlLoader = new THREE.MTLLoader();
        mtlLoader.setBaseUrl( 'obj/' );
        mtlLoader.setPath( 'obj/' );

        loadhouse();

        //loadcamera(-20,21,12);
        $.post("position.action?time="+ new Date().getTime(), {
			"address" : "0100000000000000",
			"platform":"web"
		}, function(data) {
			if ($.trim(data) != "") {
				//alert(data);
				var cam = eval('(' + data + ')');
				loadcamera(cam["x"], cam["y"], cam["z"],
						"0100000000000000");				
			}
		});
        //loadweather(-30,0,-30,"0200000000000000");
        $.post("position.action?time="+ new Date().getTime(), {
			"address" : "0200000000000000",
			"platform":"web"
		}, function(data) {
			if ($.trim(data) != "") {
				//alert(data);
				var cam = eval('(' + data + ')');
				loadweather(cam["x"], cam["y"], cam["z"],
						"0200000000000000");				
			}
		});
        //topology();
        setTimeout(topology, 5000);
        
        setTimeout(getalerts, 2500);
        alertinfotimer = window.setInterval("getalerts()", 6 * 1000);

        //loadgateway(10,20,12);
        //x=(-60,70) y=(0,22) z=(-15,45)
        //loadpoint(-60,0,10);
        //loadpoint(70,0,10);
        //loadpoint(10,30,45);
        //loadpoint(10,0,-15);

        
        /* var geometry = new THREE.BoxGeometry( 4, 3, 2);
         var material = new THREE.MeshBasicMaterial( { color:0x257595,opacity: 0.5} );
         var point = new THREE.Mesh( geometry, material );
         point.position.set(10,22,0);
         scene.add(point);
        points.push(point); */

        raycaster = new THREE.Raycaster();
        mouse = new THREE.Vector2();
        renderer = new THREE.WebGLRenderer();
        renderer.setPixelRatio( window.devicePixelRatio );
        renderer.setSize( window.innerWidth, window.innerHeight );
        renderer.setClearColor(0xeeeeee, 1.0);
        container.appendChild( renderer.domElement );

        document.addEventListener( 'mousedown', onDocumentMouseDown, false );
        document.addEventListener( 'touchstart', onDocumentTouchStart, false );
        window.addEventListener( 'resize', onWindowResize, false );

    }
    /**
     * 窗口大小重置事件
     * **/
    function onWindowResize() {
        camera.aspect = window.innerWidth / window.innerHeight;
        camera.updateProjectionMatrix();
        renderer.setSize( window.innerWidth, window.innerHeight );
        controls.handleResize();
    }
    /**
     * 触摸事件
     * **/
    function onDocumentTouchStart( event ) {

        event.preventDefault();
        event.clientX = event.touches[0].clientX;
        event.clientY = event.touches[0].clientY;
        onDocumentMouseDown( event );

    }
    /**
     * 鼠标点击事件
     * **/
    function onDocumentMouseDown( event ) {
        event.preventDefault();
        mouse.x = ( event.clientX / renderer.domElement.clientWidth ) * 2 - 1;
        mouse.y = - ( event.clientY / renderer.domElement.clientHeight ) * 2 + 1;
        //console.log(event.clientX+":"+event.clientY);
        //console.log(window.innerWidth+":"+window.innerHeight);
        raycaster.setFromCamera( mouse, camera );
        var intersects = raycaster.intersectObjects( points );		
        if ( intersects.length > 0 ) {
            var data = intersects[ 0 ].object.position;
            var om = intersects[ 0 ].object.material;
            var color = om.color.getHex()+"";
            if(color=="16711680"){
            	$("#alertinfo").attr("style", "display:inline");
            }else{
            	$("#alertinfo").attr("style", "display:none");
            }
            //console.log(color);
            //intersects[ 0 ].object.name = "123456";
            
            console.log(points);
            $.post("address.action",{"xposition":data.x,"yposition":data.y,
            	"zposition":data.z}, function(data) {
    			if ($.trim(data) != "") {
    				//alert(data);
    				//console.log(data);
    				var d = eval('(' + data + ')');
    				console.log(d);
    				var type = d['pointnum'];
    				if(type==null){
    					type = d['pointNum'];
    				}
    				//设置节点模型的userData数据
    				intersects[0].object.userData = d;
    				console.log(intersects[0].object.userData.point);
    				var x=event.clientX,y=event.clientY;
    				//console.log(boxwidth+":"+boxheight);
    				if(type=="point"){
    					var boxwidth = $("#menue").width();
        				var boxheight = $("#menue").height();
    					$("#controls").attr("style","display: none");
    					$("#cameramenue").attr("style", "display:none");
    					$("#weatherinfo").attr("style", "display: none");
    					if(window.innerWidth-event.clientX<boxwidth){
    							x = event.clientX-boxwidth*1.2;
    					}
						if (window.innerHeight - event.clientY < boxheight) {
							y = event.clientY - boxheight*1.2;
						}
						$("#menue").attr("style","display:inline;top:" + y + "px;left:" + x	+ "px");
						$("#address").val(d['point']);
						$("#detail").attr("style", "display: inline");
						var atype = document.getElementById('type');
						atype.setAttribute('value', "传感器节点");
						var id = document.getElementById('id');
						id.setAttribute('value', d['point']);
						var work = document.getElementById('work');
						if (workingstate[d['point']]) {
							work.setAttribute('value', '正在工作...');
						} else {
							work.setAttribute('value', '停止工作...');
						}
						var atemp = document.getElementById('atemp');
						atemp.setAttribute('value', d['airT']+' ℃');
						var ahim = document.getElementById('ahim');
						ahim.setAttribute('value', d['airH']+' %');
						var stemp = document.getElementById('stemp');
						var st = d['soilT']+' ℃';
						if (st <= 0) {
							st = '不含该监测项';
						}
						stemp.setAttribute('value', st);
						var shim = document.getElementById('shim');
						var sh = d['soilH']+' %';
						if (sh <= 0) {
							sh = '不含该监测项';
						}
						shim.setAttribute('value', sh);
						var co2 = document.getElementById('co2');
						var c = d['co2']+' PPM';
						if (c <= 0) {
							c = '不含该监测项';
						}
						co2.setAttribute('value', c);
						var ill = document.getElementById('ill');
						var i = d['ill']+' LUX';
						if (i <= 0) {
							i = '不含该监测项';
						}
						ill.setAttribute('value', i);
						var vol = document.getElementById('vol');
						vol.setAttribute('value', d['voltage'].toFixed(2)+' V');
					}
					if (type == "gateway") {
						$("#controls").attr("style", "display: inline;top:" +(window.innerHeight-dboxheight)/2
								+ "px;left:" +(window.innerWidth-dboxwidth)/2 + "px");
						$("#menue").attr("style", "display:none");
						$("#cameramenue").attr("style", "display:none");
						$("#weatherinfo").attr("style", "display: none");
						$("#address").val(d['point']);
						$("#detail").attr("style", "display: none");
						var atype = document.getElementById('type');
						atype.setAttribute('value', "无线网关");
						var id = document.getElementById('id');
						id.setAttribute('value', d['point']);
						var work = document.getElementById('work');
						work.setAttribute('value', '正在工作...');

					}
					if (type == "camera") {
						var cboxwidth = $("#cameramenue").width();
	    				var cboxheight = $("#cameramenue").height();
						$("#controls").attr("style", "display: none");
						$("#menue").attr("style", "display:none");
						$("#weatherinfo").attr("style", "display: none");
						if(window.innerWidth - event.clientX < cboxwidth){
							x = event.clientX-cboxwidth*1.2;
						}
						if (window.innerHeight - event.clientY < cboxheight) {
							y = event.clientY - cboxheight*1.2;
						}
						$("#cameramenue").attr(
								"style","display:inline;top:" + y+ "px;left:" + x + "px");
						$("#address").val(d['point']);
						
						var atype = document.getElementById('type');
						atype.setAttribute('value', "摄像头");
						var id = document.getElementById('id');
						id.setAttribute('value', d['point']);
						var work = document.getElementById('work');
						work.setAttribute('value', '停止工作...');

					}
					if (type == "weather") {
						var wboxwidth = $("#weatherinfo").width();
        				var wboxheight = $("#weatherinfo").height();
        				
						$("#weatherinfo").attr("style", "display: inline;top:" +(window.innerHeight-wboxheight)/2
								+ "px;left:" +(window.innerWidth-wboxwidth)*1/4+ "px");
						$("#menue").attr("style", "display:none");
						$("#controls").attr("style", "display:none");
						$("#cameramenue").attr("style", "display:none");
						
						var atype = document.getElementById('wtype');
						atype.setAttribute('value', "气象站");
						var id = document.getElementById('wid');
						id.setAttribute('value', '0200000000000000');
						var work = document.getElementById('wwork');
						work.setAttribute('value', '正在工作...');
						
						var outtemp = document.getElementById('outtemp');
						outtemp.setAttribute('value', d['temperature'].toFixed(2)+' ℃');
						var outhum = document.getElementById('outhum');
						outhum.setAttribute('value', d['humidity']+' %');
						var winddir = document.getElementById('winddir');
						winddir.setAttribute('value', d['windDir'].toFixed(2)+' °');
						var windspeed = document.getElementById('windspeed');
						windspeed.setAttribute('value', d['windSpeed'].toFixed(2)+' KM/H');
						var solarradition = document.getElementById('solarradition');
						solarradition.setAttribute('value', d['solarRadiation'].toFixed(2)+' W/M2');
						var solarv = document.getElementById('solarv');
						solarv.setAttribute('value', d['solarV'].toFixed(2)+' V');
						var batteryv = document.getElementById('batteryv');
						batteryv.setAttribute('value', d['batteryV'].toFixed(2)+' V');
					}
				}
			});

		} else {
			//$("#controls").attr("style","display: none");
			//$("#menue").attr("style", "display:none");
			//$("#cameramenue").attr("style", "display:none");
		}

	}
    /**
     * 加载温室模型
     * **/
	function loadhouse() {
		mtlLoader.load('house.mtl', function(materials) {

			materials.preload();

			var objLoader = new THREE.OBJLoader();
			objLoader.setMaterials(materials);
			objLoader.setPath('obj/');
			objLoader.load('house.obj', function(object) {
				object.traverse(function(child) {

					if (child instanceof THREE.Mesh) {

						//child.material.map = texture;
					}
				});
				scene.add(object);
			}, onProgress, onError);
		});
	}

	/**
     * 加载摄像头模型
     * **/
	function loadcamera(x, y, z,name) {
		mtlLoader.load('camera.mtl', function(materials) {

			materials.preload();

			var objLoader = new THREE.OBJLoader();
			objLoader.setMaterials(materials);
			objLoader.setPath('obj/');
			objLoader.load('camera.obj', function(object) {
				object.traverse(function(child) {

					if (child instanceof THREE.Mesh) {
						
						child.position.set(x, y, z);
						child.name = name;
						child.material = new THREE.MeshPhongMaterial( {
							color: 0xff0000
						} );
						points.push(child);
					}
				});
				//object.position.set(-20,20,12);
				scene.add(object);
			}, onProgress, onError);
		});
	}
	/**
     * 加载气象站模型
     * **/
	function loadweather(x, y, z,name) {
		mtlLoader.load('weather.mtl', function(materials) {

			materials.preload();

			var objLoader = new THREE.OBJLoader();
			objLoader.setMaterials(materials);
			objLoader.setPath('obj/');
			objLoader.load('weather.obj', function(object) {
				object.traverse(function(child) {

					if (child instanceof THREE.Mesh) {
						//console.log(child);
						child.position.set(x, y, z);
						child.name = name;
						child.material = new THREE.MeshPhongMaterial( {
							color: 0x000000
						} );
						points.push(child);
					}
				});
				//object.position.set(-20,20,12);
				scene.add(object);
			}, onProgress, onError);
		});
	}
	/**
     * 加载网关模型
     * **/
	function loadgateway(x, y, z,name) {
		mtlLoader.load('gateway.mtl', function(materials) {

			materials.preload();

			var objLoader = new THREE.OBJLoader();
			objLoader.setMaterials(materials);
			objLoader.setPath('obj/');
			objLoader.load('gateway.obj', function(object) {
				object.traverse(function(child) {

					if (child instanceof THREE.Mesh) {
						child.position.set(x, y, z);
						child.name = name;
						child.material = new THREE.MeshPhongMaterial( {
							color: 0xffff00
						} );
						points.push(child);
						
					}
				});
				//object.position.set(10,20,12);
				scene.add(object);
			}, onProgress, onError);
		});
	}

	/**
     * 加载节点模型
     * （节点x坐标，节点y坐标，节点z坐标，父节点z坐标，父节点y坐标，父节点z坐标，节点地址）
     * **/
	function loadpoint(x, y, z, fx, fy, fz,name) {
		mtlLoader.load('point.mtl', function(materials) {

			materials.preload();

			var objLoader = new THREE.OBJLoader();
			objLoader.setMaterials(materials);
			objLoader.setPath('obj/');
			objLoader.load('point.obj', function(object) {
				object.traverse(function(child) {

					if (child instanceof THREE.Mesh) {
						child.position.set(x, y, z);
						child.name = name;
						points.push(child);
						/*child.material = new THREE.MeshPhongMaterial( {
							color: 0xff0000
						} );*/
					}
				});
				//object.position.set(x,y,z);
				scene.add(object);

			}, onProgress, onError);
		});
		drawline(x, y, z, fx, fy, fz);
	}

	/**
     * 绘制节点拓扑信号线
     * （源x，源y，源z坐标，目标x，目标y，目标z坐标）
     * **/
	function drawline(sx, sy, sz, dx, dy, dz) {
		//alert(quality);
		var geometry = new THREE.Geometry();
		var line;
		//节点的坐标需要进行调整（x-3,y+2,z-4）
		geometry.vertices.push(new THREE.Vector3(sx - 3, sy + 2, sz - 4));
		//网关的坐标需要进行调整（x-4,y+2,z）
		geometry.vertices.push(new THREE.Vector3(dx - 5, dy + 1, dz));
		if(quality>=100){			
		line = new THREE.Line(geometry, new THREE.LineBasicMaterial({
			color : 0x00ff00,
			linewidth : 3
		}));
		}else if(quality<50){
			line = new THREE.Line(geometry, new THREE.LineBasicMaterial({
				color : 0xff0000,
				linewidth : 3
			}));
		}else{
			line = new THREE.Line(geometry, new THREE.LineBasicMaterial({
				color : 0x0000ff,
				linewidth : 3
			}));
		}
		scene.add(line);
	}
	/**
     * 自动刷新
     * **/
	function animate() {
		requestAnimationFrame(animate);
		render();
	}
	/**
     * 3D渲染
     * **/
	function render() {
		renderer.render(scene, camera);
		controls.update();
	}
	/**
     *打开节点拓扑
     * **/
	function start() {
		$.post("open.action", {platform:"web"},function(data) {
			if ($.trim(data) != "") {
				//alert("ok");				
			}
		});

	}
	/**
     * 获取节点拓扑
     * **/
	var d = null;
	function topology() {
		var gatep = null;
		$.post("topology.action?time="+ new Date().getTime(),{platform:"web"}, function(data) {
			if ($.trim(data) != "") {
				//alert(data);
				console.log(data);
				d = eval('(' + data + ')');
				var root = d["root"];
				$.post("position.action?time="+ new Date().getTime(), {
					"address" : root.substring(0, 16),
					"platform":"web"
				}, function(data) {
					if ($.trim(data) != "") {
						//alert(data);
						gatep = eval('(' + data + ')');
						loadgateway(gatep["x"], gatep["y"], gatep["z"],
								root.substring(0, 16));
						var Child = d[root.substring(0, 16)];
						drawpoint(gatep["x"], gatep["y"], gatep["z"], Child);
					}
				});
			}
		});
	}
	/**
     * 绘制节点模型
     * （x坐标，y坐标，z坐标，下级节点）
     * **/
	function drawpoint(x, y, z, father) {
		
		var childs = father.split(",");
		var pointp = null;
		for (var i = 0; i < childs.length; i++) {
			var child = childs[i];
			quality = child.substring(16);
			workingstate[child.substring(0, 16)] = false;
			
			$.ajax({  
		         type : "post",  
		         url : "position.action?time="+ new Date().getTime(),  
		         data : "address=" + child.substring(0, 16)+"&platform=web",  
		         async : false,
		         success : function(data){
		        	 //alert(child.substring(0, 16));
		        	 pointp = eval("(" + data + ")");  
		        	 loadpoint(pointp["x"], pointp["y"], pointp["z"], x, y, z,
		        			 child.substring(0, 16));
						var hasChild = d[child.substring(0, 16)];
						if (hasChild != null) {
							//alert(hasChild);
							drawpoint(pointp["x"] + 2, pointp["y"] + 1,
									pointp["z"] - 4, hasChild);
						}
		         }  
		     });
		}		
	}
	/**
     * 节点选项菜单
     * **/
	function menue(command) {
		$("#menue").attr("style", "display:none");
		var add = $("#address").val();
		if (command == "singlecollect") {
			var para = {
				"data" : "26525341" + add
						+ "000000000000000000000000000000000000002A"
			};
			$.post("begin.action", para, function(data) {
				//isworking = true;
			});
			workingstate[add] = true;

			return;
		}
		if (command == "allcollect") {		
			$.post("stop.action", {"data":"264153413030303030303030303030303030303030303030303030303030302A"}, 
					function(data) {	
			});
			for ( var w in workingstate) {
				workingstate[w] = true;
			}
			return;
		}
		if (command == "singlepuase") {
			var para = {
				"data" : "26545341" + add
						+ "000000000000000000000000000000000000002A"
			};
			$.post("stop.action", para, function(data) {
				//isworking = false;	
			});
			workingstate[add] = false;
			//alert(command);
			return;
		}
		if (command == "allpuase") {
			$.post("stop.action", {"data":"264153543030303030303030303030303030303030303030303030303030302A"}, 
					function(data) {	
			});
			for ( var w in workingstate) {
				workingstate[w] = false;
			}
			return;
		}
		if (command == "alertinfo") {
			//alert(add+":"+workingstate[add]);
			$("#alerts").attr("style", "display: inline;top:" +(window.innerHeight-dboxheight)*3/4
					+ "px;left:" +(window.innerWidth-dboxwidth)/2 + "px");
			return;
		}
		if (command == "detailinfo") {
			//alert(add+":"+workingstate[add]);
			$("#controls").attr("style", "display: inline;top:" +(window.innerHeight-dboxheight)/2
					+ "px;left:" +(window.innerWidth-dboxwidth)*3/4 + "px");
			return;
		}
		if (command == "closemenue") {

			return;
		}

	}
	/**
     * 摄像头选项菜单
     * **/
	function cameramenue(para) {
		$("#cameramenue").attr("style", "display:none");
		if (para == "startphoto") {
			connect();
			$("#cameravideo").attr("style", "display: inline;top:" +(window.innerHeight-dboxheight)/2
					+ "px;left:" +(window.innerWidth-dboxwidth)*1/4 + "px");
			return;
		}
		if (para == "closephoto") {
			$("#cameravideo").attr("style", "display:none");
			closeWebSocket();
			return;
		}
		if (para == "detailinfo") {

			$("#controls").attr("style", "display: inline;top:" +(window.innerHeight-dboxheight)/2
					+ "px;left:" +(window.innerWidth-dboxwidth)/2 + "px");
			$("#detail").attr("style", "display: none");
			return;
		}
		if (para == "closemenue") {

			return;
		}
	}
	/**
     * 摄像头指令菜单
     * **/
	function cameraoption(flag){
		switch (flag) {
		case 1:
			ws.send("takephoto");
			break;
		case 2:
			ws.send("takevideo");
			break;
		case 3:
			ws.send("retakevideo");
			break;
		case 4:
			ws.send("savevideo");
			break;
		default:
			break;
		}
	}
	function closecontrol() {
		$("#controls").attr("style", "display: none");
		$("#weatherinfo").attr("style", "display: none");
	}
	/**
     * 获取警报
     * **/
	function getalerts(){
		$.post("getalertinfo.action?time="+ new Date().getTime(),function(data) {
			if(data!=null&&data!=""){
				var alerts = eval('(' + data + ')');
				//console.log(points);
				for(var i=0;i<alerts.length;i++){
					var point = alerts[i]["point"]+"";
					for(var j=0;j<points.length;j++){
						var pointsname = points[j].name+"";
						//console.log(pointsname);
						if(point==pointsname){						
							points[j].material.color.setHex( 0xff0000 );
							var alerttext = $("#alertdetailinfo").text();
							$("#alertdetailinfo").text(alerttext+alerts[i]["reason"]);
							$("#alertpoint").val(point);
						}
					}
				}
			}
		});
	}
	/**
     * 查看警报
     * **/
	function conformalert(){
		$("#alertdetailinfo").text("");
		$("#alerts").attr("style", "display: none;");
		var point = $("#alertpoint").val();
		for(var j=0;j<points.length;j++){
			var pointsname = points[j].name+"";
			if(point==pointsname){						
				//alert("ok");
				points[j].material.color.setHex( 0x068706 );
				$.post("deletealertinfo.action",{"point":point}, function(data) {	
				});
				break;
			}
		}
	}
	
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
	var url = "ws://"+ip+":8080/server/cameraSocket/camera";
	//var url = "ws://1510902v9s.iok.la:41809/server/cameraSocket/camera";	
	//判断当前浏览器是否支持WebSocket
	/**
     * 打开socket连接
     * **/
	function connect(){
		
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
				$("#img").attr("src","data:image/png;base64,"+evt.data);
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
		//console.log("li kai");
		window.clearInterval(alertinfotimer);
		if(ws!=null){
			
			ws.send("closecamera");
			ws.close();
		}
	};
	
	//关闭连接
	function closeWebSocket() {
		ws.send("closecamera");
		ws.close();
		$("#img").attr("src","data:image/png;base64,");
	}
	// take photo
	function takephoto(){
		ws.send("takephoto");
	}