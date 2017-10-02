package com.server.dao;

import java.util.List;

import com.server.jopo.Weather;

public interface WeatherDao {

	public boolean save(Weather weather);
	public List<Weather> findAll();
	public List<Weather> findByPointnum(String pointnum);
}
