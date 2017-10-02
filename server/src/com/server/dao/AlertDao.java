package com.server.dao;

import java.util.List;

import com.server.jopo.Alert;

public interface AlertDao {

	public boolean save(Alert alert);
	public boolean deleteAll();
	public boolean delete(String name);
	public List<Alert> find();
	public Alert find(String name);
	public List<Alert> findBypoint(String point);
}
