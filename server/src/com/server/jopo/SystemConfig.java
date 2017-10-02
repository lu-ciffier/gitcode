package com.server.jopo;

import java.sql.Timestamp;


/**
 * @author lucyf
 * @version 2017.5.10
 * SystemConfig entity. @author MyEclipse Persistence Tools
 */

public class SystemConfig  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
     private String username;
     private String type;
     private String port;
     private String botelv;
     private Timestamp date;


    // Constructors

    /** default constructor */
    public SystemConfig() {
    }

    
    /** full constructor */
    public SystemConfig(String username, String type, String port, String botelv, Timestamp date) {
        this.username = username;
        this.type = type;
        this.port = port;
        this.botelv = botelv;
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

    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public String getPort() {
        return this.port;
    }
    
    public void setPort(String port) {
        this.port = port;
    }

    public String getBotelv() {
        return this.botelv;
    }
    
    public void setBotelv(String botelv) {
        this.botelv = botelv;
    }

    public Timestamp getDate() {
        return this.date;
    }
    
    public void setDate(Timestamp date) {
        this.date = date;
    }
   








}