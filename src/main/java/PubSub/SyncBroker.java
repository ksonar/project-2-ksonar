package PubSub;

import java.util.ArrayList;

public class SyncBroker<T> implements Broker {

	private ArrayList<Subscriber<T>> subscriberList = new ArrayList<>();
	private int count = 1;
	
	@Override
	public synchronized void  publish(Object item) {
		// TODO Auto-generated method stub
		//System.out.println("RECEIVED FROM PUB OBJECT " + count);
		//System.out.println(item);
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
