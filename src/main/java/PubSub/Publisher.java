package PubSub;

import java.util.ArrayList;

public class Publisher<T> implements Runnable {
	private ArrayList<T> jsonData;
	private Broker broker;
	
	public Publisher(ArrayList<T> data, Broker broker) {
		jsonData = data;
		this.broker = broker;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int count = 0;
		for(T f : jsonData) {
			/*
			if(count == 10) {
				break;
			}
			*/
			count++;
			broker.publish(f);
		
		}
		
		broker.shutdown();

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
