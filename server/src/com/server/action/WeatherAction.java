package com.server.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.server.jopo.Weather;
import com.server.service.WeatherService;
/**
 * @author lucyf
 * @version 2017.5.10
 * 处理气象站请求Action
 * **/
public class WeatherAction {
	private String platform="web";
	private String point;
	private String datatype;
	private WeatherService weatherService = null;
	private List<Weather> list;
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public void setWeatherService(WeatherService weatherService) {
		this.weatherService = weatherService;
	}
	/*
	 * 根据节点号获取气象站历史数据
	 */
	public String findByPointNum() throws IOException{
		
		list = new ArrayList<Weather>();
		list = weatherService.findByPointnum(point);
		if(!list.isEmpty()){
			if(platform.equals("app")){
				System.out.println("app return");
				JSONArray array = JSONArray.fromObject(list);
				String callback = ServletActionContext.getRequest().getParameter("jsoncallback");
				ServletActionContext.getResponse().getWriter().write(callback+"("+array+")");
				return null;
			}else{
				List<String> yList = new ArrayList<>();
				List<String> xList = new ArrayList<>();
				for(Weather w : list ){
					xList.add(w.getDate().getHours()+":"+
							w.getDate().getMinutes());
				}
				if(datatype.equals("1")){
					yList.clear();
					for(Weather w : list ){
						yList.add(w.getTemperature().toString());			
					}
					JSONArray array1 = JSONArray.fromObject(yList);
					JSONArray array2 = JSONArray.fromObject(xList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("2")) {
					yList.clear();
					for(Weather w : list ){
						yList.add(w.getHumidity().toString());
					}
					JSONArray array1 = JSONArray.fromObject(yList);
					JSONArray array2 = JSONArray.fromObject(xList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("3")) {
					yList.clear();
					for(Weather w : list ){
						yList.add(w.getSolarRadiation().toString());
					}
					JSONArray array1 = JSONArray.fromObject(yList);
					JSONArray array2 = JSONArray.fromObject(xList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("4")) {
					yList.clear();
					for(Weather w : list ){
						yList.add(w.getWindSpeed().toString());
					}
					JSONArray array1 = JSONArray.fromObject(yList);
					JSONArray array2 = JSONArray.fromObject(xList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("5")) {
					yList.clear();
					for(Weather w : list ){
						yList.add(w.getWindDir().toString());
					}
					JSONArray array1 = JSONArray.fromObject(yList);
					JSONArray array2 = JSONArray.fromObject(xList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("6")) {
					yList.clear();
					for(Weather w : list ){
						yList.add(w.getBatteryV().toString());
					}
					JSONArray array1 = JSONArray.fromObject(yList);
					JSONArray array2 = JSONArray.fromObject(xList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("7")) {
					yList.clear();
					for(Weather w : list ){
						yList.add(w.getSolarV().toString());
					}
					JSONArray array1 = JSONArray.fromObject(yList);
					JSONArray array2 = JSONArray.fromObject(xList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}				
			}
		}
		return null;
	}
	/*
	 * 根据节点号获取气象站最新历史数据
	 */
	public String historyByPointNumLatest() throws IOException{
		list = new ArrayList<Weather>();
		list = weatherService.findByPointnum(point);
		if(!list.isEmpty()){		
			JSONObject ob = JSONObject.fromObject(list.get(list.size()-1));
			if(platform.equals("app")){
				String callback = ServletActionContext.getRequest().getParameter("jsoncallback");
				ServletActionContext.getResponse().getWriter().write(callback+"("+ob+")");
				System.out.println(ob.toString());
				return null;
			}			
		}
		return null;
	}
}
