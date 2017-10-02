package com.server.dao;

import java.util.List;

import com.server.jopo.Device;

public interface DeviceDao {

	public boolean save(Device device);
	public boolean deleteAll();
	public List<Device> find();
	public Device find(String address);
	public Device find(String x,String y,String z);
}
