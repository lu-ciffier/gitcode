package com.server.action;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.FrameGrabber.Exception;

public class CameraServlet extends HttpServlet{
	
	private BufferedImage image;
	private Java2DFrameConverter cFrameConverter = new Java2DFrameConverter();
	private FrameGrabber grabber;
	private HttpServletResponse response;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.response = resp;
		try {
			grabber = FrameGrabber.createDefault(0);
			grabber.start();
		} catch (Exception e) {
			e.printStackTrace();
		}					
		response.setContentType("image/png");
		new Thread(new Runnable() {		
			@Override
			public void run() {
				try {
					while (true) {
						image = cFrameConverter.convert(grabber.grab());
						if (image != null) {
							ImageIO.write(image,"PNG",response.getOutputStream());
							response.getOutputStream().flush();
						}
					}
				} catch (Exception | IOException e) {
					e.printStackTrace();
				}				
			}
		}).start();
		
	}
}
