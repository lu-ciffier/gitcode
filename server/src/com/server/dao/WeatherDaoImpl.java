package com.server.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.server.jopo.Weather;

public class WeatherDaoImpl implements WeatherDao {
	
	private HibernateTemplate hibernateTemplate = null;
	private boolean weather_flag=false;
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public boolean save(Weather weather) {
		if(weather!=null){
			hibernateTemplate.save(weather);
			weather_flag = true;
		}
		return weather_flag;
	}

	@Override
	public List<Weather> findAll() {
		String hql = "from Weather w ";
		@SuppressWarnings("unchecked")
		List<Weather> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;	
		}
	}

	@Override
	public List<Weather> findByPointnum(String pointnum) {
		String hql = "from Weather w where w.pointNum='"+pointnum+"'";
		@SuppressWarnings("unchecked")
		List<Weather> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;	
		}
	}

}
