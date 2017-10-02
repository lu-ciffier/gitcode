<%@ page language="java" import="java.util.*,com.server.action.CameraVideoAction,
org.bytedeco.javacv.Java2DFrameConverter,org.bytedeco.javacv.FrameGrabber,
java.awt.image.BufferedImage,javax.imageio.ImageIO" 
pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    
    <title>My JSP 'camera.jsp' starting page</title>
    <script type="text/javascript" src="javascript/jquery-1.8.2.js"></script>

  </head>
  
  <body>
  <div>
    	<button onclick="opendev()">打开摄像头</button>
    	<button onclick="readbaseimage()">image</button>
    	<button onclick="closedev()">关闭摄像头</button>
  </div>
  <%-- 
  <%out.clear();out = pageContext.pushBody();%>
  <%!Java2DFrameConverter cFrameConverter = new Java2DFrameConverter();
 	 FrameGrabber grabber;
     BufferedImage image;%>
     <script type="text/javascript">
     	function opendev(){
     		<%  
     	    response.setContentType("image/png");
     	    response.setHeader("Pragma","no-cache");
     	    response.setHeader("Cache-Control","no-cache");
     	    ImageIO.setUseCache(false);
     	    new Thread(new Runnable() {		
     			@Override
     			public void run() {
     								
     			}
     		}).start();
     	    try {
     			grabber = FrameGrabber.createDefault(0);
     			grabber.start();
     			while (true) {
     				image = cFrameConverter.convert(grabber.grab());
     				if (image != null) {
     					ImageIO.write(image,"PNG",response.getOutputStream());
     					response.getOutputStream().flush();
     					image.flush();
     					
     				}
     			}
     		} catch (Exception e) {
     			e.printStackTrace();
     		}
     	    
     	    %>
     	}
     	function closedev(){
     		<%
     			//grabber.stop();    		
     		%>
     	}
     </script>   --%>   
    <img id="img" alt="image" src="" width="320" height="240">
    <script type="text/javascript">
    var interval;
    	function opendev(){
    		$.post("opendev.action", function(data) {			
			});
    		
    		//setTimeout(readbaseimage(), 3000);
    		interval = setInterval(readbaseimage, 1000/30);
    	}
    	function readbaseimage(){
    		$.post("baseimage.action?time="
					+ new Date().getTime(), function(data) {
    			if(data!=null){
    				$("#img").attr("src","data:image/png;base64,"+data);
    				//alert(data);
    			}
			});	
    	}
    	function closedev(){
    		clearInterval(interval);
    		$.post("closedev.action", function(data) {			
			});
    	}
    </script>
  </body>
</html>
