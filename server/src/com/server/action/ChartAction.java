package com.server.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONArray;

import com.opensymphony.xwork2.ActionContext;
import com.server.jopo.Chart;
import com.server.service.ChartService;
/**
 * @author lucyf
 * @version 2017.5.10
 * 处理图表请求Action
 * **/
public class ChartAction {
	
	private ChartService chartService = null;
	private String username;
	private String chartname;
	private String point;
	private String datatype;
	private String platform;
	public void setChartService(ChartService chartService) {
		this.chartService = chartService;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setChartname(String chartname) {
		this.chartname = chartname;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/*
	 * 保存图表
	 */
	public String save(){
		Chart chart = new Chart();
		Timestamp date = new Timestamp(new Date().getTime());
		
		if(platform.equals("web")){//ionic app		
			Map<String, Object> session = ActionContext.getContext().getSession();
			username = (String) session.get("username");
		}
		chart.setUsername(username);
		chart.setName(chartname);
		chart.setPoint(point);
		chart.setDatatype(datatype);
		chart.setDate(date);
		chartService.save(chart);
		return null;
	}
	/*
	 * 加载图表
	 * */
	public String load() throws IOException{
		if(platform.equals("web")){			
			ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			Map<String, Object> session = ActionContext.getContext().getSession();
			username = (String) session.get("username");
			List<Chart> list = chartService.findByUserName(username);
			JSONArray array = JSONArray.fromObject(list);
			ServletActionContext.getResponse().getWriter().write(array.toString());
			return null;
		}
		return null;
	}
	/*
	 * 删除图表
	 */
	public String delete(){
		if(chartname!=null){		
			chartService.detele(chartname);
		}
		return null;
	}
}
