package com.server.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.server.jopo.Chart;

public class ChartDaoImpl implements ChartDao{
	
	private HibernateTemplate hibernateTemplate = null;
	private boolean chart_flag = false;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public boolean save(Chart chart) {
		if(chart!=null){
			Chart c = find(chart.getName());
			if(c!=null){
				c.setDatatype(chart.getDatatype());
				hibernateTemplate.update(c);
				chart_flag = true;
			}else{
				hibernateTemplate.save(chart);
				chart_flag = true;
			}
		}
		return chart_flag;
	}

	@Override
	public boolean deleteAll() {
		String hql = "from Chart";
		@SuppressWarnings("unchecked")
		List<Chart> list =  hibernateTemplate.find(hql);
		hibernateTemplate.deleteAll(list);
		return true;
	}

	@Override
	public boolean delete(String name) {
		Chart chart = find(name);
		if(chart!=null){
			hibernateTemplate.delete(chart);
			return true;
		}else{			
			return false;
		}
	}	
	public Chart find(String name) {
		String hql = "from Chart c where c.name = '"+name+"'";
		@SuppressWarnings("unchecked")
		List<Chart> list =  hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list.get(0);
		}else {
			return null;		
		}
	}

	@Override
	public List<Chart> findByUserName(String username) {
		String hql = "from Chart c where c.username = '"+username+"'";
		@SuppressWarnings("unchecked")
		List<Chart> list =  hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;		
		}		
	}

}
