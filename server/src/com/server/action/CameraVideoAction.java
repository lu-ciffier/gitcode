package com.server.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;

public class CameraVideoAction {

	private BufferedImage image;
	private Java2DFrameConverter cFrameConverter;
	private ByteArrayOutputStream bao;
	private FrameGrabber grabber;
	private HttpServletResponse response;
	private Encoder encoder;
	private volatile boolean flag;
	private Thread thread;
	private String baseimage="";
	int width,height;
	public CameraVideoAction(){
		System.out.println(this.hashCode());
	}
	public String video() {
		try {
			grabber = FrameGrabber.createDefault(0);
			grabber.start();
		} catch (Exception e) {
			e.printStackTrace();
		}					
		width = grabber.getImageWidth();
		height = grabber.getImageHeight();
		response = ServletActionContext.getResponse();
		response.setContentType("image/png");
		thread = new Thread(new Runnable() {		
			@Override
			public void run() {
				try {
					while (flag) {
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
		});
		thread.start();
		
		return "success";
	}
	public void base64() throws Exception, IOException{
				
			thread = new Thread(new Runnable() {		
				@Override
				public void run() {
					try {
						grabber = FrameGrabber.createDefault(0);
						grabber.start();
						cFrameConverter = new Java2DFrameConverter();
						bao = new ByteArrayOutputStream();
						encoder = Base64.getEncoder();
						flag = true;
						
						while (flag) {
							image = cFrameConverter.convert(grabber.grab());
							//bao.reset();
							if (image != null) {
								//ImageIO.write(image,"PNG",bao);
								//bao.flush();
								//System.out.println("image:"+baseimage);
								//flag = false;
							}
						}
						grabber.stop();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			thread.start();
							
	}
	public void getBaseImage() throws IOException{
		if(image!=null){
			
			ImageIO.write(image,"PNG",bao);
			byte[] bytes = bao.toByteArray();
			baseimage = encoder.encodeToString(bytes).trim();
			//System.out.println(this.hashCode()+":"+baseimage);
			ServletActionContext.getResponse().getWriter().write(baseimage);
			bao.reset();
		}
		
	}
	public void stop() throws InterruptedException, Exception, IOException{
		
		this.flag = false;
		thread.join();
		bao.close();
		//grabber.stop();
		
	}
	public static void main(String[] args) throws Exception, IOException {
		new CameraVideoAction().base64();
	}
}
