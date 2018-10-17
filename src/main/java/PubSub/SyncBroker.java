package PubSub;

import java.util.ArrayList;
/*
 * SynchronousBroker will take a single JSON object and send to all subscribers and only then take a new JSON object.
 * @author ksonar
 */
public class SyncBroker<T> implements Broker<T> {
	private ArrayList<Subscriber<T>> subscriberList = new ArrayList<>();
	
	@Override
	public synchronized void  publish(T item) {
		for(Subscriber<T> s : subscriberList) {
			s.onEvent(item);
		}
	}

	@Override
	public void subscribe(Subscriber<T> subscriber) {
		subscriberList.add(subscriber);
	}

	/*
	 * Close all files that are open for writing
	 * @see PubSub.Broker#shutdown()
	 */
	@Override
	public void shutdown() {
	}
}
