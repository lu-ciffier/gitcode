package com.server.service;

import java.util.List;

import com.server.dao.TopologyDao;
import com.server.jopo.Topology;

public class TopologyServiceImpl implements TopologyService{

	private TopologyDao topologyDao = null;
	
	public void setTopologyDao(TopologyDao topologyDao) {
		this.topologyDao = topologyDao;
	}
	@Override
	public boolean save(Topology topology) {
		boolean save_flag = false;
		if(topology!=null){						
			save_flag = topologyDao.save(topology);			
		}
		return save_flag;
	}

	@Override
	public List<Topology> find() {
		
		return topologyDao.find();
	}

}
