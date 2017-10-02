package com.server.action;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.server.jopo.Parameter;
import com.server.jopo.Point;
import com.server.service.PointService;
import com.server.thread.ClientSocket;
/**
 * @author lucyf
 * @version 2017.5.10
 * 处理实时数据请求Action
 * **/
public class ClientSocketAction{
	private String data;
	private StringBuilder portData;
	private String platform;
	private ClientSocket clintSocket = null;
	private PointService pointService = null;
	static String username = "";
	private static final String RND = "26524E4430303030303"
			+ "03030303030303030303030303030303030303030302A";
	//private static final String RND = "hello world";
	
	public void setData(String data) {
		this.data = data;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public void setClintSocket(ClientSocket clintSocket) {
		this.clintSocket = clintSocket;
	}
	public void setPointService(PointService pointService) {
		this.pointService = pointService;
	}
	public ClientSocketAction(){
		//portData ="";
		//portThread.addPortDataListener(RealDataAction.this);
		//System.out.println(hashCode()+":regist listner");
	}
	/*
	 * 初始化数据监听器
	 */
	public void init(){
		portData =new StringBuilder("");
		//platform = "web";
		//clintSocket.addPortDataListener(ClientSocketAction.this);
		//System.out.println(hashCode()+":regist listner");
	}
	/*
	 * 获取所有节点
	 */
	public String open() throws Exception{
		String command = RND;	
		//clintSocket.writePort(command);
		clintSocket.send(command);
		//26524E441BA23E01004B12003030524F5530307F9E3E01004B1200000030DC2A
		Thread.sleep(8000);
		List<Point> list = pointService.find();
		String point="";
		
		if(platform.equals("app")){//from ionic app
			JSONArray array = new JSONArray();
			if (list!=null) {
				for(Point p:list){
					if(p.getPoint()!=""&&p.getPoint()!=null){
						getTopology(p.getPoint());
						JSONObject ob = new JSONObject();
						ob.put("point",p.getPoint() );
						array.add(ob);
						Thread.sleep(200);
					}
				}
			}else {
				array = null;
			}
			String callback = ServletActionContext.getRequest().getParameter("jsoncallback");			
			ServletActionContext.getResponse().getWriter().write(callback+"("+array+")");
			return null;
		}else if(platform.equals("pc")){//from pc app
			if (list!=null) {
				for(Point p:list){
					if(p.getPoint()!=""&&p.getPoint()!=null){
						getTopology(p.getPoint());
						point = point+p.getPointnum()+","+p.getPoint()+"<br>";
						Thread.sleep(200);
					}
				}
			}else {
				point = "There is no point";
			}
			return point;
		}else if(platform.equals("ap")){//from android app		
			if (list!=null) {
				for(Point p:list){
					if(p.getPoint()!=""&&p.getPoint()!=null){
						getTopology(p.getPoint());
						point = point+p.getPointnum()+","+p.getPoint()+"<br>";
						Thread.sleep(200);
					}
				}
			}else {
				point = "There is no point";
			}
			ServletActionContext.getResponse().getWriter().write(point);
			return null;
		}else{
			if (list!=null) {
				for(Point p:list){
					if(p.getPoint()!=""&&p.getPoint()!=null){
						getTopology(p.getPoint());
						point = point+p.getPointnum()+","+p.getPoint()+"<br>";
						Thread.sleep(200);
					}
				}
			}else {
				point = "There is no point";
			}
			ServletActionContext.getResponse().getWriter().write(point);
			return null;
		}
		//return point;
	}
	/*
	 * 节点采集
	 */
	public String sendCommand() throws IOException{
		String command = this.data;
		//portThread.writePort(command);
		clintSocket.send(command);
		return null;
	}
	
	/*
	 * 获取网络拓扑
	 */
	public void getTopology(String point) throws IOException{
		String command = "26524E53"+point+"303030303030303030303030303030303030302A";
		//portThread.writePort(command);
		clintSocket.send(command);
	}
	/*
	 * 获取实时数据
	 */
	public String realData() throws Exception {
		
		if(platform.equals("app")){
			System.out.println("App realData:"+portData);
			String callback = ServletActionContext.getRequest().getParameter("jsoncallback");
			if(portData!=null){
				Parameter parameter = new Parameter();
				parameter.setUsername("admin");
				List<Parameter> list = parameter.getParameters(portData.toString());		
				if(!list.isEmpty()){			
					JSONArray array = list2json(list);
					portData.setLength(0);
					//return array.toString();
					ServletActionContext.getResponse().getWriter().write(callback+"("+array+")");
					return null;
				}
			}
			ServletActionContext.getResponse().getWriter().write(callback+"({})");
			return null;
			
		}else if(platform.equals("pc")){
			if(portData!=null){
				System.out.println("PC realData: "+portData);
				Parameter parameter = new Parameter();
				parameter.setUsername("admin");
				List<Parameter> list = parameter.getParameters(portData.toString());		
				if(!list.isEmpty()){			
					JSONArray array = list2json(list);
					portData.setLength(0);
					return array.toString();
				}
			}
			return null;
		}else{
			System.out.println("Web realData: "+portData);
			if(portData!=null){
				Parameter parameter = new Parameter();
				parameter.setUsername("admin");
				List<Parameter> list = parameter.getParameters(portData.toString());		
				if(!list.isEmpty()){			
					JSONArray array = list2json(list);
					ServletActionContext.getResponse().getWriter().write(array.toString());
					portData.setLength(0);
				}
			}else{
				ServletActionContext.getResponse().getWriter().write("");
			}
			return null;
		}
	}
	/*
	 * list转json
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
	 * 数据验证
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
