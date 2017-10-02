package com.server.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lucyf
 * @version 2017.5.10
 * 电脑桌面APP服务器Socekt
 * **/
public class Server {

	/**
	 * 要处理客户端发来的动作，并返回一个动作，可实现该接口。
	 */
	public interface ObjectAction{
		Object doAction(Object rev);
	}
	
	public static final class DefaultObjectAction implements ObjectAction{
		public Object doAction(Object rev) {
			System.out.println("处理并返回："+rev);
			return rev;
		}
	}
	
	public static void main(String[] args) {
		int port = 7777;
		Server server = new Server();
		server.start();
	}
	
	private int port;
	private volatile boolean running=false;
	private long receiveTimeDelay=1000*60*2;
	private ConcurrentHashMap<Class, ObjectAction> actionMapping = new ConcurrentHashMap<Class,ObjectAction>();
	private Thread connWatchDog;
	
	public Server() {
		init();
	}
    public void init(){
    	this.port = 7777;
    }
	public void start(){
		if(running)return;
		running=true;
		connWatchDog = new Thread(new ConnWatchDog());
		connWatchDog.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stop(){
		if(running)running=false;
		if(connWatchDog!=null)connWatchDog.stop();
	}
	
	public void addActionMap(Class cls,ObjectAction action){
		actionMapping.put(cls, action);
	}
	
	class ConnWatchDog implements Runnable{
		public void run(){
			try {
				ServerSocket ss = new ServerSocket(7777,5);
				while(running){
					Socket s = ss.accept();
					new Thread(new SocketAction(s)).start();
				}
			} catch (IOException e) {
				e.printStackTrace();
				Server.this.stop();
			}
			
		}
	}
	/**
	 * 读取Socket客户端动作，并响应请求
	 * **/
	class SocketAction implements Runnable{
		Socket s;
		boolean run=true;
		long lastReceiveTime = System.currentTimeMillis();
		public SocketAction(Socket s) {
			this.s = s;
		}
		public void run() {
			while(running && run){
				if(System.currentTimeMillis()-lastReceiveTime>receiveTimeDelay){
					overThis();
				}else{
					try {
						InputStream in = s.getInputStream();
						if(in.available()>0){
							ObjectInputStream ois = new ObjectInputStream(in);
							Object obj = ois.readObject();
							lastReceiveTime = System.currentTimeMillis();
							System.out.println("接收：\t"+obj);
							ObjectAction oa = actionMapping.get(obj.getClass());
							oa = oa==null?new DefaultObjectAction():oa;
							Object out = oa.doAction(obj);
							if(out!=null){
								ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
								oos.writeObject(out);
								oos.flush();
							}
						}else{
							Thread.sleep(10);
						}
					} catch (Exception e) {
						e.printStackTrace();
						overThis();
					} 
				}
			}
		}
		/**
		 * 链接超时自动断开函数
		 * **/
		private void overThis() {
			if(run)run=false;
			if(s!=null){
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("关闭："+s.getRemoteSocketAddress());
		}
		
	}
	
}
