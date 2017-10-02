package com.server.service;

import com.server.jopo.SystemConfig;


public interface SystemConfigService {
	
	public boolean saveorupdate(SystemConfig system);
	public SystemConfig find(String username);
}
