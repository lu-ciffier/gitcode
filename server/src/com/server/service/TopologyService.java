package com.server.service;

import java.util.List;

import com.server.jopo.Topology;

public interface TopologyService {

	public boolean save(Topology topology);
	public List<Topology> find();
}
