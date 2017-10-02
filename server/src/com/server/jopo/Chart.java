package com.server.jopo;

import java.sql.Timestamp;


/**
 * @author lucyf
 * @version 2017.5.10
 * Chart entity. @author MyEclipse Persistence Tools
 */

public class Chart  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
     private String username;
     private String name;
     private String point;
     private String datatype;
     private Timestamp date;


    // Constructors

    /** default constructor */
    public Chart() {
    }

    
    /** full constructor */
    public Chart(String username, String name, String point, String datatype, Timestamp date) {
        this.username = username;
        this.name = name;
        this.point = point;
        this.datatype = datatype;
        this.date = date;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
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

    public Timestamp getDate() {
        return this.date;
    }
    
    public void setDate(Timestamp date) {
        this.date = date;
    }
   








}