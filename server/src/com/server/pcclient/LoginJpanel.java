package com.server.pcclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author lucyf
 * @version 2017.5.10
 * 登录模块
 * **/
public class LoginJpanel extends JPanel implements ActionListener{

	private Client client;
	private JTextField username,password;
	public LoginJpanel(Client client){
		this.client = client;
		/*JTextArea username = new JTextArea();
		JTextArea password = new JTextArea();
		add(username);
		add(password);*/
		username = new JTextField("admin", 10);
		password = new JTextField("admin", 10);
		//this.add(username);	
		JButton login = new JButton("登录");
		login.addActionListener(this);
		
		add(username);
		add(password);
		add(login);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		HashMap<String, String> map = new HashMap<String, String>();
		String id = username.getText()+":"+password.getText();
		map.put("login", id);
		try {
			client.sendObject(map);
			Thread.sleep(500);
			String result = client.getResponce();
			System.out.println(result+" login success");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
