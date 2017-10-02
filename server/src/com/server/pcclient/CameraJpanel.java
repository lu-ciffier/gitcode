package com.server.pcclient;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CameraJpanel extends JPanel implements ActionListener{

	private Client client;
	private BufferedImage image;
	private JButton open,close,photo,video;
	public CameraJpanel(Client client) {
		this.client = client;		
		JPanel jbutton = new JPanel();
		open = new JButton("开启摄像");
		close = new JButton("关闭摄像");
		photo = new JButton("拍照");
		video = new JButton("视频");
		open.addActionListener(this);
		close.addActionListener(this);
		photo.addActionListener(this);
		video.addActionListener(this);
		jbutton.add(open);
		jbutton.add(photo);
		jbutton.add(video);
		jbutton.add(close);
		
		jbutton.setLayout(new FlowLayout(FlowLayout.RIGHT));
		add(jbutton);
		try {
			image = ImageIO.read(new File(
					"E:server/photos/image_1467084703051.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(image, 50, 50, 320, 240, null);	
		super.paint(g);
		
	}
	@Override
	protected void paintComponent(Graphics g) {
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton o = (JButton) e.getSource();
		String text = o.getText();
		if(text=="开启摄像"){
			HashMap<String, String> map = new HashMap<String, String>();
			String param = "";
			map.put("open", param);
			try {
				client.sendObject(map);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(text=="拍照"){
						
		}
		if(text=="视频"){
								
		}
		if(text=="关闭摄像"){
											
		}
		
	}
	
}
