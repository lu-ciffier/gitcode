package com.server.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.server.jopo.SystemConfig;

/*
 * SystemDaoImpl
 * */
public class SystemConfigDaoImpl implements SystemConfigDao{
	
	private HibernateTemplate hibernateTemplate = null;
	private boolean system_flag=false;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	public boolean saveorupdate(SystemConfig system) {
		
		SystemConfig config = find(system.getType());
		if(config==null){
			/*SystemConfig admin = find("admin");
			admin.setPort(system.getPort());
			admin.setBotelv(system.getBotelv());
			admin.setDate(system.getDate());*/
			hibernateTemplate.save(config);
			system_flag = true;
		}else{
			config.setType(system.getType());
			config.setPort(system.getPort());
			config.setBotelv(system.getBotelv());
			hibernateTemplate.update(config);
			system_flag = true;		
		}
		return system_flag;
	}

	public List<SystemConfig> select() {
		String queryString = "from SystemConfig";
		@SuppressWarnings("unchecked")
		List<SystemConfig> list = hibernateTemplate.find(queryString);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;		
		}			
	}
	public SystemConfig find(String type){
		String hql = "from SystemConfig s where s.type='"+type+"'";
		@SuppressWarnings("unchecked")
		List<SystemConfig> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list.get(0);
		}else {
			return null;		
		}
	}

}
