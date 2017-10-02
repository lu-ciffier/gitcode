package com.server.dao;

import java.util.List;

import com.server.jopo.Chart;

public interface ChartDao {
	
	public boolean save(Chart chart);
	public boolean deleteAll();
	public boolean delete(String name);
	public List<Chart> findByUserName(String username);
	public Chart find(String name);

}
