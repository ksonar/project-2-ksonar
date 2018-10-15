package PubSub; 

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AsyncOBroker<T> implements Broker<T>, Runnable {

	PubSubBlockingQueue<T> dataQueue = new PubSubBlockingQueue<T>(10);
	
	private ArrayList<Subscriber<T>> subscriberList = new ArrayList<>();
	private int countPubItems = 0;
	private boolean run = true;
	
	//private boolean runningCheck = false;
	
	private boolean getRun() { return run; }
	
	public int getCountPubItems() { return countPubItems; }
	
	@Override
	public void publish(Object item) {
		// TODO Auto-generated method stub
		T a = (T) item;
		dataQueue.put(a);
		count();
	}
	
	public synchronized void count() {
		countPubItems++;
	}

	@Override
	public void subscribe(Subscriber<T> subscriber) {
		// TODO Auto-generated method stub
		System.out.println("ADDING NEW SUBSCRIBER");
		subscriberList.add(subscriber);
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		run = false;
		System.out.println("FALSE SET");
		System.out.println("RECEIVED " + countPubItems + " records from all publishers ");
		//runningCheck = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("HII");
		T item;
		while(getRun()) {
			pushing();
		}
		
		for(int i = 0; i < dataQueue.getSize(); i++) {
			item = dataQueue.take();
			for(Subscriber<T> s : subscriberList) {
				s.onEvent(item);
			}
		}
	}
	
	public void pushing() {
		T item;
		//if (dataQueue.getSize() > 1) {
		if (dataQueue.getSize() != 0) {
			item = dataQueue.take();
			for(Subscriber<T> s : subscriberList) {
				s.onEvent(item);
			}
		}
	}
	
	

}
