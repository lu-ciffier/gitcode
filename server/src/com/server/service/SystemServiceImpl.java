package com.server.service;

import com.server.dao.SystemConfigDao;
import com.server.jopo.SystemConfig;

/*
 * SystemServiceImpl
 * */
public class SystemServiceImpl implements SystemConfigService{
	private SystemConfigDao configDao = null;
	public void setConfigDao(SystemConfigDao configDao) {
		this.configDao = configDao;
	}
	public boolean saveorupdate(SystemConfig system) {	
		return configDao.saveorupdate(system);
	}

	public SystemConfig find(String username) {
		SystemConfig system = null;
		system = configDao.find(username);
		return system;		 
	}

}
