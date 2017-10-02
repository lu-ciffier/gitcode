package com.server.service;

import java.util.List;

import com.server.dao.PointDao;
import com.server.jopo.Point;

/*
 * PointServiceImpl
 * */
public class PointServiceImpl implements PointService{
	
	private PointDao pointDao = null;
	public void setPointDao(PointDao pointDao) {
		this.pointDao = pointDao;
	}

	
	public boolean save(List<Point> list) {
		boolean save_flag = false;
		if(!list.isEmpty()){						
			save_flag = pointDao.save(list);			
		}
		return save_flag;
	}

	public List<Point> find() {
		List<Point> list = pointDao.find();
		return list;
	}
	public Point getPoint(String data,String username){
		Point point = new Point();
		String s = data.substring(8, 24);
		if(s.equals("3030303030303030")==false){		
			point.setPoint(s);
			
			point.setPointnum(data.substring(8, 10));
		}else {
			point=null;
		}
		return point;
	}

}
