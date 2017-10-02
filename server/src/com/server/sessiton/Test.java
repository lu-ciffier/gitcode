package com.server.sessiton;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import com.server.jopo.Parameter;
import com.server.jopo.SystemConfig;
import com.server.jopo.User;


public class Test {
	
	public static void save(){
		Session session = HibernateSessionFactory.getSession();
		session.getTransaction().begin();
		Timestamp date = new Timestamp(new Date().getTime());
		session.save(new User("luo", "123", date));
		session.getTransaction().commit();
	}
	public static void delete(){
		
	}
	public static List<User> query(){
		Session session = HibernateSessionFactory.getSession();
		List<User> list = session.createQuery("from User").list();
		return list;
	}
	public static void main(String[] args) throws NoSuchPortException, PortInUseException {
		
		//Session session = HibernateSessionFactory.getSession();
		/*Timestamp t = Timestamp.valueOf("2016-3-9 09:44:43");
		session.getTransaction().begin();
		SystemConfig config = new SystemConfig("admin", "COM5", "38400", t);
		session.save(config);*/
		
		/*Timestamp from = Timestamp.valueOf("2016-01-12 15:11:11");
		Timestamp to = Timestamp.valueOf("2016-03-10 10:54:22");
		List<Timestamp> list =  session.createQuery("select p.date from Parameter p where p.date between '"+from+"'"+"and '"+to+"'").list();
		for(Timestamp t :list){
			System.out.println(t);
		}*/
		
		//session.getTransaction().commit();
		//System.out.println(session.getEntityMode());
		//User user = (User) session.get("com.ssh.jopo.User", 7);
		/*List<User> list = query();
		for(User u : list){
			System.out.println(u.getUsername());
		}*/
		
		
		//Double s = (Double) object.get("voltage");
		//System.out.println(s);
		//Timestamp t = Timestamp.valueOf("2015-10-12 00:00:00");
		//System.out.println(t);
		//System.out.println(bijiao());
		
		//JSONObject object = new JSONObject();
		/*session.getTransaction().begin();
		Timestamp date = new Timestamp(new Date().getTime());
		session.getTransaction().begin();
		SystemConfig config = new SystemConfig("admin", "weather","COM3", "38400", date);
		session.save(config);
		session.getTransaction().commit();*/
		
		
		/*CommPortIdentifier com13PI = CommPortIdentifier
	            .getPortIdentifier("COM1");
	    CommPortIdentifier com1PI = CommPortIdentifier
	            .getPortIdentifier("COM5");
	    SerialPort com13 = (SerialPort) com13PI.open("house", 400);
	    SerialPort com1 = (SerialPort) com1PI.open("weather", 400);*/
		
	    /*com13.close();
	    //com1.close();
	    com13PI = CommPortIdentifier.getPortIdentifier("COM1");
	    com13 = (SerialPort) com13PI.open("123", 400);*/
		
		/*float price=(float) 1.2454545;
		DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		String p=decimalFormat.format(price);//format 返回的是字符串	
		float price1=(float) 89.8900;
		//int itemNum=3;
		//float totalPrice=price1*itemNum;
		float num=(float)(Math.round(price1*100))/100;//如果要求精确4位就*10000然后/10000
		System.out.println(num);*/
		File imagefile = new File("WebRoot/photos/image_.png");
		File file = new File("WebRoot/photos");
		File[] photos = file.listFiles();
		for(int i=0;i<photos.length;i++){
			File photo = photos[i];
			
			String photoname = photo.getName();
			System.out.println(photoname);
		}
	}
	
	public  static boolean bijiao(){
		Timestamp t = Timestamp.valueOf("2015-12-30 09:44:43.0");
		Timestamp x = Timestamp.valueOf("2015-12-30 00:00:00");
		if(t.getTime()>x.getTime()){
			return true;
		}else {		
			return false;
		}
	}
}
