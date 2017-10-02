package com.server.jopo;

import java.io.Serializable;
import java.util.Date;


/**
 * @author lucyf
 * @version 2017.5.10
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements Serializable{


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
     private String username;
     private String password;
     private Date regdate;


    // Constructors

    /** default constructor */
    public User() {
    }

	/** minimal constructor */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /** full constructor */
    public User(String username, String password, Date regdate) {
        this.username = username;
        this.password = password;
        this.regdate = regdate;
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

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegdate() {
        return this.regdate;
    }
    
    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

}