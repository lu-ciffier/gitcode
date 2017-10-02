package com.server.service;

import java.util.List;

import com.server.dao.AlertDao;
import com.server.jopo.Alert;

public class AlertServiceImpl implements AlertService{
	
	private AlertDao alertDao = null;
	public void setAlertDao(AlertDao alertDao) {
		this.alertDao = alertDao;
	}

	@Override
	public boolean save(Alert alert) {
		if(alert!=null){
			return alertDao.save(alert);
		}else{
			return false;
		}
	}

	@Override
	public List<Alert> find() {
		
		return alertDao.find();
	}

	@Override
	public Alert find(String name) {
		
		return alertDao.find(name);
	}

	@Override
	public boolean detele(String name) {
		
		return alertDao.delete(name);
	}

}
