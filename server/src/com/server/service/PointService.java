package com.server.service;

import java.util.List;

import com.server.jopo.Point;


public interface PointService {
	public boolean save(List<Point> list);
	public List<Point> find();
}
