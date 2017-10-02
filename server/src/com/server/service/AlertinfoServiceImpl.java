package com.server.service;

import java.util.List;

import com.server.dao.AlertinfoDao;
import com.server.jopo.Alertinfo;

public class AlertinfoServiceImpl implements AlertinfoService{
	private AlertinfoDao alertinfoDao = null;
	public void setAlertinfoDao(AlertinfoDao alertinfoDao) {
		this.alertinfoDao = alertinfoDao;
	}

	@Override
	public boolean save(Alertinfo alert) {
		if(alert!=null){
			return alertinfoDao.save(alert);
		}else{
			return false;
		}
	}

	@Override
	public boolean deleteAll() {
		
		return alertinfoDao.deleteAll();
	}

	@Override
	public boolean delete(String point) {
		
		return alertinfoDao.delete(point);
	}

	@Override
	public List<Alertinfo> find() {
		return alertinfoDao.find();
	}

	@Override
	public List<Alertinfo> find(String point) {
		
		return alertinfoDao.find(point);
	}

}
