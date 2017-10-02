package com.server.service;

import java.util.List;

import com.server.jopo.Chart;

public interface ChartService {

	public boolean save(Chart chart);
	public List<Chart> findByUserName(String username);
	public Chart find(String name);
	public boolean detele(String name);
}
