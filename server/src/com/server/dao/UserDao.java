package com.server.dao;

import com.server.jopo.User;

public interface UserDao {
	
	public boolean regist(String username,String password);
	public boolean login(String username,String password);
	public User find(String username);
}
