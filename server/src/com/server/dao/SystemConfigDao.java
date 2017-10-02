package com.server.dao;

import java.util.List;

import com.server.jopo.SystemConfig;


public interface SystemConfigDao {
	public boolean saveorupdate(SystemConfig system);
	public List<SystemConfig> select();
	public SystemConfig find(String type);
}
