package com.server.action;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;

import com.server.jopo.Parameter;
import com.server.jopo.Topology;
import com.server.service.ParameterService;
import com.server.service.TopologyService;
/**
 * @author lucyf
 * @version 2017.5.10
 * 处理节点拓扑请求Action
 * **/
public class TopologyAction {

	private TopologyService topologyService = null;
	private ParameterService parameterService = null;
	private Map<Integer, String> father = new HashMap();
	private Map<Integer, String> childen = new HashMap();
	private String point;
	private String root = null;
	private String result = "";
	private String platform;

	public void setTopologyService(TopologyService topologyService) {
		this.topologyService = topologyService;
	}
	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/*
	 * 获取节点拓扑结构数据
	 */
	public String getTopo() throws IOException{
		long start = System.currentTimeMillis();
		
		List<Topology> list = topologyService.find();
		System.out.println("tpopsise:"+list.size());
		int i = 1;
		int j = 1;
		if (list!=null) {
			for(Topology p:list){
				if(p.getPoint()!=""&&p.getPoint()!=null){
					if(!(father.containsValue(p.getFpoint()))){						
						father.put(i++, p.getFpoint());
					}
					childen.put(j++, p.getPoint());
				}
			}
			getRoot(list);
			Collection<String> fathers = father.values();
			JSONObject ob = new JSONObject();
			ob.put("root", root);
			for(String f : fathers){
				ob.put(f, getChild(f,list));
			}
			if(platform.equals("app")){
				String callback = ServletActionContext.getRequest().getParameter("jsoncallback");			
				ServletActionContext.getResponse().getWriter().write(callback+"("+ob+")");
			}else{
				
				ServletActionContext.getResponse().getWriter().write(ob.toString());
			}
		}
		System.out.println("拓扑请求耗时："+(System.currentTimeMillis()-start)+"秒");
		return null;
	}
	/*
	 * 获取节点最新数据
	 */
	public String getParameter() throws IOException{
		List<Parameter> list = parameterService.findByPoint(point);
		Parameter p = list.get(list.size()-1);
		JSONObject object = JSONObject.fromObject(p);
		ServletActionContext.getResponse().getWriter().write(object.toString());
		return null;
	}
	/*
	 * 获取根节点
	 */
	public String getRoot(List<Topology> list){
		Collection<String> fathers = father.values();
		for(String s :fathers){
			if(!(childen.containsValue(s))){
					root = s;
					result = s +"<br>";
					return s;				
			}
		}
		return null;
	}
	/*
	 * 获取子节点
	 */
	public String getChild(String father,List<Topology> list){
		String childs = "";
		if(father!=null&&father!=""){		
			for(Topology p:list){
				if(p.getFpoint().equals(father)){
					if(childs!=""){
						childs = childs +",";
					}
					childs = childs + p.getPoint() + p.getQuality();
				}
			}			
		}
		return childs;
	}
}
