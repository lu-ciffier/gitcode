package com.server.service;

import java.util.List;

import com.server.jopo.Weather;

public interface WeatherService {
	public List<Weather> findAll();
	public List<Weather> findByPointnum(String pointnum);
	public boolean deleteAll(String username);
	public boolean deleteByPointNum(String pointnum);
}
