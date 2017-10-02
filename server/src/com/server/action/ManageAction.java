package com.server.action;

import java.text.SimpleDateFormat;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.server.jopo.User;
import com.server.service.ParameterService;
import com.server.service.UserService;

/**
 * @author lucyf
 * @version 2017.5.10
 * 处理用户管理请求Action
 * **/
public class ManageAction {
	private String username;
	private String platform;
	private UserService userService = null;
	private ParameterService parameterService = null;

	public void setUsername(String username) {
		this.username = username;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/*
	 * 查询用户基本信息
	 */
	public String userinfo() throws Exception {
		if(platform.equals("app")){
			User user = userService.find(username);
			JSONObject ob = JSONObject.fromObject(user);
			String callback = ServletActionContext.getRequest().getParameter("jsoncallback");			
			ServletActionContext.getResponse().getWriter().write(callback+"("+ob+")");
			return null;
		}else{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, Object> session = ActionContext.getContext().getSession();
			username = (String) session.get("username");
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			User user = userService.find(username);
			if(user!=null){				
				String userinfo = user.getUsername()+","+user.getPassword()+","+
						format.format(user.getRegdate());
				ServletActionContext.getResponse().getWriter().write(userinfo);
			}
			return null;
		}
		
	}
	/*
	 * 删除用户数据
	 */
	public String dropdata() throws Exception {
		Map<String, Object> session = ActionContext.getContext().getSession();
		username = (String) session.get("username");
		parameterService.delete_by_user(username);
		return null;
	}
	/*
	 * 退出登录
	 */
	public String userquit() throws Exception {
		if(platform.equals("web")){		
			ActionContext.getContext().getSession().remove("username");
		}
		return null;
	}
	
}
