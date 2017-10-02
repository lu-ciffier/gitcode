package com.server.dao;

import java.util.List;

import com.server.jopo.Alertinfo;

public interface AlertinfoDao {
	
	public boolean save(Alertinfo alert);
	public boolean deleteAll();
	public boolean delete(String point);
	public List<Alertinfo> find();
	public List<Alertinfo> find(String point);

}
