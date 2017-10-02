package com.server.service;

import java.util.List;

import com.server.jopo.Alertinfo;

public interface AlertinfoService {
	public boolean save(Alertinfo alert);
	public boolean deleteAll();
	public boolean delete(String point);
	public List<Alertinfo> find();
	public List<Alertinfo> find(String point);

}
