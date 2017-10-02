package com.server.thread;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;






import org.bytedeco.javacv.FrameGrabber.Exception;

import sun.misc.BASE64Encoder;


/**
 * @author lucyf
 * @version 2017.5.10
 * 图片加载Socket
 * **/
/*
 * 该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。
 * 类似Servlet的注解mapping。无需在web.xml中配置。
 */
@ServerEndpoint("/imageSocket/photo")
public class ImageSocket {

	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。

	private static int onlineCount = 0;

	private InputStream inputStream;
  
	private static CopyOnWriteArrayList<ImageSocket> imageSocketSet = new CopyOnWriteArrayList<ImageSocket>();

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	
	public ImageSocket(){
			
		System.out.println("imageSocket:"+this.hashCode());		
	}

	@OnOpen
	public void onOpen(Session session) throws Exception, IOException {
		this.session = session;
		imageSocketSet.add(this);
		// 加入set中
		addOnlineCount(); // 在线数加1
		loadPhoto();
		System.out.print(this.session.getId()+"新连接加入!");
		System.out.println("当前在线人数为" + getOnlineCount());
	}

	/**
	 * 
	 * 连接关闭调用的方法
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	@OnClose
	public void onClose() throws InterruptedException, IOException {
		
		imageSocketSet.remove(this); // 从set中删除
		subOnlineCount(); // 在线数减1
		System.out.println(this.session.getId()+"连接关闭!当前在线人数为" + getOnlineCount());
	}


	@OnMessage
	public void onMessage(String message, Session session) throws 
	InterruptedException, org.bytedeco.javacv.FrameRecorder.Exception, 
	IOException {
		this.session = session;
		System.out.println("来自客户端"+session.getId()+"的消息:" + message);
		// 群发消息
		
	}

	@OnError
	public void onError(Session session, Throwable error) {
		this.session = session;
	}
	/**
	 * 加载图片
	 * **/
	public void loadPhoto() throws IOException{
		byte[] data = null;
		String base64;
		BASE64Encoder encoder = new BASE64Encoder();
		File file = new File("E:/server/photos");
		File[] photos = file.listFiles();
		for(int i=0;i<photos.length;i++){
			File photo = photos[i];
			
			String photoname = photo.getName();
			System.out.println(photoname);
			if(photoname.endsWith(".png")){
				inputStream = new FileInputStream(photo);				
				data = new byte[inputStream.available()];
				inputStream.read(data);
				inputStream.close();
				base64 = encoder.encode(data);
				this.sendMessage(base64);
			}
		}
		this.sendMessage("end");
	}
	
	
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
		// this.session.getAsyncRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		ImageSocket.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		ImageSocket.onlineCount--;
	}
}
