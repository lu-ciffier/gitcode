package com.server.jopo;

import java.sql.Timestamp;

/**
 * @author lucyf
 * @version 2017.5.10
 * Alert entity. @author MyEclipse Persistence Tools
 */

public class Alert implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String point;
	private String datatype;
	private Integer min;
	private Integer max;
	private Timestamp date;

	// Constructors

	/** default constructor */
	public Alert() {
	}

	/** full constructor */
	public Alert(String name, String point, String datatype, Integer min,
			Integer max, Timestamp date) {
		this.name = name;
		this.point = point;
		this.datatype = datatype;
		this.min = min;
		this.max = max;
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

	public String getDatatype() {
		return this.datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public Integer getMin() {
		return this.min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return this.max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}