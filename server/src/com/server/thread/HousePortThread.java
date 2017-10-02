package com.server.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.comm.*;

import com.server.dao.AlertDao;
import com.server.dao.AlertinfoDao;
import com.server.dao.ParameterDao;
import com.server.dao.PointDao;
import com.server.dao.SystemConfigDao;
import com.server.dao.TopologyDao;
import com.server.jopo.Alert;
import com.server.jopo.Alertinfo;
import com.server.jopo.Parameter;
import com.server.jopo.Point;
import com.server.jopo.SystemConfig;
import com.server.jopo.Topology;

/**
 * @author lucyf
 * @version 2017.5.10
 * 温室串口数据监听线程
 * **/
public class HousePortThread {

	private  CommPortIdentifier portId;
	private  SerialPort serialPort;
	private  InputStream inputStream;
	private  OutputStream outputStream;
	private  Thread readThread;
	private  StringBuilder stringBuilder;
	private List<PortDataListener> listenerList;
	private SystemConfigDao configDao = null;
	private ParameterDao parameterDao = null;
	private AlertDao alertDao = null;
	private AlertinfoDao alertinfoDao = null;
	private PointDao pointDao = null;
	private TopologyDao topologyDao = null;
	private String port;
	private String botelv;
	private  boolean onDataReceive;
	private  byte[] dataByte;
	public void setConfigDao(SystemConfigDao configDao) {
		this.configDao = configDao;
	}
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
	public void setUsername(String username) {
	}
	public boolean isOnDataReceive() {
		return onDataReceive;
	}
	public String getPort() {
		return port;
	}
	public String getBotelv() {
		return botelv;
	}
	/*
	 * 服务器启动，加载串口
	 */
	public void init(){
		port = "";
		botelv = "";
		SystemConfig config = configDao.find("house");
		this.port = config.getPort();
		this.botelv = config.getBotelv();
		initHousePort(port, Integer.parseInt(botelv));	
	}
	/*
	 * 串口初始化
	 */
	public void initHousePort(String port,int botelv){
		portId = null;
		onDataReceive = false;	
		dataByte = new byte[10240];
		stringBuilder = new StringBuilder("");
		listenerList = Collections.synchronizedList(new ArrayList<PortDataListener>());
		for (CommPortIdentifier portIdentifier : portlist()) {
			portId = portIdentifier;
			if (port.equals(portId.getName())){
				break;
			}
		}
		try {
			serialPort = (SerialPort) portId.open("house", 400);
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
			serialPort.notifyOnDataAvailable(true);
			serialPort.setSerialPortParams(botelv,
					SerialPort.DATABITS_8, 
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			serialPort.addEventListener(new serialPortListener());
			System.out.println("House Port "+port+" Init Ok!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/*
	 * 串口写入数据
	 * @param s 写入的数据
	 **/
	public void writePort(String s){
		try {
			if(s.startsWith("26524E44")){
				/**
				 * 收到获取节点拓扑命令的时候就更新节点拓扑
				 * */
				pointDao.deleteAll();
				topologyDao.deleteAll();
			}
			outputStream.write(hexStringToBytes(s));
			outputStream.flush();
			System.out.println("port write:"+s);
		} catch (IOException e) {
			throw new RuntimeException("");
		}
	}
	/*
	 * 串口读取数据
	 **/
	public String readPort(){
		String dataString = "";
		dataString = stringBuilder.toString();
		stringBuilder.setLength(0);
		onDataReceive = false;
		return dataString;
	}
	/*
	 * 串口关闭
	 **/
	public void closePort(){
		try {
			inputStream.close();
			outputStream.close();
			readThread.join(1000);
			serialPort.close();
			System.out.println("port closed ok!");
		} catch (IOException e) {
			throw new RuntimeException("");
		}catch (InterruptedException e) {				
			throw new RuntimeException("");
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
				/**
				 * 将实时数据进行推送
				 **/
				sendPortData(builder+"<br>");
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
	public synchronized void addPortDataListener(PortDataListener listener){
		this.listenerList.add(listener);
	}
	public synchronized void removePortDataListener(PortDataListener listener){
		this.listenerList.remove(listener);
	}
	/*
	 * 给所有监听器推送实时数据
	 * @param sb 推送的数据
	 * **/
	public synchronized void sendPortData(String sb){
		for(PortDataListener listener :listenerList){
			listener.getPortData(sb);
		}
	}
	/*
	 * 串口接收数据监听器
	 **/
	public class serialPortListener implements SerialPortEventListener
	{
		public void serialEvent(SerialPortEvent event) {
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					onDataReceive = true;
					int len = 0;
					while (inputStream.available() > 0) {
						len = inputStream.read(dataByte);
						for (int i = 0; i < len; i++) {
							String hex = Integer.toHexString(dataByte[i] & 0xFF);
							if (hex.length() == 1) {
								hex = '0' + hex;
							}						
							stringBuilder.append(hex.toUpperCase()+" ");
						}
						//System.out.println(stringBuilder);						
						//stringBuilder.setLength(0);
						dataPreProcess(stringBuilder.toString());
					}
				} catch (IOException e) {
					throw new RuntimeException("读取串口错误");
				}			
								
			}else {
				System.out.println("read ok");
			}									
		}		
	}
	/*
	 * 获取服务器所有串口
	 * **/
	public List<CommPortIdentifier> portlist() {
		List<CommPortIdentifier> list = new ArrayList<CommPortIdentifier>();
		Enumeration<?> en = CommPortIdentifier.getPortIdentifiers();
		while (en.hasMoreElements()) {
			portId = (CommPortIdentifier) en.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				list.add(portId);
			}
		}
		return list;
	}
	/*
	 * 16进制转字节函数
	 * **/
	public byte[] hexStringToBytes(String hexString)// 16进制转字节
	{
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	/*
	 * 字符转字节函数
	 * **/
	private byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);

	}
	
	public static void main(String[] args) {
		//new PortThread().init("COM2", 9600);
		
	}
	
}
