package com.server.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.server.dao.ParameterDao;
import com.server.jopo.Parameter;

/**
 * ParameterServiceImpl
 * */
public class ParameterServiceImpl implements ParameterService{
	private ParameterDao parameterDao = null;
	public void setParameterDao(ParameterDao parameterDao) {
		this.parameterDao = parameterDao;
	}

	public boolean save(List<String> list,String username) {
		boolean save_flag = false;
		for(String s :list){
			parameterDao.save(getParameter(s,username));
			save_flag = true;
		}
		return save_flag;
	}

	public List<Parameter> findAll() {
		return parameterDao.findAll();
	}
	public List<Parameter> findByUsername(String username) {
		return parameterDao.findByUsername(username);
	}
	public List<Parameter> findByPoint(String point) {
		return parameterDao.findByPoint(point);
	}
	public List<Parameter> findByPointnum(String pointnum) {
		return parameterDao.findByPointnum(pointnum);
	}
	public List<Parameter> findByTime(Timestamp from,Timestamp to) {
		return parameterDao.findByTime(from, to);
	}
	
	public Parameter getParameter(String data,String username){
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

	public boolean delete_by_user(String username) {
		
		return parameterDao.delete_by_user(username);
	}
}
