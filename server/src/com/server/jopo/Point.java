package com.server.jopo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lucyf
 * @version 2017.5.10
 * Point entity. @author MyEclipse Persistence Tools
 */

public class Point implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String pointnum;
	private String fpoint;
	private String point;
	private Timestamp date;

	// Constructors

	/** default constructor */
	public Point() {
	}

	/** full constructor */
	public Point(String pointnum, String fpoint, String point, Timestamp date) {
		this.pointnum = pointnum;
		this.fpoint = fpoint;
		this.point = point;
		this.date = date;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPointnum() {
		return this.pointnum;
	}

	public void setPointnum(String pointnum) {
		this.pointnum = pointnum;
	}

	public String getFpoint() {
		return this.fpoint;
	}

	public void setFpoint(String fpoint) {
		this.fpoint = fpoint;
	}

	public String getPoint() {
		return this.point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	// 把数据格式化为单独的一条结果
	public List<String> format(String data) {
		List<String> list = new ArrayList<String>();
		StringBuilder builder = new StringBuilder("");
		char[] s = data.toCharArray();
		for(int i=0;i<s.length;i++){
			builder.append(s[i]);			
			if(s[i]=='A'&&s[i-1]=='2'&&(i+1)%64==0){
				list.add(builder.toString());
				builder.setLength(0);
			}
		}
		return list;
	}
	public Point getPoint(String data){
		
		//26524E441BA23E01004B12003030524F5530307F9E3E01004B1200000030DC2A
		Point point = new Point();
		if (data.startsWith("26524E44")){
			// pointnum 节点代号
			String pointnum = data.substring(8, 10);
			
			// point节点
			String pointadd = data.substring(8, 24);
			
			//fpoint
			String fpoint = data.substring(38, 54);
			//fpoint = getFpoint(fpoint);
			
			//date
			Timestamp date = new Timestamp(new Date().getTime());
			if(pointadd.equals("3030303030303030")){
				point = null;
				return point;
			}
			point.setPointnum(pointnum);
			point.setPoint(pointadd);
			point.setFpoint(fpoint);
			point.setDate(date);
		}else {
			point = null;
		}
		return point;
	}
	public List<Point> getPoints(String data){
		List<String> list = format(data);
		List<Point> points = new ArrayList<Point>();
		for(String s : list){
			Point p = getPoint(s);
			if(p != null){			
				points.add(p);
			}
		}
		return points;
	}
	public String getFpoint(String fpoint){
		//7F9E3E01004B1200
		char[] fp = fpoint.toCharArray();
		StringBuilder father = new StringBuilder("");
		for(int i=14;i>=0;){			
			father.append(fp[i]);
			father.append(fp[i+1]);
			i = i-2;
		}
		return father.toString();
	}
	public static void main(String[] args) {
		new Point().getPoint("");
	}
}