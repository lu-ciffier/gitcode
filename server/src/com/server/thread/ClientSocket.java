package com.server.thread;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.server.dao.AlertDao;
import com.server.dao.AlertinfoDao;
import com.server.dao.ParameterDao;
import com.server.dao.PointDao;
import com.server.dao.TopologyDao;
import com.server.jopo.Alert;
import com.server.jopo.Alertinfo;
import com.server.jopo.Parameter;
import com.server.jopo.Point;
import com.server.jopo.Topology;


/**
 * @author lucyf
 * @version 2017.5.10
 * 电脑APP客户端Socket
 * **/
public class ClientSocket {

	private String serverIp;
	private int port;
	private Socket socket;
	private boolean running=false;
	private  StringBuilder stringBuilder;
	
	private ParameterDao parameterDao = null;
	private AlertDao alertDao = null;
	private AlertinfoDao alertinfoDao = null;
	private PointDao pointDao = null;
	private TopologyDao topologyDao = null;
	
	
	public void setParameterDao(ParameterDao parameterDao) {
		this.parameterDao = parameterDao;
	}
	public void setAlertDao(AlertDao alertDao) {
		this.alertDao = alertDao;
	}
	public void setAlertinfoDao(AlertinfoDao alertinfoDao) {
		this.alertinfoDao = alertinfoDao;
	}
	public void setPointDao(PointDao pointDao) {
		this.pointDao = pointDao;
	}
	public void setTopologyDao(TopologyDao topologyDao) {
		this.topologyDao = topologyDao;
	}
	public ClientSocket() {
		System.out.println("ClientSocket:"+this.hashCode());
		this.serverIp="192.168.1.120";
		this.port=1234;
		stringBuilder = new StringBuilder("");
		try {
			start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ClientSocket(String serverIp, int port) {
		this.serverIp=serverIp;
		this.port=port;
		stringBuilder = new StringBuilder("");
		
	}
	/**
	 * 实例化Socket
	 * **/
	public void start() throws UnknownHostException, IOException {
		if(running)return;
		socket = new Socket(serverIp,port);
		System.out.println("本地端口："+socket.getLocalPort());		
		running=true;
		
		
		new Thread(new ReceiveWatchDog()).start();
	}
	
	public void stop(){
		if(running)running=false;
	}

	/**
	 * 发送请求动作
	 * **/
	public void send(String obj) throws IOException {
		OutputStream os = socket.getOutputStream();
		os.write(obj.getBytes());
		System.out.println("发送的数据："+obj);
		os.flush();
	}
	/**
	 * 监听服务器端响应动作
	 * **/
	class ReceiveWatchDog implements Runnable{
		byte[] b = new byte[10240];
		public void run() {
			while(running){
				try {
					InputStream in = socket.getInputStream();
					if(in.available()>0){
						int len = in.read(b);
						//System.out.println("lenth:" + len);			
						for (int i = 0; i < len; i++) {
							String hex = Integer.toHexString(b[i] & 0xFF);
							if (hex.length() == 1) {
								hex = '0' + hex;
							}						
							stringBuilder.append(hex.toUpperCase()+" ");
						}
						dataPreProcess(stringBuilder.toString());
						System.out.println("接收的数据:" + stringBuilder);
						
					}else{
						Thread.sleep(10);
					}
				} catch (Exception e) {
					e.printStackTrace();
					ClientSocket.this.stop();
				}
					
			}
		}
	}
	/*
	 * 串口接收数据预处理、保存数据库
	 * @param data 预处理的数据
	 **/
	public void dataPreProcess(String data){
		//StringBuilder builder = new StringBuilder("");
		String s = data.replaceAll(" ", "");
		int a = (s.length())/64;
		if(a>0){
			String builder = new String(s.substring(0, a*64));
			stringBuilder = new StringBuilder(s.substring(a*64));
			if(builder.startsWith("26524E44")){
				System.out.println("网络拓扑："+builder);
				pointDao.save(new Point().getPoints(builder));
			}else if(builder.startsWith("26525341")){
				System.out.println("节点采集："+builder);
				Parameter parameter = new Parameter();
				parameter.setUsername("admin");
				List<Parameter> list = parameter.getParameters(builder);
				for(Parameter p:list){
					parameterDao.save(p);
					List<Alert> alerts = alertDao.findBypoint(p.getPoint());
					if(alerts!=null){
						
						for(Alert alert :alerts){
							String datatype = alert.getDatatype();
							switch (datatype) {
							case "空气温度":
								if(p.getAirT()<alert.getMin()){
									Alertinfo alertinfo = new Alertinfo();
									Timestamp date = new Timestamp(new Date().getTime());
									alertinfo.setName(p.getPointnum()+"-"+datatype+"-min");
									alertinfo.setPoint(p.getPoint());
									alertinfo.setReason("警报，空气温度已低于"+alert.getMin()+"摄氏度");
									alertinfo.setDate(date);
									break;
								}
								if(p.getAirT()>alert.getMax()){
									Alertinfo alertinfo = new Alertinfo();
									Timestamp date = new Timestamp(new Date().getTime());
									alertinfo.setName(p.getPointnum()+"-"+datatype+"-max");
									alertinfo.setPoint(p.getPoint());
									alertinfo.setReason("警报，空气温度已超过"+alert.getMax()+"摄氏度");
									alertinfo.setDate(date);
									alertinfoDao.save(alertinfo);
									break;
								}
							 case "空气湿度":
								if(p.getAirH()<alert.getMin()){
									Alertinfo alertinfo = new Alertinfo();
									Timestamp date = new Timestamp(new Date().getTime());
									alertinfo.setName(p.getPointnum()+"-"+datatype+"-min");
									alertinfo.setPoint(p.getPoint());
									alertinfo.setReason("警报，空气湿度已低于"+alert.getMin()+"%");
									alertinfo.setDate(date);
									alertinfoDao.save(alertinfo);
									break;
								}
								if(p.getAirH()>alert.getMax()){
									Alertinfo alertinfo = new Alertinfo();
									Timestamp date = new Timestamp(new Date().getTime());
									alertinfo.setName(p.getPointnum()+"-"+datatype+"-max");
									alertinfo.setPoint(p.getPoint());
									alertinfo.setReason("警报，空气湿度已超过"+alert.getMax()+"%");
									alertinfo.setDate(date);
									alertinfoDao.save(alertinfo);
									break;
								}
							  case "光照强度":
									if(p.getIll()<alert.getMin()){
										Alertinfo alertinfo = new Alertinfo();
										Timestamp date = new Timestamp(new Date().getTime());
										alertinfo.setName(p.getPointnum()+"-"+datatype+"-min");
										alertinfo.setPoint(p.getPoint());
										alertinfo.setReason("警报，光照强度已低于"+alert.getMin()+"勒克斯");
										alertinfo.setDate(date);
										alertinfoDao.save(alertinfo);
										break;
									}
									if(p.getIll()>alert.getMax()){
										Alertinfo alertinfo = new Alertinfo();
										Timestamp date = new Timestamp(new Date().getTime());
										alertinfo.setName(p.getPointnum()+"-"+datatype+"-max");
										alertinfo.setPoint(p.getPoint());
										alertinfo.setReason("警报，光照强度已超过"+alert.getMax()+"勒克斯");
										alertinfo.setDate(date);
										alertinfoDao.save(alertinfo);
										break;
								}
							  case "二氧化碳浓度":
									if(p.getCo2()<alert.getMin()){
										Alertinfo alertinfo = new Alertinfo();
										Timestamp date = new Timestamp(new Date().getTime());
										alertinfo.setName(p.getPointnum()+"-"+datatype+"-min");
										alertinfo.setPoint(p.getPoint());
										alertinfo.setReason("警报，二氧化碳浓度已低于"+alert.getMin()+"ppm");
										alertinfo.setDate(date);
										alertinfoDao.save(alertinfo);
										break;
									}
									if(p.getCo2()>alert.getMax()){
										Alertinfo alertinfo = new Alertinfo();
										Timestamp date = new Timestamp(new Date().getTime());
										alertinfo.setName(p.getPointnum()+"-"+datatype+"-max");
										alertinfo.setPoint(p.getPoint());
										alertinfo.setReason("警报，二氧化碳浓度已超过"+alert.getMax()+"ppm");
										alertinfo.setDate(date);
										alertinfoDao.save(alertinfo);
										break;
								}
							  case "土壤温度":
									if(p.getSoilT()<alert.getMin()){
										Alertinfo alertinfo = new Alertinfo();
										Timestamp date = new Timestamp(new Date().getTime());
										alertinfo.setName(p.getPointnum()+"-"+datatype+"-min");
										alertinfo.setPoint(p.getPoint());
										alertinfo.setReason("警报，土壤温度已低于"+alert.getMin()+"摄氏度");
										alertinfo.setDate(date);
										alertinfoDao.save(alertinfo);
										break;
									}
									if(p.getSoilT()>alert.getMax()){
										Alertinfo alertinfo = new Alertinfo();
										Timestamp date = new Timestamp(new Date().getTime());
										alertinfo.setName(p.getPointnum()+"-"+datatype+"-max");
										alertinfo.setPoint(p.getPoint());
										alertinfo.setReason("警报，土壤温度已超过"+alert.getMax()+"摄氏度");
										alertinfo.setDate(date);
										alertinfoDao.save(alertinfo);
										break;
								}
							  case "土壤湿度":
									if(p.getSoilH()<alert.getMin()){
										Alertinfo alertinfo = new Alertinfo();
										Timestamp date = new Timestamp(new Date().getTime());
										alertinfo.setName(p.getPointnum()+"-"+datatype+"-min");
										alertinfo.setPoint(p.getPoint());
										alertinfo.setReason("警报，土壤湿度已低于"+alert.getMin()+"%");
										alertinfo.setDate(date);
										alertinfoDao.save(alertinfo);
										break;
									}
									if(p.getSoilH()>alert.getMax()){
										Alertinfo alertinfo = new Alertinfo();
										Timestamp date = new Timestamp(new Date().getTime());
										alertinfo.setName(p.getPointnum()+"-"+datatype+"-max");
										alertinfo.setPoint(p.getPoint());
										alertinfo.setReason("警报，土壤湿度已超过"+alert.getMax()+"%");
										alertinfo.setDate(date);
										alertinfoDao.save(alertinfo);
										break;
								}
							  case "工作电压":
									if(p.getVoltage()<alert.getMin()){
										Alertinfo alertinfo = new Alertinfo();
										Timestamp date = new Timestamp(new Date().getTime());
										alertinfo.setName(p.getPointnum()+"-"+datatype+"-min");
										alertinfo.setPoint(p.getPoint());
										alertinfo.setReason("警报，工作电压已低于"+alert.getMin()+"V");
										alertinfo.setDate(date);
										alertinfoDao.save(alertinfo);
										break;
									}
									if(p.getVoltage()>alert.getMax()){
										Alertinfo alertinfo = new Alertinfo();
										Timestamp date = new Timestamp(new Date().getTime());
										alertinfo.setName(p.getPointnum()+"-"+datatype+"-max");
										alertinfo.setPoint(p.getPoint());
										alertinfo.setReason("警报，工作电压已超过"+alert.getMax()+"V");
										alertinfo.setDate(date);
										alertinfoDao.save(alertinfo);
										break;
								}

							default:
								break;
							}
						}
					}
				}
				
			}else if(builder.startsWith("26524E53")){
				System.out.println("通信质量："+builder);
				Topology topology = new Topology();
				List<Topology> tolist = topology.getTopologys(builder);
				for(Topology p : tolist){				
					topologyDao.save(p);
				}
			}
		}	
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		String serverIp = "192.168.1.105";
		int port = 1234;
		ClientSocket client = new ClientSocket(serverIp,port);
		client.start();
	}
	
}

