package com.server.service;

import java.util.List;

import com.server.dao.DeviceDao;
import com.server.jopo.Device;

public class DeviceServiceImpl implements DeviceService{

	private DeviceDao deviceDao = null;
	public void setDeviceDao(DeviceDao deviceDao) {
		this.deviceDao = deviceDao;
	}
	@Override
	public boolean save(Device device) {
		if(device!=null){			
			return deviceDao.save(device);			 
		}else {
			return false;
		}
	}

	@Override
	public List<Device> find() {
		
		return deviceDao.find();
	}
	@Override
	public Device find(String address) {		
		return deviceDao.find(address);
	}
	@Override
	public Device find(String x,String y,String z) {		
		return deviceDao.find(x, y, z);
	}

}
