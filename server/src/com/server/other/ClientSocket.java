package com.server.other;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class ClientSocket {

	/*
	 * NET 发布的WS 可以使用Java的客户端调用. 但是需要传输HTTP格式的数据,因为WS是基于HTTP协议的
	 * 
	 * 目前为止: POST GET 可以发送WS 但是已经不推荐使用. 我们应该使用SOAP的格式来发送数据
	 * 
	 * SOAP: 就是XML SOAP1.1 绝大部分都使用1.1 因为JDK1.6只支持SOAP1.1 SOAP1.2 安全
	 * 
	 * HttpClient: 测试HTTP请求
	 * 通过wsimport 解析WSDL 生成响应的源码
	 */

	public static void main(String[] args) throws Exception {
		
		
		 Socket sc = new Socket("127.0.0.1", 7777); // 向socket服务端发送数据请求
		 OutputStream out = sc.getOutputStream(); // 发送数据
		 //InputStream in = sc.getInputStream();
		 out.write("luo,123456".getBytes()); 
		 sc.close();
		 // 等待并接受从服务端返回来的数据 
		/* byte[] b = new byte[1024];
		 while(true){			 
			 int len = in.read(b);
			 if(len>-1){ 
				 String result = new String(b, 0, len);
				 System.out.println("服务端处理的结果为:" + result);
			 }
		 }*/
	}
}
