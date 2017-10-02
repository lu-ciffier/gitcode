package com.server.service;

import com.server.jopo.User;

public interface UserService {
	
	public boolean regist(String username, String password);
	public boolean login(String username, String password);
	public User find(String username);

}
