package com.server.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.server.jopo.Device;

public class DeviceDaoImpl implements DeviceDao{

	private HibernateTemplate hibernateTemplate = null;
	private boolean device_flag=false;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	@Override
	public boolean save(Device device) {
		if(device!=null){
			Device d = find(device.getAddress());
			if(d!=null){
				d.setAddress(device.getAddress());
				d.setXposition(device.getXposition());
				d.setYposition(device.getYposition());
				d.setZposition(device.getZposition());
				hibernateTemplate.update(d);
				device_flag = true;
			}else{				
				hibernateTemplate.save(device);
				device_flag = true;
			}
		}
		return device_flag;
	}

	@Override
	public boolean deleteAll() {
		String hql = "from Device";
		@SuppressWarnings("unchecked")
		List<Device> list =  hibernateTemplate.find(hql);
		hibernateTemplate.deleteAll(list);
		return true;
	}

	@Override
	public List<Device> find() {
		String hql = "from Device";
		@SuppressWarnings("unchecked")
		List<Device> list =  hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;		
		}
	}
	public Device find(String address) {
		String hql = "from Device d where d.address = '"+address+"'";
		@SuppressWarnings("unchecked")
		List<Device> list =  hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list.get(0);
		}else {
			return null;		
		}
	}
	public Device find(String x,String y,String z) {
		String hql = "from Device d where d.xposition = '"+Integer.parseInt(x)
				+"' and d.yposition = '"+Integer.parseInt(y)
				+"' and d.zposition = '"+Integer.parseInt(z)+"'";
		@SuppressWarnings("unchecked")
		List<Device> list =  hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list.get(0);
		}else {
			return null;		
		}
	}

}
