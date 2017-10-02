package com.server.action;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.struts2.ServletActionContext;

import com.server.thread.CameraClient;

public class CameraAction {

	
	public String create() throws IOException{
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		String uri = "ws://localhost:8080/cameraSocket/camera";
        Session session;
		try {
			System.out.println("conneck");
			session = container.connectToServer(CameraClient.class, new URI(uri));
			session.getBasicRemote().sendText("123456"); // 发送文本消息
		} catch (DeploymentException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String callback = ServletActionContext.getRequest().getParameter("jsoncallback");			
		ServletActionContext.getResponse().getWriter().write(callback+"("+""+")");
		return null;
	}	
}
