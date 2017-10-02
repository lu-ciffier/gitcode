package com.server.jopo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lucyf
 * @version 2017.5.10
 * Parameter entity. @author MyEclipse Persistence Tools
 */

public class Parameter implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String username;
	private String pointnum;
	private String point;
	private Float airT;
	private Integer airH;
	private Integer ill;
	private Integer co2;
	private Float soilT;
	private Integer soilH;
	private Float voltage;
	private Timestamp date;

	// Constructors

	/** default constructor */
	public Parameter() {
	}

	/** full constructor */
	public Parameter(String username, String pointnum, String point,
			Float airT, Integer airH, Integer ill, Integer co2, Float soilT,
			Integer soilH, Float voltage, Timestamp date) {
		this.username = username;
		this.pointnum = pointnum;
		this.point = point;
		this.airT = airT;
		this.airH = airH;
		this.ill = ill;
		this.co2 = co2;
		this.soilT = soilT;
		this.soilH = soilH;
		this.voltage = voltage;
		this.date = date;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPointnum() {
		return this.pointnum;
	}

	public void setPointnum(String pointnum) {
		this.pointnum = pointnum;
	}

	public String getPoint() {
		return this.point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public Float getAirT() {
		return this.airT;
	}

	public void setAirT(Float airT) {
		this.airT = airT;
	}

	public Integer getAirH() {
		return this.airH;
	}

	public void setAirH(Integer airH) {
		this.airH = airH;
	}

	public Integer getIll() {
		return this.ill;
	}

	public void setIll(Integer ill) {
		this.ill = ill;
	}

	public Integer getCo2() {
		return this.co2;
	}

	public void setCo2(Integer co2) {
		this.co2 = co2;
	}

	public Float getSoilT() {
		return this.soilT;
	}

	public void setSoilT(Float soilT) {
		this.soilT = soilT;
	}

	public Integer getSoilH() {
		return this.soilH;
	}

	public void setSoilH(Integer soilH) {
		this.soilH = soilH;
	}

	public Float getVoltage() {
		return this.voltage;
	}

	public void setVoltage(Float voltage) {
		this.voltage = voltage;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	// 把数据格式化为单独的一条结果
	public List<String> format(String data) {
		List<String> list = new ArrayList<String>();
		String[] datas = data.split("<br>");
		for (int i = 0; i < datas.length; i++) {
			list.add(datas[i]);
		}
		return list;
	}

	// 从结果中解析出参数数据
	public Parameter getParameter(String data){
		Parameter parameter = new Parameter();
		if (data.startsWith("26525341")) {
		//26525341 D3A13E01004B1200 0001   00D9 2C 7878 02AC 00B4 32 2A   7878787878022A
		
		// pointnum 节点代号
		String pointnum = data.substring(8, 10);
		
		// point节点
		String point = data.substring(8, 24);
			
		// Air Temperature 空气温度
		String air_tem_s = data.substring(28, 32);
		int air_tem_i = Integer.parseInt(air_tem_s,16);
		Float air_tem = (float) (air_tem_i/10);
		if(air_tem>150||air_tem<-150){
			parameter = null;
			return parameter;
		}
		
		// Air Humidity 空气湿度		
		String air_hum_s = data.substring(32, 34);
		int air_hum_i = Integer.parseInt(air_hum_s,16);
		Integer air_hum = air_hum_i;
		if(air_hum>100||air_hum<0){
			parameter = null;
			return parameter;
		}
		
		// Illumination 光敏
		String ill_s = data.substring(34, 38);
		Integer ill = Integer.parseInt(ill_s, 16);
		if(ill>65536||ill<=0){
			parameter = null;
			return parameter;
		}
		
		// co2 二氧化碳浓度
		String co2_s = data.substring(38, 42);
		int co2_i = Integer.parseInt(co2_s,16);
		Integer co2 = co2_i;
		if(co2>10000||co2<0){
			parameter = null;
			return parameter;
		}
		
		
		// Soil Temperature 土壤温度	
		String soil_tem_s = data.substring(42,46);
		int soil_tem_i = Integer.parseInt(soil_tem_s,16);		
		Float soil_tem = (float) (soil_tem_i/10);
		if(soil_tem>150||soil_tem<-150){
			parameter = null;
			return parameter;
		}
		
		// Soil Humidity 土壤湿度
		String soil_hum_s = data.substring(46,48);
		int soil_hum_i = Integer.parseInt(soil_hum_s,16);
		Integer soil_hum = soil_hum_i;
		if(soil_hum>100||soil_hum<0){
			parameter = null;
			return parameter;
		}
		
		//电压
		String u = data.substring(48, 50);
		Float voltage = (float) (Integer.parseInt(u, 16))/10;
		if(voltage>5||voltage<0){
			parameter = null;
			return parameter;
		}
		
		//date
		Timestamp date = new Timestamp(new Date().getTime());
		
		parameter.setUsername(username);
		parameter.setPoint(point);
		parameter.setPointnum(pointnum);
		parameter.setIll(ill);
		parameter.setCo2(co2);
		parameter.setAirT(air_tem);
		parameter.setAirH(air_hum);
		parameter.setSoilT(soil_tem);
		parameter.setSoilH(soil_hum);
		parameter.setVoltage(voltage);
		parameter.setDate(date);
		} else {
			parameter = null;
		}
		return parameter;
	}
	// 从结果中解析出参数数据
	public Parameter testParameter(String data){
		Parameter parameter = new Parameter();
		if (data.startsWith("26525341")) {
		//26525341 D3A13E01004B1200 0001   00D9 2C 7878 02AC 00B4 32 2A   7878787878022A
		
		// pointnum 节点代号
		String pointnum = data.substring(8, 10);
		
		// point节点
		String point = data.substring(8, 24);
			
		// Air Temperature 空气温度
		String air_tem_s = data.substring(28, 32);
		int air_tem_i = Integer.parseInt(air_tem_s,16);
		Float air_tem = (float) (air_tem_i/10);
		
		
		// Air Humidity 空气湿度		
		String air_hum_s = data.substring(32, 34);
		int air_hum_i = Integer.parseInt(air_hum_s,16);
		Integer air_hum = air_hum_i;
		
		
		// Illumination 光敏
		String ill_s = data.substring(34, 38);
		Integer ill = Integer.parseInt(ill_s, 16);
		
		
		// co2 二氧化碳浓度
		String co2_s = data.substring(38, 42);
		int co2_i = Integer.parseInt(co2_s,16);
		Integer co2 = co2_i;
				
		// Soil Temperature 土壤温度	
		String soil_tem_s = data.substring(42,46);
		int soil_tem_i = Integer.parseInt(soil_tem_s,16);		
		Float soil_tem = (float) (soil_tem_i/10);
			
		// Soil Humidity 土壤湿度
		String soil_hum_s = data.substring(46,48);
		int soil_hum_i = Integer.parseInt(soil_hum_s,16);
		Integer soil_hum = soil_hum_i;
			
		//电压
		String u = data.substring(48, 50);
		Float voltage = (float) (Integer.parseInt(u, 16))/10;		
		
		//date
		Timestamp date = new Timestamp(new Date().getTime());
		
		parameter.setUsername(username);
		parameter.setPoint(point);
		parameter.setPointnum(pointnum);
		parameter.setIll(ill);
		parameter.setCo2(co2);
		parameter.setAirT(air_tem);
		parameter.setAirH(air_hum);
		parameter.setSoilT(soil_tem);
		parameter.setSoilH(soil_hum);
		parameter.setVoltage(voltage);
		parameter.setDate(date);
		} else {
			parameter = null;
		}
		return parameter;
	}
	//
	public List<Parameter> getParameters(String data) {
		List<String> list = format(data);
		List<Parameter> pList = new ArrayList<Parameter>();
		for (String s : list) {
			Parameter p = getParameter(s);
			if (p != null) {
				pList.add(p);
			}
		}
		return pList;
	}
public static void main(String[] args) {
	//26525341409E3E01004B1200000100DD3F038D00000A5A0F277878787878FF2A
	Parameter p = new Parameter();
	Parameter pa = new Parameter();
	//265253410300000000000000000100C83D000002547FFE26207878787878602A
	String s = "265253412A9F3E01004B1200000100B53E0000028E00C4312178787878783D2A";
	//pa = p.getParameter(s);
	pa = p.testParameter(s);
	System.out.println(pa.getAirH());
	System.out.println(pa.getAirT());
	System.out.println(pa.getSoilH());
	System.out.println(pa.getSoilT());
	System.out.println(pa.getIll());
	System.out.println(pa.getCo2());
}
}