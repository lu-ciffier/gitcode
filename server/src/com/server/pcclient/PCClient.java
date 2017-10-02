package com.server.pcclient;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;


/**
 * @author lucyf
 * @version 2017.5.10
 * 电脑APP客户端入口
 * **/
public class PCClient extends JFrame implements WindowListener, ActionListener{
	
	private Client client;
	private JButton user,real,history,camera,system;
	JPanel realjJPanel,loginJPanel,historyJPanel,cameraJPanel,configJPanel;
	
	public PCClient() throws Exception{
		init();
	
		JMenuBar bar = new JMenuBar();
		user = new JButton("用户登录");
		real = new JButton("实时采集");
		history = new JButton("历史数据");
		//camera = new JButton("图像监控");
		system = new JButton("系统设置");
		
		user.addActionListener(this);
		real.addActionListener(this);
		history.addActionListener(this);
		//camera.addActionListener(this);
		system.addActionListener(this);
		
		bar.add(user);
		bar.add(real);
		bar.add(history);
		//bar.add(camera);
		bar.add(system);
		this.setJMenuBar(bar);
		//this.setContentPane(loginJPanel);		
		this.addWindowListener(this);
	}
	public static void main(String[] args) throws Exception {
		
		new PCClient();
			 
	}
	/**
	 * UI初始化
	 * **/
	public void init() throws Exception{
		String ip = InetAddress.getLocalHost().getHostAddress();
		client = new Client(ip, 7777);
		client.start();
		this.setTitle("温室监测系统");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		// 设置窗口大小
		this.setSize(700, 500);
		//获得显示器大小对象
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();
		// 获得窗口大小对象
		Dimension frameSize = this.getSize();
		if (frameSize.width > displaySize.width){			
			// 窗口的宽度不能大于显示器的宽度
			frameSize.width = displaySize.width; 
		}  
		if (frameSize.height > displaySize.height){
			// 窗口的高度不能大于显示器的高度
			frameSize.height = displaySize.height;		
		}  
		// 设置窗口居中显示器显示 
		this.setLocation((displaySize.width - frameSize.width) / 2,  
				(displaySize.height - frameSize.height) / 2); 
		//frame.pack();
		this.setVisible(true);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		try {
			//ps.close();
			//client.stop();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.exit(0);
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 按钮菜单动作
	 * **/
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton o = (JButton) e.getSource();
		String text = o.getText();
		this.getContentPane().removeAll();
		if(text=="用户登录"){
			if(loginJPanel==null){			
				loginJPanel = new LoginJpanel(client);
			}
			this.getContentPane().add(loginJPanel);
			this.validate();
			
		}
		if(text=="实时采集"){
			if(realjJPanel==null){			
				realjJPanel = new RealJpanel(client);
			}
			this.getContentPane().add(realjJPanel);
			//real.setBackground(new Color(100));
			this.validate();
		}
		if(text=="历史数据"){
			if(historyJPanel==null){			
				historyJPanel = new HistoryJpanel(client);
			}
			this.getContentPane().add(historyJPanel);			
			this.validate();
		}
		if(text=="图像监控"){
			if(cameraJPanel==null){			
				cameraJPanel = new CameraJpanel(client);
			}
			this.getContentPane().add(cameraJPanel);		
			this.validate();
		}
		if(text=="系统设置"){
			if(configJPanel==null){				
				configJPanel = new configJpanel(client);
			}
			this.getContentPane().add(configJPanel);			
			this.validate();
		}
	}
}
