package PubSub;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Publisher<T> implements Runnable {
	private ArrayList<T> jsonData;
	private Broker broker;
	boolean flag = false;
	
	//private AtomicBoolean running = new AtomicBoolean(true);
	private boolean running = true;
	
	//public boolean getRunning() { return running.get(); }
	public boolean getRunning() { return running; }
	
	//public void setRunning() { this.running.set(false); }
	public void setRunning() { running = false; }
	
	public Publisher(ArrayList<T> data, Broker broker) {
		jsonData = data;
		this.broker = broker;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int count = 0;
		int len = jsonData.size();
		System.out.println(len);
		//display();
		//while(running.get() && count < len) {
		for(T f : jsonData) {
			//System.out.println(running.get() + " : " + count);
			//if(running.get() && count < len) {
			if(running == true) {
				//System.out.println(count);
				//if(count == 10) {
				//	break;
				//}
				count++;
				broker.publish(f);
			} 
			else {
				//running.set(false);
				running = false;
				break;
			}
			
		
		//System.out.println("RUNNING : " + running);
	}
		
		System.out.println("NOT PUBLISHING ANYMORE : " + count);

	}
	

	
	public void display() {
		
		int count = 0;
		System.out.println("LENGTH " + jsonData.size());
		for(T f : jsonData) {
			if(count == 10) {
				break;
			}
			
			System.out.println(f.toString());
			count++;
		}
	}

}
