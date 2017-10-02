package com.server.other;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Encoder;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.struts2.ServletActionContext;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.FrameGrabber.Exception;

import com.opensymphony.xwork2.util.ResolverUtil.IsA;
import com.server.action.CameraVideoAction;

/*
 * 该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。
 * 类似Servlet的注解mapping。无需在web.xml中配置。
 */
@ServerEndpoint("/websocket/camera")
public class CameraSocket {

	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。

	private static int onlineCount = 0;

	private volatile boolean flag;
	private Thread thread;
	private String baseimage="";
	private CameraThread cameraThread;
	

	/*concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	 * 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	 */

	private static CopyOnWriteArrayList<CameraSocket> webSocketSet = new CopyOnWriteArrayList<CameraSocket>();
	

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	/**
	 * 
	 * 连接建立成功调用的方法
	 * 
	 * @param session
	 *  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	public CameraSocket(){
		
		cameraThread = CameraThread.getInstance();
		System.out.println("websocket"+this.hashCode());
		System.out.println("camera"+cameraThread.hashCode());
	}

	@OnOpen
	public void onOpen(Session session) throws Exception, IOException {
		this.session = session;
		webSocketSet.add(this);
		// 加入set中
		addOnlineCount(); // 在线数加1
		if(getOnlineCount()==1){			
		}
		if(!cameraThread.isFlag()){
			System.out.println("camera is opening...");			
			cameraThread.open();
		}

		System.out.print(this.session.getId()+"新连接加入!");
		System.out.println("当前在线人数为" + getOnlineCount());
		thread = new Thread(new Runnable() {		
			@Override
			public void run() {
				
				
			}
		});
		thread.start();
	}

	/**
	 * 
	 * 连接关闭调用的方法
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	@OnClose
	public void onClose() throws InterruptedException, IOException {
		
		//bao.close();
		webSocketSet.remove(this); // 从set中删除
		subOnlineCount(); // 在线数减1
		if(getOnlineCount()==0){
			
		}
		System.out.println(this.session.getId()+"连接关闭!当前在线人数为" + getOnlineCount());
	}

	/**
	 * 
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message
	 * 客户端发送过来的消息
	 * 
	 * @param session
	 * 可选的参数
	 * @throws InterruptedException 
	 */

	@OnMessage
	public void onMessage(String message, Session session) throws InterruptedException {
		this.session = session;
		System.out.println("来自客户端"+session.getId()+"的消息:" + message);
		// 群发消息
		if(message.equals("closecamera")){
			flag = false;
			thread.join();
			System.out.println("camera is closing...");
		}
		/*for (WebSocket item : webSocketSet) {
			try {
				item.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}*/
	}
	
	/**
	 * 
	 * 发生错误时调用
	 * 
	 * @param session
	 * 
	 * @param error
	 */

	@OnError
	public void onError(Session session, Throwable error) {
		this.session = session;
		//System.out.println("发生错误");
		//error.printStackTrace();
	}

	/**
	 * 
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * 
	 * @param message
	 * @return 
	 * 
	 * @throws IOException
	 */

	
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
		// this.session.getAsyncRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		CameraSocket.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		CameraSocket.onlineCount--;
	}
}
