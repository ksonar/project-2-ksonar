package PubSub;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AsyncOBroker<T> implements Broker, Runnable {
	//BlockingQueue<T> dataQueue = new ArrayBlockingQueue(100);
	PubSubBlockingQueue<T> dataQueue = new PubSubBlockingQueue(1000);
	private ArrayList<Subscriber<T>> subscriberList = new ArrayList<>();
	
	private boolean run = true;
	
	private boolean runningCheck = false;
	
	private boolean getRun() { return run; }
	
	@Override
	public void publish(Object item) {
		// TODO Auto-generated method stub
		//T a = (T) item;
		dataQueue.put((T) item);

		//for(T data : dataQueue) {
		//	System.out.println(data.toString());
		//}
		//System.out.println("........"+dataQueue.size());
		
	}

	@Override
	public void subscribe(Subscriber subscriber) {
		// TODO Auto-generated method stub
		System.out.println("ADDING NEW SUBSCRIBER");
		subscriberList.add(subscriber);
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		run = false;
		System.out.println("FALSE SET");
		runningCheck = true;
		
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("HII");
		T item;
		while(getRun()) {
			pushing();
			//if(runningCheck == true) {
			//	System.out.println("CHECKING");
			//}
		}
		System.out.println("STATE IS : " + run);
		
		for(int i = 0; i < dataQueue.getSize(); i++) {
			item = dataQueue.take();
			for(Subscriber<T> s : subscriberList) {
				s.onEvent(item);
			}
		}
		
		//for(T data : dataQueue) {
		//	for(Subscriber<T> s : subscriberList) {
		//		s.onEvent(data);
		//	}
		//}
	System.out.println("ASYNC THREAD DONE");

		
	}
	
	public void pushing() {
		T item;
		if (dataQueue.getSize() > 1) {
			item = dataQueue.take();
			for(Subscriber<T> s : subscriberList) {
				s.onEvent(item);
			}
		}



	}
	
	

}
