package com.server.pcclient;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * @author lucyf
 * @version 2017.5.10
 * 设置模块
 * **/
public class configJpanel extends JPanel implements ActionListener{
	
	private Client client;
	private String ports="";
	private JComboBox<String> porth;
	private JComboBox<String> botelvh;
	private JComboBox<String> portw;
	private JComboBox<String> botelvw;
	public configJpanel(Client client){
		this.client = client;
		JPanel now = new JPanel();
		JPanel chooseh = new JPanel();
		JPanel choosew = new JPanel();
		JPanel button = new JPanel();
		getPort();
		String[] ps = ports.split(",");
		porth = new JComboBox<String>();
		botelvh = new JComboBox<String>();
		portw = new JComboBox<String>();
		botelvw = new JComboBox<String>();
		JTextField portNow = new JTextField();
		JButton save = new JButton("保存");
		save.addActionListener(this);
		portNow.setText("当前工作串口："+"温室串口"+ps[0]+",气象站"+ps[1]);			
		for(int i=2;i<ps.length;i++){
			porth.addItem(ps[i]);
			portw.addItem(ps[i]);
		}		
		botelvh.addItem("4800");		
		botelvh.addItem("9600");		
		botelvh.addItem("18200");		
		botelvh.addItem("38400");
		botelvw.addItem("9600");		
		botelvw.addItem("18200");		
		botelvw.addItem("38400");
		botelvw.addItem("57600");		
		
		now.add(portNow);
		now.setPreferredSize(new Dimension(600,40));
		
		JTextField texth = new JTextField();
		texth.setText("温室串口：");
		chooseh.add(texth);
		chooseh.add(porth);
		chooseh.add(botelvh);
		
		JTextField textw = new JTextField();
		textw.setText("气象站串口：");
		choosew.add(textw);
		choosew.add(portw);
		choosew.add(botelvw);
		chooseh.setPreferredSize(new Dimension(600,40));
		choosew.setPreferredSize(new Dimension(600,40));
		button.add(save);
		button.setPreferredSize(new Dimension(600,50));
		add(now);
		add(chooseh);
		add(choosew);
		add(button);
	}
	/*
	 * 获取历史数据
	 * */
	public void getPort(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("config", "port");
		try {
			client.sendObject(map);
			Thread.sleep(500);
			this.ports = client.getResponce();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		HashMap<String, String> map = new HashMap<String, String>();
		String ph = porth.getItemAt(porth.getSelectedIndex());
		String bh = botelvh.getItemAt(botelvh.getSelectedIndex());
		String pw = portw.getItemAt(portw.getSelectedIndex());
		String bw = botelvw.getItemAt(botelvw.getSelectedIndex());
		map.put("configsaveh", ph+","+bh);
		map.put("configsavew", pw+","+bw);
		try {
			client.sendObject(map);
			map.clear();
			map.put("configsavew", pw+","+bw);
			client.sendObject(map);
			//Thread.sleep(500);			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
