package PubSub;

import java.util.ArrayList;

public class SyncBroker<T> implements Broker {
	private static SyncBroker INSTANCE;
	private ArrayList<Subscriber> subscriberList = new ArrayList<>();
	private int count = 1;
	private SyncBroker() { }
	
	
	//<?> : https://stackoverflow.com/questions/9921676/what-does-class-mean-in-java
	public synchronized static SyncBroker<?> getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new SyncBroker();
		}
		return INSTANCE;
	}
	
	@Override
	public synchronized void  publish(Object item) {
		// TODO Auto-generated method stub
		//System.out.println("RECEIVED FROM PUB OBJECT " + count);
		count++;
		for(Subscriber s : subscriberList) {
			s.onEvent(item);
		}
		
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
		
	}

}
