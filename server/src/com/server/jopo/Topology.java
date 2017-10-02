package com.server.jopo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lucyf
 * @version 2017.5.10
 * Topology entity. @author MyEclipse Persistence Tools
 */

public class Topology implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String point;
	private String fpoint;
	private Integer quality;
	private Timestamp date;

	// Constructors

	/** default constructor */
	public Topology() {
	}

	/** full constructor */
	public Topology(String point, String fpoint, Integer quality, Timestamp date) {
		this.point = point;
		this.fpoint = fpoint;
		this.quality = quality;
		this.date = date;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPoint() {
		return this.point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getFpoint() {
		return this.fpoint;
	}

	public void setFpoint(String fpoint) {
		this.fpoint = fpoint;
	}

	public Integer getQuality() {
		return this.quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
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
	public Topology getTopology(String data){
		Topology topology = new Topology();
		if (data.startsWith("26524E53")) {
	//26524E53 1BA23E01004B1200 0001 303235 7F9E3E01004B1200 0000787878052A
			// point节点
			String point = data.substring(8, 24);
			//通信质量(百位 十位 个位)
			String quality_b = data.substring(28, 30);
			int bai = Integer.parseInt(quality_b)-30;
			String quality_s = data.substring(30, 32);
			int shi = Integer.parseInt(quality_s)-30;
			String quality_g = data.substring(32, 34);
			int ge = Integer.parseInt(quality_g)-30;
			int quality = bai*100+shi*10+ge;
			//point父节点
			String fpoint = data.substring(34, 50);
			//date
			Timestamp date = new Timestamp(new Date().getTime());
			topology.setPoint(point);		
			topology.setFpoint(fpoint);
			topology.setQuality(quality);
			topology.setDate(date);
		}else{
			topology = null;
		}
		return topology;
	}
	public List<Topology> getTopologys(String data){
		List<String> list = format(data);
		List<Topology> topologies = new ArrayList<Topology>();
		for(String s : list){
			Topology p = getTopology(s);
			topologies.add(p);
		}
		return topologies;
	}
}