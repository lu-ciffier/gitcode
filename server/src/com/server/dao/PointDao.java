package com.server.dao;

import java.util.List;

import com.server.jopo.Point;


public interface PointDao {
	public boolean save(List<Point> point);
	public boolean deleteAll();
	public List<Point> find();
}
