package com.server.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


import java.util.List;

import javax.comm.CommPortIdentifier;

import com.server.action.ConfigAction;
import com.server.action.HistoryParameterAction;
import com.server.action.RealDataAction;
import com.server.action.UserAction;
/**
 * @author lucyf
 * @version 2017.5.10
 * 电脑桌面APP Socket实例，服务器启动时运行
 * **/
public class AppSocket extends Server implements PortDataListener {
	private UserAction userAction = null;
	private HousePortThread housePortThread = null;
	private WeatherPortThread weatherPortThread = null;
	private RealDataAction realDataAction = null;
	private HistoryParameterAction historyParameterAction = null;
	private ConfigAction configAction = null;
	private ServerSocket ss;
	private Socket s;
	private String portData;
	private OutputStream out;
	private InputStream in;

	public void setUserAction(UserAction userAction) {
		this.userAction = userAction;		
	}
	public void setHousePortThread(HousePortThread housePortThread) {
		this.housePortThread = housePortThread;
	}
	public void setWeatherPortThread(WeatherPortThread weatherPortThread) {
		this.weatherPortThread = weatherPortThread;
	}
	public void setRealDataAction(RealDataAction realDataAction) {
		this.realDataAction = realDataAction;
		realDataAction.init();
	}
	public void setHistoryParameterAction(
			HistoryParameterAction historyParameterAction) {
		this.historyParameterAction = historyParameterAction;
	}
	public void setConfigAction(ConfigAction configAction) {
		this.configAction = configAction;
	}
	public AppSocket() {
		//运行socket服务器
		super();
		init();
		this.start();
	}
	public void init(){
		addActionMap(HashMap.class,new HashmapAction());
		//realDataAction.init();
	}
	/**
	 * 实例化Socket
	 * **/
	public void Socketstart() throws Exception {
		ss = new ServerSocket(7777);
		// 用来处理客户端的请求
		while (true) {
			// 接受客户端socket请求,接受成功返回socket对象
			s = ss.accept();
			// 通过输入流,获取客户端的输出流
			in = s.getInputStream();
			out = s.getOutputStream();
			/*byte[] b = new byte[1024];
			int len = in.read(b);
			String data = new String(b, 0, len);
			System.out.println("从客户端传过来的数据:" + data);*/
			//login();
			//portThread.writePort(data);
			// 服务器处理数据,字母小写转大写并且返回
			if(in.available()>0){			
				//onReceiveFromClient();
				ObjectInputStream ois = new ObjectInputStream(in);
				ObjectOutputStream oos = new ObjectOutputStream(out);
				Object o = ois.readObject();
				System.out.println("接收到:"+o);
				//String ins = new String("接收到:"+o);
				oos.writeObject(ois);
				oos.flush();
			}
		}
	}
	/**
	 * 监听温室实时数据函数
	 * @param sb 监听的数据
	 * **/
	@Override
	public void getPortData(String sb) {
		System.out.println("从串口传过来的数据:" + sb);
		this.portData = sb;
		if (portData != "") {

			try {
				out.write(portData.getBytes());
				// s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			portData = "";
		}
	}
	/**
	 * 接受客户端数据并返回
	 * **/
	public String onReceiveFromClient() throws Exception{
		byte[] b = new byte[1024];
		int len = 0;
		String result = "";
		len = in.read(b);
		if(len>0){
			result = new String(b, 0, len);
			System.out.println("从客户端传过来的数据:" + result);		
		}
		return result;
	}
	/**
	 * 请求动作解析类
	 * **/
	public final class HashmapAction implements ObjectAction {

		@Override
		public Object doAction(Object rev) {		
			HashMap<String, String> map = (HashMap<String, String>) rev;
			//登录动作
			if(map.containsKey("login")){
				String login = map.get("login");
				try {
					return login(login);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//获取串口信息动作
			if(map.containsKey("config")){
				//第一个是当前的串口,后面是可用的串口
				String ports = "";
				String porth=housePortThread.getPort();
				String portw=weatherPortThread.getPort();
				if(porth!=""&&portw!=""){
					ports = porth+","+portw+",";
				}
				List<CommPortIdentifier> list = housePortThread.portlist();
				for(CommPortIdentifier id :list){
					
					ports = ports + id.getName()+",";
				}
				return ports;
			}
			//设置温室串口动作
			if(map.containsKey("configsaveh")){
				//第一个是当前的串口,后面是可用的串口
				String config = map.get("configsaveh");
				String[] cs = config.split(",");
				try {
					configAction.setPort(cs[0]);
					configAction.setBotelv(cs[1]);
					configAction.setType("house");
					configAction.setPlatform("pc");
					return configAction.config();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//设置气象站串口动作
			if(map.containsKey("configsavew")){
				//第一个是当前的串口,后面是可用的串口
				String config = map.get("configsavew");
				String[] cs = config.split(",");
				try {
					configAction.setPort(cs[0]);
					configAction.setBotelv(cs[1]);
					configAction.setType("weather");
					configAction.setPlatform("pc");
					return configAction.config();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//打开拓扑，获取节点动作
			if(map.containsKey("getpoint")){
				//第一个是当前的串口,后面是可用的串口
				String command = map.get("getpoint");				
				try {
					if(command.equals("")){	
						realDataAction.setPlatform("pc");
						return realDataAction.open();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//获取实时数据动作
			if(map.containsKey("getparameter")){								
				try {
					realDataAction.setPlatform("pc");
					return realDataAction.realData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//节点采集与停止动作
			if(map.containsKey("collect")){
				String command = map.get("collect");
				try {
					realDataAction.setData(command);
					if(command.startsWith("26525341")){
						realDataAction.setPlatform("app");
						return realDataAction.beginCollect();
					}
					if(command.startsWith("26545341")){
						realDataAction.setPlatform("app");
						return realDataAction.stopCollect();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//获取历史数据动作
			if(map.containsKey("historydata")){
				//第一个是当前的串口,后面是可用的串口
				String param = map.get("historydata");				
				try {
					if(param.equals("")){	
						historyParameterAction.setPlatform("app");
						return historyParameterAction.historyAll();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}						
			return rev;
		}
		/**
		 * 登录入口函数
		 * @param s 登录数据
		 * **/
		public String login(String s) throws Exception {

			String[] ids = s.split(":");			
			String result = new String("");
			userAction.setPlatform("pc");
			userAction.setUsername(ids[0]);
			userAction.setPassword(ids[1]);
			try {
				result = userAction.login();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	}
	public static void main(String[] args) {
		try {
			new AppSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
