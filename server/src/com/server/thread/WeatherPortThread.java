package com.server.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.comm.*;

import com.server.dao.SystemConfigDao;
import com.server.dao.WeatherDao;
import com.server.jopo.SystemConfig;
import com.server.jopo.Weather;


/**
 * 气象站环境数据Socket
 * **/
public class WeatherPortThread {

	private  CommPortIdentifier portId;
	private  SerialPort serialPort;
	private  InputStream inputStream;
	private  OutputStream outputStream;
	private  Thread readThread;
	private  StringBuilder stringBuilder;
	private List<PortDataListener> listenerList;
	private SystemConfigDao configDao = null;
	private WeatherDao weatherDao = null;
	private String port;
	private String botelv;
	private  boolean onDataReceive;
	private  byte[] dataByte;

	public boolean isOnDataReceive() {
		return onDataReceive;
	}
	public String getPort() {
		return port;
	}
	public String getBotelv() {
		return botelv;
	}
	public void setConfigDao(SystemConfigDao configDao) {
		this.configDao = configDao;
	}
	public void setWeatherDao(WeatherDao weatherDao) {
		this.weatherDao = weatherDao;
	}
	public void init(){
		port = "";
		botelv = "";
		SystemConfig config = configDao.find("weather");
		this.port = config.getPort();
		this.botelv = config.getBotelv();		
		initWeatherPort(port, Integer.parseInt(botelv));
	}
	/**
	 * 串口初始化
	 **/
	public void initWeatherPort(String port,int botelv){
		onDataReceive = false;	
		dataByte = new byte[10240];
		stringBuilder = new StringBuilder("");
		listenerList = Collections.synchronizedList(new ArrayList<PortDataListener>());
		try {
			portId = CommPortIdentifier.getPortIdentifier(port);
		} catch (NoSuchPortException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			serialPort = (SerialPort) portId.open("weather", 2000);
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
			serialPort.notifyOnDataAvailable(true);
			serialPort.setSerialPortParams(botelv,
					SerialPort.DATABITS_8, 
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			serialPort.addEventListener(new serialPortListener());
			System.out.println("Weather Port "+port+" Init Ok!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 串口读取数据
	 **/
	public String readPort(){
		String dataString = "";
		dataString = stringBuilder.toString();
		stringBuilder.setLength(0);
		onDataReceive = false;
		return dataString;
	}
	/**
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
	/**
	 * 串口接收数据预处理、保存数据库
	 **/
	public void dataPreProcess(String data){
		
		String s = data.replaceAll(" ", "");
		System.out.println("read data before:"+s);
		while(s.startsWith("7E427D5E")&&s.indexOf("7E", 8)>0){
			int end = s.indexOf("7E", 5);
			String entry = s.substring(0, end+2);
			System.out.println("read data after:"+entry);
			String amtype = entry.substring(10, 12);
			if(amtype.equals("0B")){
				Weather weather = Weather.compute(entry);
				if(weather!=null){
					weatherDao.save(weather);
				}
			}
			stringBuilder = new StringBuilder(s.substring(end+2));
			s = stringBuilder.toString();
		}
		
	}	
	
	public synchronized void addPortDataListener(PortDataListener listener){
		this.listenerList.add(listener);
	}
	public synchronized void removePortDataListener(PortDataListener listener){
		this.listenerList.remove(listener);
	}
	public synchronized void sendPortData(String sb){
		for(PortDataListener listener :listenerList){
			listener.getPortData(sb);
		}
	}
	/**
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
	/**
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
	/**
	 * 字符转字节函数
	 * **/
	private byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);

	}
	
}
