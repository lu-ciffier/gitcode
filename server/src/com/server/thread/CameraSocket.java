package com.server.thread;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.FrameGrabber.Exception;

/**
 * @author lucyf
 * @version 2017.5.10
 * 视频监控Socket
 * **/
/*
 * 该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。
 * 类似Servlet的注解mapping。无需在web.xml中配置。
 */
@ServerEndpoint("/cameraSocket/camera")
public class CameraSocket{

	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	private BufferedImage image;
	private Frame frame;
	private  Java2DFrameConverter cFrameConverter;
	private  ByteArrayOutputStream bao;
	private FrameGrabber grabber;
	private  Encoder encoder;
	private volatile  boolean flag;
	private static Thread thread;
	private String baseimage="";	
	
	final private static int WEBCAM_DEVICE_INDEX = 0;
    final private static int FRAME_RATE = 5;
    final private static int GOP_LENGTH_IN_FRAMES = 60;

    private FFmpegFrameRecorder recorder;
    private String videoname;
    private volatile boolean videostart;
    private volatile boolean videoing;
    private volatile boolean revideostart;
    private volatile boolean videoclose;
      

	/*concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	 * 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	 */
	private static CopyOnWriteArrayList<CameraSocket> cameraSocketSet = new 
			CopyOnWriteArrayList<CameraSocket>();	
	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	
	public CameraSocket() throws Exception, IOException{
		
		bao = new ByteArrayOutputStream();
		encoder = Base64.getEncoder();
		flag = true;
		System.out.println("cameraSocket:"+this.hashCode());		
	}
	/*
	 * 
	 * 连接建立成功调用的方法
	 * 
	 * @param session
	 *  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 *  第一个建立连接的用户会启动摄像头
	 */
	@OnOpen
	public void onOpen(Session session) throws Exception, IOException {
		this.session = session;
		cameraSocketSet.add(this);
		// 加入set中
		addOnlineCount(); // 在线数加1
		if(getOnlineCount()==1){			
			System.out.println("camera is opening...");
			camera();						
		}
		System.out.print(this.session.getId()+"新连接加入!");
		System.out.println("当前在线人数为" + getOnlineCount());
	}

	/*
	 * 
	 * 连接关闭调用的方法
	 * @throws InterruptedException 
	 * @throws IOException
	 */

	@OnClose
	public void onClose() throws InterruptedException, IOException {
				
		cameraSocketSet.remove(this); // 从set中删除
		subOnlineCount(); // 在线数减1		
		System.out.println(this.session.getId()+"连接关闭!");
		System.out.println("当前在线人数为" + getOnlineCount());
	}

	/*
	 * 
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message
	 * 客户端发送过来的消息
	 * 
	 * @param session
	 * 可选的参数
	 * @throws InterruptedException 
	 * @throws org.bytedeco.javacv.FrameRecorder.Exception 
	 */

	@OnMessage
	public void onMessage(String message, Session session) throws InterruptedException, 
			org.bytedeco.javacv.FrameRecorder.Exception {
		this.session = session;
		System.out.println("来自客户端"+session.getId()+"的消息:" + message);
		/*
		 * 关闭命令
		 * */
		if(message.equals("closecamera")){
			if(getOnlineCount()==1){				
				flag = false;			
				CameraSocket.thread.join();
				System.out.println("camera is closing...");
				for (CameraSocket item : cameraSocketSet) {
					try {
						item.sendMessage("0");
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}
		/*
		 * 拍照命令
		 * */
		if(message.equals("takephoto")){
			System.out.println("camera is taking photo...");
			long id = new Date().getTime();			
			File imagefile = new File("E:server/photos/image_"+id+".png");
			try {
				ImageIO.write(image, "PNG", imagefile);
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		/*
		 * 录制视频命令
		 * */
		if(message.equals("takevideo")){
			long id = new Date().getTime();
			//WebRoot/videos/video_"+id+".mp4
			videoname = "E:server/videos/video_"+id+".mp4";
			recorder = new FFmpegFrameRecorder(videoname,
			        grabber.getImageWidth(), grabber.getImageHeight(), 2);			
			recorder.setInterleaved(true);
			recorder.setVideoOption("tune", "zerolatency");		        
			recorder.setVideoOption("preset", "ultrafast");		       
			recorder.setVideoOption("crf", "28");		 
			recorder.setVideoBitrate(2000000);
			recorder.setGopSize(GOP_LENGTH_IN_FRAMES);
			recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
			recorder.setFormat("flv");
			recorder.setFrameRate(FRAME_RATE);
			videostart = true;			
		}
		if(message.equals("retakevideo")){
			if(videoing){			
				revideostart = true;			
			}
		}
		if(message.equals("savevideo")){
			videoclose = true;
		}
	}
	
	/*
	 * 
	 * 发生错误时调用
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		this.session = session;		
	}

	/*
	 * 初始化服务器摄像头
	 */
	public synchronized void camera() throws Exception, IOException{
		cFrameConverter = new Java2DFrameConverter();
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {	
				try {
					grabber = FrameGrabber.createDefault(WEBCAM_DEVICE_INDEX);
					grabber.start();
				} catch (Exception e) {
					e.printStackTrace();
				}				
				try {												
					videostart = false;
					videoing = false;
					revideostart = false;
					videoclose = false;				
					int fps = 0;
					long Time = System.currentTimeMillis();
					while (flag) {						
						if(cameraSocketSet.size()>0){						
							for (CameraSocket item : cameraSocketSet) {							
								item.sendMessage(baseimage);
							}
						}else{
							break;
						}
						bao.reset();
						//获取图像帧
						frame = grabber.grab();
						image = cFrameConverter.convert(frame);						
						if (image != null) {
							fps++;
							long Fps = System.currentTimeMillis();
							ImageIO.write(image,"PNG",bao);
							byte[] bytes = bao.toByteArray();						
							if(Fps-Time>1000L){
								System.out.println("fps:"+fps);
								Time = Fps;
								fps = 0;
								long length = bytes.length;
								long size = length/1024;
								System.out.println("image:"+size+"kB");
							}
							//Base64编码
							baseimage = encoder.encodeToString(bytes).trim();										
						}						
				        if(videostart){		        	
				        	recorder.start();
				        	videoing = true;
				        	videostart = false;				        	
				        }
				        if(videoing){
				        	try {	        		
				        		recorder.record(frame);			        		
				        	} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
				        		e.printStackTrace();
				        	}
				        }
				        if(revideostart){
				        	if(videoing){
				        		videoing = false;
				        		recorder.stop();
				        		recorder.start();
				        	}else{
				        		recorder.start();
				        	}
				        	videoing = true;							
							revideostart = false;
				        }
				        if(videoclose){
			        		try {
			        			videoing = false;
								recorder.stop();
								videoclose = false;													
							} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
								e.printStackTrace();
							}
				        }						
					}
					grabber.stop();
					bao.close();
				} catch (Exception | IOException e) {
					e.printStackTrace();
				} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
					e.printStackTrace();
				}				
			}
		});
		thread.start();
	}
	/*
	 * 给Socket客户端发送消息
	 * @param message 消息内容
	 * **/
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		CameraSocket.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		CameraSocket.onlineCount--;
	}
	
	public static void main(String[] args) throws Exception, IOException {
		CameraSocket cs = new CameraSocket();
		cs.camera();
	}
}
