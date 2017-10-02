package com.server.sessiton;

import java.sql.Timestamp;
import java.util.Date;

import com.server.jopo.Parameter;

public class TestData {
	public static Parameter getParameter(String data){
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
		System.out.println("空气温度:"+air_tem);
		
		// Air Humidity 空气湿度		
		String air_hum_s = data.substring(32, 34);
		int air_hum_i = Integer.parseInt(air_hum_s,16);
		Integer air_hum = air_hum_i;
		System.out.println("空气湿度:"+air_hum);
		
		// Illumination 光敏
		String ill_s = data.substring(34, 38);
		Integer ill = Integer.parseInt(ill_s, 16);
		System.out.println("光敏:"+ill);
		
		// co2 二氧化碳浓度
		String co2_s = data.substring(38, 42);
		int co2_i = Integer.parseInt(co2_s,16);
		Integer co2 = co2_i;
		System.out.println("二氧化碳浓度:"+co2);
		
		
		// Soil Temperature 土壤温度	
		String soil_tem_s = data.substring(42,46);
		int soil_tem_i = Integer.parseInt(soil_tem_s,16);		
		Float soil_tem = (float) (soil_tem_i/10);
		System.out.println("土壤温度:"+soil_tem);
		
		// Soil Humidity 土壤湿度
		String soil_hum_s = data.substring(46,48);
		int soil_hum_i = Integer.parseInt(soil_hum_s,16);
		Integer soil_hum = soil_hum_i;
		System.out.println("土壤湿度:"+soil_hum);
		
		//电压
		String u = data.substring(48, 50);
		Float voltage = (float) (Integer.parseInt(u, 16))/10;
		System.out.println("电压:"+voltage);
		
		//date
		Timestamp date = new Timestamp(new Date().getTime());
		
		parameter.setUsername("admin");
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
	public static void main(String[] args) {
		
		Parameter pa = new Parameter();
		String s = "26525341259F3E01004B1200143E00CA3605F70000FE70FB2978787878787E2A";
		pa = getParameter(s);
	}
}
