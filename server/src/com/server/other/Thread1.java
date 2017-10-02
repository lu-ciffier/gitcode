package com.server.other;

import com.server.thread.PortDataListener;
import com.server.thread.HousePortThread;

public class Thread1 implements PortDataListener{

	@Override
	public void getPortData(String sb) {
		System.out.println(this.hashCode()+":"+sb);
	}
	
	public void PortDataListenerRegist(){
		HousePortThread portThread = new HousePortThread();
		portThread.initHousePort("COM2", 9600);
		portThread.addPortDataListener(this);
	}
	public static void main(String[] args) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Thread1 t1 = new Thread1();
				t1.PortDataListenerRegist();
			}
		}).start();
	}
}
