package com.server.jopo;

import java.sql.Timestamp;

/**
 * @author lucyf
 * @version 2017.5.10
 * Alertinfo entity. @author MyEclipse Persistence Tools
 */

public class Alertinfo implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String point;
	private String reason;
	private Timestamp date;

	// Constructors

	/** default constructor */
	public Alertinfo() {
	}

	/** full constructor */
	public Alertinfo(String name, String point, String reason, Timestamp date) {
		this.name = name;
		this.point = point;
		this.reason = reason;
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

	public String getPoint() {
		return this.point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}