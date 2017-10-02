package com.server.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;

import sun.misc.BASE64Encoder;

public class PhotoAction {
	
	private BufferedImage image;
	private Encoder encoder;
	private InputStream inputStream;
	
	public void loadPhoto() throws IOException{
		byte[] data = null;
		String base64;
		String base64s = "";
		BASE64Encoder encoder = new BASE64Encoder();
		File file = new File("E:/server/photos");
		File[] photos = file.listFiles();
		for(int i=0;i<photos.length;i++){
			File photo = photos[i];
			
			String photoname = photo.getName();
			System.out.println(photoname);
			System.out.println(photoname.endsWith(".png"));
			if(photoname.endsWith(".png")){
				inputStream = new FileInputStream(photo);				
				data = new byte[inputStream.available()];
				inputStream.read(data);
				inputStream.close();
				base64 = encoder.encode(data);
				//base64s = base64s+base64+"";
				System.out.println(base64);
			}
		}
	}
	public static void main(String[] args) throws IOException {
		//new PhotoAction().loadPhoto();
			String ip = InetAddress.getLocalHost().getHostAddress();       
			System.out.println(ip);
	}
}
