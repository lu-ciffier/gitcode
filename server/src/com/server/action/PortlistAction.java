package com.server.action;

import java.io.IOException;
import java.util.List;

import javax.comm.CommPortIdentifier;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.server.thread.HousePortThread;
/**
 * @author lucyf
 * @version 2017.5.10
 * 处理串口请求Action
 * **/
public class PortlistAction {

	private HousePortThread portThread = null;
	private String platform;
	private List<CommPortIdentifier> list = null;
	
	public void setPortThread(HousePortThread portThread) {
		this.portThread = portThread;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/*
	 * 获取服务器所有串口
	 */
	public String portlist() throws Exception{
		String ports="";
		list = portThread.portlist();
		for(CommPortIdentifier id :list){
			if(ports!=""){
				ports = ports+",";
			}
			ports = ports + id.getName();
		}
		String jsonp = "({ports:"+"'"+ports+"'"+"})";
		
		if(platform.equals("app")){
			String callback = ServletActionContext.getRequest().getParameter("jsoncallback");
			//System.out.println(jsonp);
			JSONArray array = new JSONArray();
			for(CommPortIdentifier id :list){
				JSONObject o = new JSONObject();
				o.put("name", id.getName());
				array.add(o);
			}
			System.out.println(array);
			ServletActionContext.getResponse().getWriter().write(callback+'('+array+')');
			return null;
		}else if(platform.equals("ap")){//from android app
			JSONArray array = new JSONArray();
			for(CommPortIdentifier id :list){
				JSONObject o = new JSONObject();
				o.put("name", id.getName());
				array.add(o);
			}
			ServletActionContext.getResponse().getWriter().write(array.toString());
			return null;
		}else{
			//System.out.println(ports);
			ServletActionContext.getResponse().getWriter().write(ports);		
			return null;
		}
		
	}
	/*
	 * 获取服务器当前串口
	 */
	public String getSystemConfig() throws IOException{
		String port = portThread.getPort();
		//String botelv = portThread.getBotelv();
		if(port!=""){
			ServletActionContext.getResponse().getWriter().write(port);
		}
		return null;
	}
}
