package com.server.dao;

import java.sql.Timestamp;
import java.util.List;

import com.server.jopo.Parameter;


public interface ParameterDao {
	public boolean save(Parameter parameter);
	public List<Parameter> findAll();
	public List<Parameter> findByUsername(String username);
	public List<Parameter> findByPoint(String point);
	public List<Parameter> findByPointnum(String pointnum);
	public List<Parameter> findByTime(Timestamp from,Timestamp to);
	
	public boolean delete_by_user(String username);
}
