package PubSub;

import java.util.ArrayList;

public class SyncBroker<T> implements Broker<T> {

	private ArrayList<Subscriber<T>> subscriberList = new ArrayList<>();
	private int count = 1;
	
	@Override
	public synchronized void  publish(Object item) {
		// TODO Auto-generated method stub
		count++;
		T data = (T) item;
		for(Subscriber<T> s : subscriberList) {
			s.onEvent(data);
		}
	}

	@Override
	public void subscribe(Subscriber<T> subscriber) {
		// TODO Auto-generated method stub
		System.out.println("ADDING NEW SUBSCRIBER");
		subscriberList.add(subscriber);
		
	}

	@Override
	public void shutdown() {
		
		for(Subscriber<T> s : subscriberList) {
			s.close();
		}

	}

}
