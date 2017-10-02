package com.server.other;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

public class CameraVideo extends JComponent implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BufferedImage image;
	Graphics2D graphics;
	Java2DFrameConverter cFrameConverter;
	FrameGrabber grabber;
	CanvasFrame canvasFrame;
	
	int width,height;
	public CameraVideo() {
		//this.graphics = (Graphics2D) graphics2;
		cFrameConverter = new Java2DFrameConverter();
		canvasFrame = new CanvasFrame("video");
		canvasFrame.setDefaultCloseOperation(CanvasFrame.EXIT_ON_CLOSE);
		try {
			grabber = FrameGrabber.createDefault(0);
			grabber.start();
		} catch (Exception e) {
			e.printStackTrace();
		}					
		width = grabber.getImageWidth();
		height = grabber.getImageHeight();
		//System.out.println("CameraVideo:"+hashCode());
		run();
	}

	
	@Override
	public void run() {
		try {
			int fps = 0;
			long Time = System.currentTimeMillis();
			while (true) {				
				image = cFrameConverter.convert(grabber.grab());				
				if(image!=null){					
					//graphics.drawImage(image, null, 0, 0);
					fps++;
					long Fps = System.currentTimeMillis();											
					if(Fps-Time>1000L){
						System.out.println("fps:"+fps);
						Time = Fps;
						fps = 0;						
					}
					canvasFrame.showImage(image);
				}
			}
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		/*JFrame frame = new JFrame("Camera Test");  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.getContentPane().setLayout(new BorderLayout());
        //必须要先显示之后才能得到frame.getGraphics()的返回值，不然都是空
        frame.setVisible(true);  
        //System.out.println(frame.getGraphics());
*/		CameraVideo test = new CameraVideo();
		/*frame.getContentPane().add(test, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(test.width,test.height));
        System.out.println(test.width+","+test.height);
        frame.pack();*/ 
        Thread th = new Thread(test);
        th.start();
        
		//CameraVideo test = new CameraVideo();
        
	}
	public void init() {
		/*CameraVideo test = new CameraVideo();
        Thread th = new Thread(test);
        th.start();*/
	}
}
