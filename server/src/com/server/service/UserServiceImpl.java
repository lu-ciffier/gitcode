package com.server.service;

import com.server.dao.UserDao;
import com.server.jopo.User;


public class UserServiceImpl implements UserService{
	
	private UserDao userDao= null;
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public boolean regist(String username, String password) {		
		
		return userDao.regist(username, password);
	}

	public boolean login(String username, String password) {
		
		return userDao.login(username, password);
	}

	public User find(String username) {		
		return userDao.find(username);
	}

}
