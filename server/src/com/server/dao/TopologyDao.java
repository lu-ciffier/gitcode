package com.server.dao;

import java.util.List;

import com.server.jopo.Topology;

public interface TopologyDao {

	public boolean save(Topology topology);
	public boolean deleteAll();
	public List<Topology> find();

}
