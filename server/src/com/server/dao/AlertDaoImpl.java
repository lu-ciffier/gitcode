package com.server.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.server.jopo.Alert;

public class AlertDaoImpl implements AlertDao{
	
	private HibernateTemplate hibernateTemplate = null;
	private boolean alert_flag = false;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public boolean save(Alert alert) {
		if(alert!=null){
			Alert a = find(alert.getName());
			if(a!=null){
				a.setDatatype(alert.getDatatype());
				hibernateTemplate.update(a);
				alert_flag = true;
			}else{
				hibernateTemplate.save(alert);
				alert_flag = true;
			}
		}
		return alert_flag;
	}

	@Override
	public boolean deleteAll() {
		String hql = "from Alert";
		@SuppressWarnings("unchecked")
		List<Alert> list =  hibernateTemplate.find(hql);
		hibernateTemplate.deleteAll(list);
		return true;
	}

	@Override
	public boolean delete(String name) {
		Alert alert = find(name);
		if(alert!=null){
			hibernateTemplate.delete(alert);
			return true;
		}else{			
			return false;
		}
	}

	@Override
	public List<Alert> find() {
		String hql = "from Alert";
		@SuppressWarnings("unchecked")
		List<Alert> list =  hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;		
		}
	}

	@Override
	public Alert find(String name) {
		String hql = "from Alert a where a.name = '"+name+"'";
		@SuppressWarnings("unchecked")
		List<Alert> list =  hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list.get(0);
		}else {
			return null;		
		}
	}

	@Override
	public List<Alert> findBypoint(String point) {
		String hql = "from Alert a where a.point = '"+point+"'";
		@SuppressWarnings("unchecked")
		List<Alert> list =  hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;		
		}
	}

}
