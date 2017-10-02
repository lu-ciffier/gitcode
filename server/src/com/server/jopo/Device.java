package com.server.jopo;

import java.sql.Timestamp;

/**
 * @author lucyf
 * @version 2017.5.10
 * Device entity. @author MyEclipse Persistence Tools
 */

public class Device implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String address;
	private Integer xposition;
	private Integer yposition;
	private Integer zposition;
	private Timestamp date;

	// Constructors

	/** default constructor */
	public Device() {
	}

	/** full constructor */
	public Device(String name, String address, Integer xposition,
			Integer yposition, Integer zposition, Timestamp date) {
		this.name = name;
		this.address = address;
		this.xposition = xposition;
		this.yposition = yposition;
		this.zposition = zposition;
		this.date = date;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getXposition() {
		return this.xposition;
	}

	public void setXposition(Integer xposition) {
		this.xposition = xposition;
	}

	public Integer getYposition() {
		return this.yposition;
	}

	public void setYposition(Integer yposition) {
		this.yposition = yposition;
	}

	public Integer getZposition() {
		return this.zposition;
	}

	public void setZposition(Integer zposition) {
		this.zposition = zposition;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}