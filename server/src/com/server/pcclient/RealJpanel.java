package com.server.pcclient;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.server.jopo.Parameter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * @author lucyf
 * @version 2017.5.10
 * 实时数据模块
 * **/
public class RealJpanel extends JPanel implements ActionListener, TableCellRenderer{

	private Client client;
	private JTable table,tpoint;
	private JButton start,stop,collect,pause;
	private List<String> checkpoint;
	private List<Parameter> list;
	private boolean readding=false;
	private static final String FOOTER = "000000000000000000000000000000000000002A";
	public RealJpanel(Client client){
		this.client = client;
		JPanel jbutton = new JPanel();
		JPanel jtable = new JPanel();
		start = new JButton("开始");
		stop= new JButton("停止");
		collect = new JButton("采集");
		pause = new JButton("暂停");
		start.addActionListener(this);
		stop.addActionListener(this);
		collect.addActionListener(this);
		pause.addActionListener(this);
		checkpoint = new ArrayList<String>();
		list = new ArrayList<Parameter>();
		Object[] header = {"节点","空温","空湿","土温","土湿","CO2","光照","电压","时间"};
		Object[] tpheader = {"选择","节点","地址"};
		Object[][] data =  new Object[0][];
		TableModel model = new DefaultTableModel(data, header);
		TableModel tpmodel = new DefaultTableModel(data, tpheader);
		table = new JTable(model);
		tpoint = new JTable(tpmodel);
		table.setAutoscrolls(true);
		tpoint.setPreferredScrollableViewportSize(new Dimension(180,130));
		tpoint.setAutoscrolls(true);
		hideColumn(tpoint, 2);
		tpoint.getColumnModel().getColumn(0).setCellRenderer(this);
		//tpoint.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane tPane = new JScrollPane(table);
		JScrollPane pPane = new JScrollPane(tpoint);
		
		jbutton.add(start);
		jbutton.add(stop);
		jbutton.add(pPane);
		jbutton.add(collect);
		jbutton.add(pause);
		jbutton.setPreferredSize(new Dimension(200,400));
		jtable.add(tPane);
		this.add(jbutton);
		this.add(jtable);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton o = (JButton) e.getSource();
		String text = o.getText();
		if(text=="开始"){
			HashMap<String, String> map = new HashMap<String, String>();
			String command = "";
			readding = true;			
			map.put("getpoint", command);
			try {
				client.sendObject(map);
				Thread.sleep(1500);
				String result = client.getResponce();
				if(result.contains("<br>")){
					String[] point = result.split("<br>");
					fillpTable(point);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			new Thread(new Runnable() {		
				@Override
				public void run() {
					readParameter();
				}
			}).start();
		}
		if(text=="采集"){
			
			int rows[] = tpoint.getSelectedRows();
			if(rows.length>0){
				for(int row :rows){
					HashMap<String, String> map = new HashMap<String, String>();
					String addr = tpoint.getValueAt(row, 2).toString();
					map.put("collect", "26525341"+addr+FOOTER);
					try {
						client.sendObject(map);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}			
		}
		if(text=="暂停"){
			
			int rows[] = tpoint.getSelectedRows();
			if(rows.length>0){
				for(int row :rows){
					HashMap<String, String> map = new HashMap<String, String>();
					String addr = tpoint.getValueAt(row, 2).toString();
					map.put("collect", "26545341"+addr+FOOTER);
					try {
						client.sendObject(map);
					} catch (IOException e1) {						
						e1.printStackTrace();
					}					
				}
			}			
		}
		if(text=="停止"){
			readding = false;								
		}
	}
	/*
	 * 获取温室实时数据
	 * */
	public void readParameter(){
		HashMap<String, String> map = new HashMap<String, String>();
		String command = "";
		map.put("getparameter", command);
		while(readding){			
			try {
				client.sendObject(map);
				Thread.sleep(1500);
				String responce = client.getResponce();
				if(responce.startsWith("[")){
					list.clear();
					JSONArray ja = JSONArray.fromObject(responce);
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
					fillTable(list);
				}
				Thread.sleep(1000);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	/*
	 * 隐藏表行
	 * */
	protected void hideColumn(JTable table,int index){ 
		TableColumn tc= table.getColumnModel().getColumn(index); 
		tc.setMaxWidth(0); 
		tc.setPreferredWidth(0); 
		tc.setMinWidth(0); 
		tc.setWidth(0); 
		table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0); 
		table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0); 
	}
	/*
	 * 显示节点列表
	 * */
	public void fillpTable(String[] list){
		DefaultTableModel model = (DefaultTableModel) tpoint.getModel();
		model.setRowCount(0);		
		for(int i=0;i<list.length;i++){
			Object[] row = new Object[3];
			row[0] = "";
			row[1]	= (list[i].split(","))[0];
			row[2]	= (list[i].split(","))[1];
			model.addRow(row);
		}
		tpoint.validate();
	}
	/*
	 * 显示实时数据列表
	 * */
	public void fillTable(List<Parameter> list){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		//model.setRowCount(0);
		for(Parameter p :list){
			if(table.getRowCount()>0){
				for(int i=0;i<table.getRowCount();i++){
					String id = table.getValueAt(i, 0).toString();
					if(p.getPointnum().equals(id)){
						model.removeRow(i);
					}
				}
			}
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
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JCheckBox box = new JCheckBox();
		box.setSelected(isSelected);
		box.setHorizontalAlignment((int)0.5f);
		//tpoint.getValueAt(tpoint.getSelectedRow(), 1);
		
		return box;
	}
}
