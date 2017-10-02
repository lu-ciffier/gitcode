package com.server.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.server.jopo.Alert;
import com.server.service.AlertService;
/**
 * @author lucyf
 * @version 2017.5.10
 * 处理警报请求Action
 * **/
public class AlertAction {
	
	private AlertService alertService = null;
	private String name;
	private String point;
	private String datatype;
	private String min="0";
	private String max="0";
	private String platform;
	public void setAlertService(AlertService alertService) {
		this.alertService = alertService;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/**
	 * 保存警报到数据库
	 * @return 返回值为空
	 * **/
	public String save() throws IOException{
		Alert alert = new Alert();
		Timestamp date = new Timestamp(new Date().getTime());
		alert.setName(name);
		alert.setPoint(point);
		alert.setDatatype(datatype);
		alert.setMin(Integer.parseInt(min));
		alert.setMax(Integer.parseInt(max));
		alert.setDate(date);
		alertService.save(alert);
		if(platform.equals("app")){
			String callback = ServletActionContext.getRequest().getParameter("jsoncallback");			
			ServletActionContext.getResponse().getWriter().write(callback+"({flag:true})");
			return null;
		}else{
			
			return null;
		}
	}
	/**
	 * 核对警报命名是否规范
	 * **/
	public String checkname() throws IOException{
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		Alert alert = alertService.find(name);
		
		if(alert!=null){
			ServletActionContext.getResponse().getWriter().write("请重新命名!");
		}
		return null;
	}
	/**
	 * 从数据库加载警报
	 * **/
	public String load() throws IOException{
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		List<Alert> list = alertService.find();
		if(list!=null){
			JSONArray array = JSONArray.fromObject(list);
			if(platform.equals("app")){
				String callback = ServletActionContext.getRequest().getParameter("jsoncallback");			
				ServletActionContext.getResponse().getWriter().write(callback+"("+array+")");
				return null;
			}else{
				
				ServletActionContext.getResponse().getWriter().write(array.toString());
				return null;
			}
		}
		return null;
	}
	/**
	 * 从数据库中删除警报
	 * **/
	public String delete() throws IOException{
		if(name!=null){		
			alertService.detele(name);
		
		if(platform.equals("app")){
			String callback = ServletActionContext.getRequest().getParameter("jsoncallback");			
			ServletActionContext.getResponse().getWriter().write(callback+"({flag:true})");
			return null;
		}else{
			
			return null;
		}
		}
		return null;
	}
}
