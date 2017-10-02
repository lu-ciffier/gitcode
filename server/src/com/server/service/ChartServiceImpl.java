package com.server.service;

import java.util.List;

import com.server.dao.ChartDao;
import com.server.jopo.Chart;

public class ChartServiceImpl implements ChartService{
	
	private ChartDao chartDao = null;
	public void setChartDao(ChartDao chartDao) {
		this.chartDao = chartDao;
	}

	@Override
	public boolean save(Chart chart) {
		if(chart!=null){
			return chartDao.save(chart);
		}else{
			return false;
		}
	}


	@Override
	public Chart find(String name) {
		
		return chartDao.find(name);
	}

	@Override
	public boolean detele(String name) {
		
		return chartDao.delete(name);
	}

	@Override
	public List<Chart> findByUserName(String username) {
		
		return chartDao.findByUserName(username);
	}

}
