package com.server.dao;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.server.jopo.User;


public class UserDaoImpl implements UserDao {
	private HibernateTemplate hibernateTemplate = null;
	private boolean user_flag=false;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	//注册
	public boolean regist(String username, String password) {

		Date newdate = new Date();
		User user = find(username);
		if(user==null){
			hibernateTemplate.save(new User(username, password, newdate));
			user_flag = true;
		}else {			
			user_flag = false;
		}
		return user_flag;
	}
	//登录
	public boolean login(String username, String password) {
		
		User user = find(username,password);
		if(user==null){
			user_flag = false;
		}else {
			user_flag = true;
		}		
		return user_flag;
	}
	//用户名查找
	public User find(String username){
		String queryString = "from User u where u.username='"+username+"'";
		@SuppressWarnings("unchecked")
		List<User> list = hibernateTemplate.find(queryString);
		if(list.isEmpty()==false){
			if(list.size()==1){
				return list.get(0);
			}else {
				return null;
			}
		}else {
			return null;		
		}
	}
	//用户名密码查询
	public User find(String username,String password){
		String queryString = "from User u where u.username='"+username+"'and u.password='"+password+"'";
		@SuppressWarnings("unchecked")
		List<User> list = hibernateTemplate.find(queryString);
		if(list.isEmpty()==false){
			if(list.size()==1){
				return list.get(0);
			}else {
				return null;
			}
		}else {
			return null;		
		}
	}
}
