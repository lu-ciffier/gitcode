package com.server.pcclient;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.server.jopo.Parameter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * @author lucyf
 * @version 2017.5.10
 * 历史数据模块
 * **/
public class HistoryJpanel extends JPanel implements ActionListener{

	private Client client;
	private List<Parameter> list;
	private JTable table;
	private JComboBox<String> point;
	private JTextField time_from;
	private JTextField time_to;
	public HistoryJpanel(Client client){
		this.client = client;
		list = new ArrayList<Parameter>();
		JPanel button = new JPanel();
		JPanel jtable = new JPanel();
		point = new JComboBox<String>();
		time_from = new JTextField("2016-03-01",8);
		time_to = new JTextField("2016-03-20",8);
		JButton bpoint = new JButton("选择节点查看");
		JButton btime = new JButton("选择时间查看");
		JButton ball = new JButton("选择全部查看");
		
		point.addItem("77");
		point.addItem("7D");
		point.addItem("D3");
		point.addItem("1B");
		point.addItem("2A");
		point.addItem("32");
		point.addItem("47");
		point.addItem("46");
		point.setBorder(BorderFactory.createTitledBorder("选择节点"));
		time_from.setBorder(BorderFactory.createTitledBorder("开始时间："));
		time_to.setBorder(BorderFactory.createTitledBorder("截至时间："));
		
		bpoint.addActionListener(this);
		btime.addActionListener(this);
		ball.addActionListener(this);
		Object[] header = {"节点","空温","空湿","土温","土湿","CO2","光照","电压","时间"};
		Object[][] data =  new Object[0][9];
		TableModel model = new DefaultTableModel(data, header);
		table = new JTable(model);
		table.setAutoscrolls(true);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane sPane = new JScrollPane(table);
		
		button.add(point);
		button.add(bpoint);
		button.add(time_from);
		button.add(time_to);
		button.add(btime);
		button.add(ball);
		button.setPreferredSize(new Dimension(150,400));
		jtable.add(sPane);
		this.add(button);
		this.add(jtable);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton jb = (JButton) e.getSource();
		String text = jb.getText();				
		if(text=="选择全部查看"){
			getData();
			fillTable(list);
		}
		if(text=="选择时间查看"){
			getData();
			List<Parameter> listt = new ArrayList<>();
			Timestamp from = Timestamp.valueOf(time_from.getText()+" 00:00:00");
			Timestamp to = Timestamp.valueOf(time_to.getText()+" 00:00:00");
			for(int i=0;i<list.size();i++){
				Parameter p = list.get(i);
				if(p.getDate().before(to)&&p.getDate().after(from)){
					listt.add(p);
				}
			}
			list = listt;
			fillTable(list);
		}
		if(text=="选择节点查看"){
			getData();
			List<Parameter> listp = new ArrayList<>();
			String ps = point.getItemAt(point.getSelectedIndex());
			for(int i=0;i<list.size();i++){
				Parameter p = list.get(i);
				if(p.getPointnum().equals(ps)){
					listp.add(p);
				}
			}
			list = listp;
			fillTable(list);
		}	
	}
	/*
	 * 获取历史数据
	 * */
	public void getData(){
		HashMap<String, String> map = new HashMap<String, String>();
		String param = "";
		list.clear();
		map.put("historydata", param);
		try {
			client.sendObject(map);
			Thread.sleep(1000);
			JSONArray ja = JSONArray.fromObject(client.getResponce());
			for (int i = 0; i < ja.size(); i++) {
				JSONObject o = ja.getJSONObject(i);
				Parameter p = new Parameter();
				p.setPoint(o.getString("point"));
				p.setPointnum(o.getString("pointnum"));
				p.setAirT(Float.valueOf(o.getString("air_tem")));
				p.setSoilT(Float.valueOf(o.getString("soil_tem")));
				p.setAirH(Integer.valueOf(o.getString("air_hum")));
				p.setSoilH(Integer.valueOf(o.getString("soil_hum")));
				p.setCo2(Integer.valueOf(o.getString("co2")));
				p.setIll(Integer.valueOf(o.getString("ill")));
				p.setVoltage(Float.valueOf(o.getString("voltage")));
				JSONObject dates = o.getJSONObject("date");
				long time = Long.valueOf(dates.getString("time"));
				Timestamp date = new Timestamp(time);
				p.setDate(date);
				list.add(p);
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	/*
	 * 显示历史数据表格
	 * */
	public void fillTable(List<Parameter> list){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);		
		for(Parameter p :list){
			String[] row = new String[9];
			row[0] = p.getPointnum();
			row[1] = p.getAirT().toString();
			row[2] = p.getAirH().toString();
			row[3] = p.getSoilT().toString();
			row[4] = p.getSoilH().toString();
			row[5] = p.getCo2().toString();
			row[6] = p.getIll().toString();
			row[7] = p.getVoltage().toString();
			row[8] = p.getDate().toString();
			model.addRow(row);
		}
		table.validate();
	}
}
