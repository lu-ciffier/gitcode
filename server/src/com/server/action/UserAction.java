package com.server.action;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.server.service.UserService;
/**
 * @author lucyf
 * @version 2017.5.10
 * 处理用户登录请求Action
 * **/
public class UserAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private UserService userService = null;
	private RealDataAction realDataAction = null;
	private String username;
	private String password;
	private String platform;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setRealDataAction(RealDataAction realDataAction) {
		this.realDataAction = realDataAction;
	}
	/*
	 * 用户登录
	 */
	public String login() throws Exception {
		System.out.println(platform);
		if(platform.equals("pc")){//from pc app
			String json = null;
			if(validateId()){
				json = "{success:true,msg:'登陆成功'}";
			}else{
				json = "{success:false,msg:'登陆失败'}";
			}
			return json;
		}else{
			
			ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			if(platform.equals("web")){//from web
				if(validateId()){
					ActionContext.getContext().getSession().put("username", username);
					realDataAction.init();
					return SUCCESS;
				}else {
					return "input";
				}
			}else if(platform.equals("app")){//from ionic app
				String json = null;
				if(validateId()){
					json = "{success:true,msg:'登陆成功'}";
				}else{
					json = "{success:false,msg:'登陆失败'}";
				}
				//return json;
				String callback = ServletActionContext.getRequest().getParameter("jsoncallback");
				//System.out.println("back:"+callback);
				ServletActionContext.getResponse().getWriter().write(callback+"("+json+")");
				
				return null;
				
			}else if(platform.equals("ap")){//from android app
				String json = null;
				if(validateId()){
					json = "{success:true,msg:'登陆成功'}";
				}else{
					json = "{success:false,msg:'登陆失败'}";
				}
				ServletActionContext.getResponse().getWriter().write(json);
				return null;
			}else {
				return null;
			}
		}
	}
	/*
	 * 用户注册
	 */
	public String regist() throws Exception {				
			
		if (platform.equals("pc")) {// from pc app
			String json = null;
			if (validatenull()) {
				if (userService.regist(username, password)) {
					json = "{success:true,msg:'注册成功'}";
				}else{
					json = "{success:false,msg:'注册失败'}";
				}
			} else {
				json = "{success:false,msg:'注册失败'}";
			}
			return json;
		} else {

			ServletActionContext.getResponse().setContentType(
					"text/html;charset=utf-8");
			if (platform.equals("web")) {// from web
				if (validatenull()) {
					if (userService.regist(username, password)) {
						ActionContext.getContext().getSession()
								.put("username", username);
						realDataAction.init();
						return SUCCESS;
					}else{
						return "input";
					}
				} else {
					return "input";
				}
			} else if (platform.equals("app")) {// from ionic app
				String json = null;
				if (validatenull()) {
					if (userService.regist(username, password)) {
						json = "{success:true,msg:'注册成功'}";
					} else {
						json = "{success:false,msg:'注册失败'}";
					}
				} else {
					json = "{success:false,msg:'注册失败'}";
				}
				// return json;
				String callback = ServletActionContext.getRequest()
						.getParameter("jsoncallback");
				// System.out.println("back:"+callback);
				ServletActionContext.getResponse().getWriter()
						.write(callback + "(" + json + ")");
				return null;

			} else if (platform.equals("ap")) {// from android app
				String json = null;
				if (validatenull()) {
					if (userService.regist(username, password)) {
						json = "{success:true,msg:'注册成功'}";
					} else {
						json = "{success:false,msg:'注册失败'}";
					}
				} else {
					json = "{success:false,msg:'注册失败'}";
				}
				ServletActionContext.getResponse().getWriter().write(json);
				return null;
			} else {
				return null;
			}
		}		
	}
	/*
	 * 数据验证
	 */
	public boolean validateId() {
		boolean flag = false;
		if (userService.login(username, password)) {
			flag = true;
		}
		return flag;
	}
	/*
	 * 空数据验证
	 */
	public boolean validatenull(){
		if(username==null||"".equals(username)||password==null
				||"".equals(password)){
			return false;
		}else {
			return true;
		}
	}
}
