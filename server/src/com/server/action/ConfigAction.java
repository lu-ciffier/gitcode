package com.server.action;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.server.jopo.Device;
import com.server.jopo.Parameter;
import com.server.jopo.SystemConfig;
import com.server.jopo.Weather;
import com.server.service.DeviceService;
import com.server.service.ParameterService;
import com.server.service.SystemConfigService;
import com.server.service.WeatherService;

/**
 * @author lucyf
 * @version 2017.5.10
 * 处理设置请求Action
 * **/
public class ConfigAction {

	private String port;
	private String botelv;
	private String type;
	private String platform;
	private String username = "";
	private String name;
	private String address;
	private String xposition;
	private String yposition;
	private String zposition;
	private SystemConfigService configService = null;
	private DeviceService deviceService = null;
	private ParameterService parameterService = null;
	private WeatherService weatherService = null;
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getBotelv() {
		return botelv;
	}
	public void setBotelv(String botelv) {
		this.botelv = botelv;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setXposition(String xposition) {
		this.xposition = xposition;
	}
	public void setYposition(String yposition) {
		this.yposition = yposition;
	}
	public void setZposition(String zposition) {
		this.zposition = zposition;
	}
	public void setConfigService(SystemConfigService configService) {
		this.configService = configService;
	}
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	public void setWeatherService(WeatherService weatherService) {
		this.weatherService = weatherService;
	}
	/*
	 * 设置系统串口
	 */
	public String config() throws Exception {
		Timestamp date = new Timestamp(new Date().getTime());
		if(platform.equals("app")){
			//System.out.println(platform+"3");
			configService.saveorupdate(new SystemConfig(username,type,port,botelv,date));
			String jsonp = "{flag:true}";
			String callback = ServletActionContext.getRequest().getParameter("jsoncallback");
			ServletActionContext.getResponse().getWriter().write(callback+"("+jsonp+")");		
			return null;
		}if(platform.equals("pc")){
			configService.saveorupdate(new SystemConfig(username,type,port,botelv,date));
			return null;
		}
		else{
			
			Map<String, Object> session = ActionContext.getContext().getSession();
			username = (String) session.get("username");
			configService.saveorupdate(new SystemConfig(username,type,port,botelv,date));			
			ServletActionContext.getResponse().getWriter().write("Save Ok");
			return null;
		}
		
	}
	/*
	 * 设置3D坐标
	 */
	public String device() throws Exception {
		Timestamp date = new Timestamp(new Date().getTime());
		Device device = new Device();
		device.setName(name);
		device.setAddress(address);
		device.setXposition(Integer.parseInt(xposition));
		device.setYposition(Integer.parseInt(yposition));
		device.setZposition(Integer.parseInt(zposition));
		device.setDate(date);
		deviceService.save(device);
		if(platform.equals("app")){			
			String callback = ServletActionContext.getRequest().getParameter("jsoncallback");			
			ServletActionContext.getResponse().getWriter().write(callback+"({flag:true})");
			return null;
		}else{	
			ServletActionContext.getResponse().getWriter().write("Save Ok");
			return null;
			//String json = "save ok";
			//return json;
		}
	}
	/*
	 * 根据IEEE地址获取3D坐标
	 */
	public String position() throws IOException{
		Device device =  deviceService.find(address);
		JSONObject ob = new JSONObject();
		ob.put("x", device.getXposition());
		ob.put("y", device.getYposition());
		ob.put("z", device.getZposition());
		if(platform.equals("web")){			
			ServletActionContext.getResponse().getWriter().write(ob.toString());
		}
		return null;
	}
	/*
	 * 根据3D坐标获取设备信息
	 */
	public String address() throws IOException{
	
		Device device =  deviceService.find(xposition, yposition, zposition);
		if(device!=null){
			String add = device.getAddress();
			Parameter p = null;
			if (device.getName().contains("point")) {
				List<Parameter> list = parameterService.findByPoint(add);
				if (list != null) {
					p = list.get(list.size() - 1);
					p.setPointnum("point");
				} else {
					p = new Parameter(null, "point", add, null, null, null,
							null, null, null, null, null);
				}
			}
			if (device.getName().contains("gateway")) {
				p = new Parameter(null, "gateway", add, null, null, null,
						null, null, null, null, null);
			}
			if (device.getName().contains("camera")) {
				p = new Parameter(null, "camera", add, null, null, null,
						null, null, null, null, null);
			}
			if (device.getName().contains("weather")) {
				List<Weather> list = weatherService.findAll();
				Weather w = new Weather();
				if (list != null) {
					w = list.get(list.size() - 1);
					w.setPointNum("weather");
				} else {					
					w = new Weather("weather", null, null, null, null, 
							null, null, null, null);
				}
				JSONObject object = JSONObject.fromObject(w);
				ServletActionContext.getResponse().getWriter().write(object.toString());
				return null;
			}
			JSONObject object = JSONObject.fromObject(p);
			ServletActionContext.getResponse().getWriter().write(object.toString());			
		}
		return null;
	}
	/*
	 * 删除
	 */
	public String delete() throws Exception{
		
		return null;
	}
	/*
	 * 获取服务器的IP地址
	 */
	public String hostip() throws Exception{
		String ip = InetAddress.getLocalHost().getHostAddress();
		ServletActionContext.getResponse().getWriter().write(ip);
		return null;
	}
}
