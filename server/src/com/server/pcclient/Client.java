package com.server.pcclient;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author lucyf
 * @version 2017.5.10
 * 电脑APP客户端Socket
 * **/
public class Client {

	/**
	 * 处理服务端发回的对象，可实现该接口。
	 */
	public static interface ObjectAction{
		void doAction(Object obj,Client client);
	}
	public static final class DefaultObjectAction implements ObjectAction{
		public void doAction(Object obj,Client client) {
			//client.responce = obj;
			System.out.println("处理：\t"+obj.toString());
		}
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		String serverIp = "127.0.0.1";
		int port = 7777;
		Client client = new Client(serverIp,port);
		client.start();
	}
	
	private String serverIp;
	private int port;
	private Socket socket;
	private boolean running=false;
	private long lastSendTime;
	private String responce;
	private ConcurrentHashMap<Class, ObjectAction> actionMapping 
			= new ConcurrentHashMap<Class,ObjectAction>();
	public String getResponce() {
		String s = responce;
		responce = "";
		return s;
	}
	public Client(String serverIp, int port) {
		this.serverIp=serverIp;
		this.port=port;
	}
	/**
	 * 实例化Socket
	 * **/
	public void start() throws UnknownHostException, IOException {
		if(running)return;
		socket = new Socket(serverIp,port);
		System.out.println("本地端口："+socket.getLocalPort());
		lastSendTime=System.currentTimeMillis();
		running=true;
		responce = new String();
		//new Thread(new KeepAliveWatchDog()).start();
		//sendObject(new String("admin:admin"));
		
		new Thread(new ReceiveWatchDog()).start();
	}
	
	public void stop(){
		if(running)running=false;
	}
	
	/**
	 * 添加接收对象的处理对象。
	 * @param cls 待处理的动作，其所属的类。
	 * @param action 待处理动作。
	 */
	public void addActionMap(Class cls,ObjectAction action){
		actionMapping.put(cls, action);
	}
	/**
	 * 发送请求动作
	 * **/
	public void sendObject(Object obj) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(obj);
		System.out.println("发送：\t"+obj);
		oos.flush();
	}
	/**
	 * 自动登录服务器
	 * **/
	class KeepAliveWatchDog implements Runnable{
		long checkDelay = 10;
		long keepAliveDelay = 2000;
		public void run() {
			while(running){
				if(System.currentTimeMillis()-lastSendTime>keepAliveDelay){
					try {
						Client.this.sendObject(new String("admin:admin"));
					} catch (IOException e) {
						e.printStackTrace();
						Client.this.stop();
					}
					lastSendTime = System.currentTimeMillis();
				}else{
					try {
						Thread.sleep(checkDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
						Client.this.stop();
					}
				}
			}
		}
	}
	/**
	 * 监听服务器端响应动作
	 * **/
	class ReceiveWatchDog implements Runnable{
		public void run() {
			while(running){
				try {
					InputStream in = socket.getInputStream();
					if(in.available()>0){
						ObjectInputStream ois = new ObjectInputStream(in);
						Object obj = ois.readObject();
						responce = obj.toString();
						System.out.println("接收：\t"+responce);
						ObjectAction oa = actionMapping.get(obj.getClass());
						oa = oa==null?new DefaultObjectAction():oa;
						oa.doAction(obj, Client.this);
					}else{
						Thread.sleep(10);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Client.this.stop();
				} 
			}
		}
	}
	
}

