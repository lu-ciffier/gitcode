package com.server.other;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.FrameGrabber.Exception;

public class CameraThread {

	private BufferedImage image;
	private Java2DFrameConverter cFrameConverter;
	private ByteArrayOutputStream bao;
	private FrameGrabber grabber;
	private Encoder encoder;
	private String baseimage;
	private Thread thread;
	private boolean flag;
	private CameraThread(){
		cFrameConverter = new Java2DFrameConverter();
		bao = new ByteArrayOutputStream();
		encoder = Base64.getEncoder();
		baseimage = "";
		flag = false;
	};
	private static final CameraThread CAMERA = new CameraThread();
	public static CameraThread getInstance(){
		return CAMERA;
	}

	public void open() {
		flag = true;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					grabber = FrameGrabber.createDefault(0);
					grabber.start();
					while (flag) {
						bao.reset();
						image = cFrameConverter.convert(grabber.grab());
						if (image != null) {
							ImageIO.write(image, "PNG", bao);
							byte[] bytes = bao.toByteArray();
							baseimage = encoder.encodeToString(bytes).trim();
						}
					}
					grabber.stop();
					bao.close();
				} catch (Exception | IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public String getBaseimage() {
		return baseimage;
	}
	public void stop(){
		this.flag = false;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public boolean isFlag() {
		return flag;
	}
}
