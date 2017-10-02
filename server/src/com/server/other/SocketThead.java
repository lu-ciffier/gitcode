package com.server.other;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.server.jopo.SystemConfig;
import com.server.thread.PortDataListener;
import com.server.thread.HousePortThread;

public class SocketThead implements PortDataListener{
	private ServerSocket ss;
	private Socket s;
	private HousePortThread portThread;
	private String portData;
	private OutputStream out;
	private InputStream in;
	
	public SocketThead(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					portData = "";
					portThread = new HousePortThread();
					portThread.initHousePort("COM2", 9600);
					portThread.addPortDataListener(SocketThead.this);
					start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}).start();
	}
	public void start() throws IOException{
		ss = new ServerSocket(7777);
		// 用来处理客户端的请求
		while (true) {
			// 接受客户端socket请求,接受成功返回socket对象
			s = ss.accept();
			// 通过输入流,获取客户端的输出流
			in = s.getInputStream();
			out = s.getOutputStream();
			byte[] b = new byte[1024];
			int len = in.read(b);
			String data = new String(b, 0, len);
			System.out.println("从客户端传过来的数据:" + data);
			portThread.writePort(data);
			// 服务器处理数据,字母小写转大写并且返回
			
		}
	}
	@Override
	public void getPortData(String sb) {
		System.out.println("从串口传过来的数据:" + sb);
		this.portData = sb;
		if(portData!=""){				
			
			try {
				out.write(portData.getBytes());
				//s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			portData = "";
		}
	}
}
