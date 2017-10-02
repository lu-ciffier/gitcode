package com.server.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.server.jopo.Point;

/*
 * PointDaoImpl
 * */
public class PointDaoImpl implements PointDao{
	private HibernateTemplate hibernateTemplate = null;
	private boolean point_flag=false;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public boolean save(List<Point> list) {
		if(!list.isEmpty()){			
			for(Point point:list){			
				hibernateTemplate.save(point);
				point_flag = true;						
			}
		}
		return point_flag;
	}


	public List<Point> find() {
		String hql = "from Point";
		@SuppressWarnings("unchecked")
		List<Point> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;		
		}
	}
	public boolean deleteAll(){
		
		String hql = "from Point";
		@SuppressWarnings("unchecked")
		List<Point> list = hibernateTemplate.find(hql);
		hibernateTemplate.deleteAll(list);			
		return true;
	}
}
