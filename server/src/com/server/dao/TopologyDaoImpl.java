package com.server.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.server.jopo.Topology;

public class TopologyDaoImpl implements TopologyDao{

	private HibernateTemplate hibernateTemplate = null;
	private boolean flag=false;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	@Override
	public boolean save(Topology topology) {
		if(topology!=null){
			Topology t = findByPoint(topology.getPoint());
			if(t==null){
				hibernateTemplate.save(topology);
			}
			flag = true;
		}
		return flag;
	}

	@Override
	public List<Topology> find() {
		String hql = "from Topology";
		@SuppressWarnings("unchecked")
		List<Topology> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;		
		}
	}
	public Topology findByPoint(String point) {
		String hql = "from Topology t where t.point = '"+point+"'";
		@SuppressWarnings("unchecked")
		List<Topology> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list.get(0);
		}else {
			return null;		
		}
	}
	@Override
	public boolean deleteAll() {
		String hql = "from Topology";
		@SuppressWarnings("unchecked")
		List<Topology> list = hibernateTemplate.find(hql);
		hibernateTemplate.deleteAll(list);	
		return true;
	}

}
