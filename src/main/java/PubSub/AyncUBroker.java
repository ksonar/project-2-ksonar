package PubSub;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AyncUBroker<T> implements Broker<T>, Runnable {
	//LinkedList<T> dataQueue = new LinkedList<>();
	ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	private ArrayList<Subscriber<T>> subscriberList = new ArrayList<>();
	int count = 0;
	@Override
	public void publish(Object item) {
		// TODO Auto-generated method stub
		synchronized(this) { count++; }
		executorService.execute(new Runnable() {
		    public void run() {
		    	T data = (T) item;
				for(Subscriber<T> s : subscriberList) {
					s.onEvent(data);
				}
		        //System.out.println(count);
		        
		    }
		});
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
		executorService.shutdown();
		try {
			while(!executorService.awaitTermination(200, TimeUnit.MILLISECONDS)) {}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//
		/*
		try {
			while(!executorService.awaitTermination(2000, TimeUnit.MILLISECONDS)) {}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executorService.shutdown();
		*/
		System.out.println("SHUTDOWN : " + count);
		
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("AsyncUnordered run");
	}

}
