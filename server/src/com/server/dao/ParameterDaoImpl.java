package com.server.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.server.jopo.Parameter;

/*
 * ParameterDaoImpl
 * */
public class ParameterDaoImpl implements ParameterDao{
	private HibernateTemplate hibernateTemplate = null;
	private boolean parameter_flag=false;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public boolean save(Parameter parameter) {
		if(parameter!=null){
						
			hibernateTemplate.save(parameter);		
			parameter_flag = true;
			
		}
		return parameter_flag;
	}

	/**
	 * 查找Parameter所有数据
	 * */
	public List<Parameter> findAll() {
		String hql = "from Parameter p ";
		@SuppressWarnings("unchecked")
		List<Parameter> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;	
		}
	}
	/**
	 * 按用户名查找Parameter数据
	 * */
	public List<Parameter> findByUsername(String username) {
		String hql = "from Parameter p where p.username='"+username+"'";
		@SuppressWarnings("unchecked")
		List<Parameter> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;	
		}
	}
	/**
	 * 按节点地址查找Parameter数据
	 * */
	public List<Parameter> findByPoint(String point) {
		String hql = "from Parameter p where p.point='"+point+"'";
		@SuppressWarnings("unchecked")
		List<Parameter> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;	
		}
	}
	public List<Parameter> findByPointnum(String pointnum) {
		String hql = "from Parameter p where p.pointnum='"+pointnum+"'";
		@SuppressWarnings("unchecked")
		List<Parameter> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;	
		}
	}
	/**
	 * 按时间查找Parameter数据
	 * time格式: 2016-01-12 15:11:11
	 * */
	public List<Parameter> findByTime(Timestamp from,Timestamp to) {
		String hql = "from Parameter p where p.date between '"+from+"'"+"and '"+to+"'";
		@SuppressWarnings("unchecked")
		List<Parameter> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			return list;
		}else {
			return null;	
		}
	}

	public boolean delete_by_user(String username) {
		String hql = "from Parameter p where p.username='"+username+"'";
		@SuppressWarnings("unchecked")
		List<Parameter> list = hibernateTemplate.find(hql);
		if(list.isEmpty()==false){
			hibernateTemplate.deleteAll(list);
			return true;
		}else {
			return false;		
		}
	}

}
