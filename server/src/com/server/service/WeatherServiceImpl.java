package com.server.service;

import java.util.List;

import com.server.dao.WeatherDao;
import com.server.jopo.Weather;

public class WeatherServiceImpl implements WeatherService{

	private WeatherDao weatherDao = null;
	public void setWeatherDao(WeatherDao weatherDao) {
		this.weatherDao = weatherDao;
	}
	
	@Override
	public List<Weather> findAll() {
		// TODO Auto-generated method stub
		return weatherDao.findAll();
	}

	@Override
	public List<Weather> findByPointnum(String pointnum) {
		// TODO Auto-generated method stub
		return weatherDao.findByPointnum(pointnum);
	}

	@Override
	public boolean deleteAll(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteByPointNum(String pointnum) {
		// TODO Auto-generated method stub
		return false;
	}

}
