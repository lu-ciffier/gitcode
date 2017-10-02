package com.server.service;

import java.util.List;

import com.server.jopo.Device;

public interface DeviceService {
	
	public boolean save(Device device);
	public List<Device> find();
	public Device find(String address);
	public Device find(String x, String y, String z);

}
