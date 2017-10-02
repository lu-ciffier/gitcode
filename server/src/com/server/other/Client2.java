package com.server.other;

import java.io.IOException;
import java.net.UnknownHostException;

import net.sf.json.JSONObject;


public class Client2 extends Client{

	public Client2(String serverIp, int port) {
		super(serverIp, port);	
	}
	public final class Login implements ObjectAction {

		@Override
		public void doAction(Object obj, Client client) {
			// TODO Auto-generated method stub
			JSONObject jo = JSONObject.fromObject(obj);			
			System.out.println(jo.getBoolean("success"));
		}
		
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client2 c = new Client2("127.0.0.1", 7777);
		c.addActionMap(String.class, c.new Login());
		c.start();
	}

	
}
