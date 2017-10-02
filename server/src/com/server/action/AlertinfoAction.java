package com.server.action;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.server.jopo.Alertinfo;
import com.server.service.AlertinfoService;
/**
 * @author lucyf
 * @version 2017.5.10
 * 处理警报信息请求Action
 * **/
public class AlertinfoAction {

	private AlertinfoService alertinfoService = null;
	private String point;
	public void setAlertinfoService(AlertinfoService alertinfoService) {
		this.alertinfoService = alertinfoService;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	/**
	 * 获取警报信息
	 * **/
	public String getAlert() throws IOException{
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		List<Alertinfo> list = alertinfoService.find();
		if(list!=null){
			JSONArray array = JSONArray.fromObject(list);
			ServletActionContext.getResponse().getWriter().write(array.toString());
		}else{
			ServletActionContext.getResponse().getWriter().write("");
		}
		return null;
	}
	/**
	 * 删除警报信息
	 * **/
	public String delete() throws IOException{
		if(point!=null){
			
			alertinfoService.delete(point);
			ServletActionContext.getResponse().getWriter().write("ok");
		}
		return null;
	}
}
