package com.server.service;

import java.sql.Timestamp;
import java.util.List;

import com.server.jopo.Parameter;


public interface ParameterService {
	public boolean save(List<String> list,String username);
	public Parameter getParameter(String data,String username);
	
	public List<Parameter> findAll();
	public List<Parameter> findByUsername(String username);
	public List<Parameter> findByPoint(String point);
	public List<Parameter> findByPointnum(String pointnum);
	public List<Parameter> findByTime(Timestamp from,Timestamp to);
	public boolean delete_by_user(String username);
}
