package com.server.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.server.jopo.Parameter;
import com.server.service.ParameterService;

/**
 * @author lucyf
 * @version 2017.5.10
 * 处理历史数据请求Action
 * **/
public class HistoryParameterAction {
	private String username = null;
	private String platform = "web";
	private List<Parameter> list;
	private String date_from;
	private String date_to;
	private String point;
	private String datatype;
	static String data = null;
	private ParameterService parameterService = null;
	public List<Parameter> getList() {
		return list;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public void setDate_from(String date_from) {
		this.date_from = date_from;
	}
	public void setDate_to(String date_to) {
		this.date_to = date_to;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	/*
	 * 查询所有历史数据
	 */
	public String historyAll() throws Exception {
		list = new ArrayList<Parameter>();
		list = parameterService.findAll();
		JSONArray array = list2json(list);
		System.out.println("select history");
		if(platform.equals("web")){
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().write(array.toString());
			return null;
		}
		if(platform.equals("app")){//from ionic app
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			String callback = ServletActionContext.getRequest().getParameter("jsoncallback");
			ServletActionContext.getResponse().getWriter().write(callback+"("+array+")");
			return null;
		}
		if(platform.equals("ap")){//from android app
			
			return "success";
		}
		return array.toString();
	}
	/*
	 * 根据时间查询历史数据
	 */
	public String historyByTime() throws Exception {
		list = new ArrayList<Parameter>();
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		Map<String, Object> session = ActionContext.getContext().getSession();
		username = (String) session.get("username");	
		data = "";
		Timestamp from = Timestamp.valueOf(date_from+" 00:00:00");
		Timestamp to = Timestamp.valueOf(date_to+" 00:00:00");
		list = parameterService.findByTime(from, to);
		if(!list.isEmpty()){		
			JSONArray array = list2json(list);	
			ServletActionContext.getResponse().getWriter().write(array.toString());
		}
		return null;				
	}
	/*
	 * 查询节点最新历史数据
	 */
	public String historyByPointLatest() throws Exception {
		list = new ArrayList<Parameter>();
		list = parameterService.findByPoint(point);	
		if(!list.isEmpty()){		
			JSONObject ob = JSONObject.fromObject(list.get(list.size()-1));
			if(platform.equals("app")){
				String callback = ServletActionContext.getRequest().getParameter("jsoncallback");
				ServletActionContext.getResponse().getWriter().write(callback+"("+ob+")");
				return null;
			}			
		}
		return null;				
	}
	/*
	 * 根据节点查询历史数据
	 */
	public String historyByPoint() throws IOException{
		list = new ArrayList<Parameter>();
		list = parameterService.findByPoint(point);
		if(!list.isEmpty()){
			if(platform.equals("app")){//from ionic app
				System.out.println("app return");
				JSONArray array = list2json(list);
				String callback = ServletActionContext.getRequest().getParameter("jsoncallback");
				ServletActionContext.getResponse().getWriter().write(callback+"("+array+")");
				return null;
			}else{
				if(datatype.equals("1")){			
					List<String> tempList = new ArrayList<>();
					List<String> timeList = new ArrayList<>();
					for(Parameter p : list ){
						tempList.add(p.getAirT().toString());
						timeList.add(p.getDate().getHours()+":"+p.getDate().getMinutes());
					}
					JSONArray array1 = JSONArray.fromObject(tempList);
					JSONArray array2 = JSONArray.fromObject(timeList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("2")) {
					List<String> tempList = new ArrayList<>();
					List<String> timeList = new ArrayList<>();
					for(Parameter p : list ){
						tempList.add(p.getAirH().toString());
						timeList.add(p.getDate().getHours()+":"+p.getDate().getMinutes());
					}
					JSONArray array1 = JSONArray.fromObject(tempList);
					JSONArray array2 = JSONArray.fromObject(timeList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("3")) {
					List<String> tempList = new ArrayList<>();
					List<String> timeList = new ArrayList<>();
					for(Parameter p : list ){
						tempList.add(p.getIll().toString());
						timeList.add(p.getDate().getHours()+":"+p.getDate().getMinutes());
					}
					JSONArray array1 = JSONArray.fromObject(tempList);
					JSONArray array2 = JSONArray.fromObject(timeList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("4")) {
					List<String> tempList = new ArrayList<>();
					List<String> timeList = new ArrayList<>();
					for(Parameter p : list ){
						tempList.add(p.getCo2().toString());
						timeList.add(p.getDate().getHours()+":"+p.getDate().getMinutes());
					}
					JSONArray array1 = JSONArray.fromObject(tempList);
					JSONArray array2 = JSONArray.fromObject(timeList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("5")) {
					List<String> tempList = new ArrayList<>();
					List<String> timeList = new ArrayList<>();
					for(Parameter p : list ){
						tempList.add(p.getSoilT().toString());
						timeList.add(p.getDate().getHours()+":"+p.getDate().getMinutes());
					}
					JSONArray array1 = JSONArray.fromObject(tempList);
					JSONArray array2 = JSONArray.fromObject(timeList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("6")) {
					List<String> tempList = new ArrayList<>();
					List<String> timeList = new ArrayList<>();
					for(Parameter p : list ){
						tempList.add(p.getSoilH().toString());
						timeList.add(p.getDate().getHours()+":"+p.getDate().getMinutes());
					}
					JSONArray array1 = JSONArray.fromObject(tempList);
					JSONArray array2 = JSONArray.fromObject(timeList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}
				if (datatype.equals("7")) {
					List<String> tempList = new ArrayList<>();
					List<String> timeList = new ArrayList<>();
					for(Parameter p : list ){
						tempList.add(p.getVoltage().toString());
						timeList.add(p.getDate().getHours()+":"+p.getDate().getMinutes());
					}
					JSONArray array1 = JSONArray.fromObject(tempList);
					JSONArray array2 = JSONArray.fromObject(timeList);
					ServletActionContext.getResponse().getWriter().write(
							array1.toString()+"&"+array2.toString());
					return null;
				}				
			}				
		}		
		return null;
	}
	/*
	 * list转换成json
	 */
	public JSONArray list2json(List<Parameter> list) {
		JSONArray array = new JSONArray();
		
		for(Parameter p : list){
			if(validateData(p)){			
				JSONObject ob = new JSONObject();
				ob.put("id", p.getId());
				ob.put("pointnum", p.getPointnum());
				ob.put("point", p.getPoint());
				ob.put("air_tem", p.getAirT());
				ob.put("air_hum", p.getAirH());
				ob.put("ill", p.getIll());
				ob.put("co2", p.getCo2());
				ob.put("soil_tem", p.getSoilT());
				ob.put("soil_hum", p.getSoilH());
				ob.put("voltage", p.getVoltage().toString());
				ob.put("date", p.getDate().toString());
				array.add(ob);
			}
		}
		return array;
	}
	/*
	 * 验证温室数据是否有效
	 */
	public boolean validateData(Parameter p){
		if(p.getAirT()>-150.0&&p.getAirT()<150.0){
			if(p.getSoilT()>-150.0&&p.getSoilT()<150.0){
				if(p.getAirH()>=0&&p.getAirH()<=100){
					if(p.getSoilH()>=0&&p.getSoilH()<=100){
						if(p.getIll()>0){							
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
