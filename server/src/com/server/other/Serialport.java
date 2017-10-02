package com.server.other;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

public class Serialport {
	private  CommPortIdentifier portId;
	private  SerialPort serialPort;
	private  InputStream inputStream;
	private  OutputStream outputStream;
	private  StringBuilder stringBuilder;
	private  byte[] dataByte;
	/**
	 * 串口初始化
	 * **/
	public void init() {
		for (CommPortIdentifier portIdentifier : portlist()) {
			portId = portIdentifier;
			if ("COM2".equals(portId.getName())){
				break;
			}
		}
		try {
			serialPort = (SerialPort) portId.open("app", 2000);
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
			serialPort.notifyOnDataAvailable(true);
			serialPort.setSerialPortParams(38400,
					SerialPort.DATABITS_8, 
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			serialPort.addEventListener(new serialPortListener());
			System.out.println("port init ok!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 串口数据接收监听器
	 **/
	public class serialPortListener implements SerialPortEventListener
	{
		public void serialEvent(SerialPortEvent event) {
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
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
	 * 串口写入数据
	 **/
	public void writePort(String s){
		try {
			outputStream.write(s.getBytes());
			outputStream.flush();
			System.out.println("port write:"+s);
		} catch (IOException e) {
			throw new RuntimeException("");
		}	
	}
	/**
	 * 串口读取数据
	 **/
	public String readPort(){
		String dataString = "";
		dataString = stringBuilder.toString();
		stringBuilder.setLength(0);
		return dataString;
	}
	/**
	 * 串口关闭
	 **/
	public void closePort(){
		try {
			inputStream.close();
			outputStream.close();
			serialPort.close();			
		} catch (IOException e) {
			throw new RuntimeException("");
		}
	}
	/**
	 * 获取设备上的所有可用串口
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
	public static void main(String[] args) {
		Serialport sp = new Serialport();
		
		System.out.println(sp.portlist().get(1).getName());
	}
}
