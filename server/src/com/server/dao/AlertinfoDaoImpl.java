package com.server.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.server.jopo.Alertinfo;

public class AlertinfoDaoImpl implements AlertinfoDao{
	
	private HibernateTemplate hibernateTemplate = null;
	private boolean alertinfo_flag = false;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public boolean save(Alertinfo alert) {
		if(alert!=null){
			Alertinfo a = findname(alert.getName());
			if(a!=null){
				/*a.setReason(alert.getReason());
				hibernateTemplate.update(a);
				alertinfo_flag = true;*/
			}else{
				hibernateTemplate.save(alert);
				alertinfo_flag = true;
			}
		}
		return alertinfo_flag;
	}

	@Override
	public boolean deleteAll() {
		String hql = "from Alertinfo";
		@SuppressWarnings("unchecked")
		List<Alertinfo> list =  hibernateTemplate.find(hql);
		hibernateTemplate.deleteAll(list);
		return true;
	}

	@Override
	public boolean delete(String point) {
		List<Alertinfo> alerts = find(point);
		if(alerts!=null){
			for(Alertinfo info:alerts){
				
				hibernateTemplate.delete(info);
			}
			return true;
		}else{			
			return false;
		}
	}

	@Override
	public List<Alertinfo> find() {
		String hql = "from Alertinfo";
		@SuppressWarnings("unchecked")
		List<Alertinfo> list =  hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;		
		}
	}

	@Override
	public List<Alertinfo> find(String point) {
		String hql = "from Alertinfo a where a.point = '"+point+"'";
		@SuppressWarnings("unchecked")
		List<Alertinfo> list =  hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;		
		}
	}
	public Alertinfo findname(String name) {
		String hql = "from Alertinfo a where a.name = '"+name+"'";
		@SuppressWarnings("unchecked")
		List<Alertinfo> list =  hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list.get(0);
		}else {
			return null;		
		}
	}

}
