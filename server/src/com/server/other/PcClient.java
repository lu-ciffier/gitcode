package com.server.other;

import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class PcClient extends JFrame implements WindowListener, ActionListener{
	private JFrame frame;
	private Socket ps;
	private OutputStream out;
	private InputStream in;
	
	public PcClient() throws Exception{
		init();
		JPanel panel = new JPanel();
		JButton login = new JButton();
		JButton regist = new JButton();
		login.setText("登录");
		regist.setText("注册");
		login.addActionListener(this);
		regist.addActionListener(this);
		panel.add(login);
		panel.add(regist);
		this.add(panel);
		this.addWindowListener(this);
	}
	public static void main(String[] args) throws Exception {
		//JFrame frame = new JFrame("ShareModel");
		new PcClient();
				
		/*JTextArea areaFiftyOne = new JTextArea();
		JTextArea areaFiftyTwo = new JTextArea();
		areaFiftyTwo.setDocument(areaFiftyOne.getDocument());
		JTextArea areaFiftyThree = new JTextArea();
		areaFiftyThree.setDocument(areaFiftyOne.getDocument());

		Container content = frame.getContentPane();
		content.setLayout(new GridLayout(3, 1));
		content.add(new JScrollPane(areaFiftyOne));
		content.add(new JScrollPane(areaFiftyTwo));
		content.add(new JScrollPane(areaFiftyThree));*/

		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//client.frame.setSize(300, 300);
		//client.frame.setVisible(true); 
	}
	public void init() throws Exception{
		
		this.setTitle("温室监测系统");
		ps = new Socket("127.0.0.1", 7777);
		out = ps.getOutputStream();
		in = ps.getInputStream();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//frame.getContentPane().add();
		// 设置窗口大小
		this.setSize(300, 200);
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
		//frame.setResizable(false);
	}
	public void login() throws IOException{
		out.write("luo,123456".getBytes());
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		try {
			ps.close();
		} catch (IOException e1) {
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
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			//获取注册监听器的控件的信息
			JButton b = (JButton) e.getSource();
			System.out.println(b.getText());
			login();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
