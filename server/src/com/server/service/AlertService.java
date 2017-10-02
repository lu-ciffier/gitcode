package com.server.service;

import java.util.List;

import com.server.jopo.Alert;

public interface AlertService {

	public boolean save(Alert alert);
	public List<Alert> find();
	public Alert find(String name);
	public boolean detele(String name);
}
