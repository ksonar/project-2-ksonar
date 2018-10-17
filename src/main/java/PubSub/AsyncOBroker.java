package PubSub; 

import java.util.ArrayList;

/*
 * AsynchronousOrderedBroker that will take in data without synchronization and push objects to susbcribers in an orderly manner
 * @author ksonar
 */

public class AsyncOBroker<T> implements Broker<T>, Runnable {
	
	PubSubBlockingQueue<T> dataQueue = new PubSubBlockingQueue<T>(20);
	
	private ArrayList<Subscriber<T>> subscriberList = new ArrayList<>();

	private boolean run = true;
	int count = 0;

	/*
	 * Return running state of broker thread
	 */
	private boolean getRun() { return run; }
	
	@Override
	public void publish(T item) {
		dataQueue.put(item);
		//count();
	}


	@Override
	public void subscribe(Subscriber<T> subscriber) {
		subscriberList.add(subscriber);
	}
	

	/*
	 * Update running state to false, called after publishers have finished publishing
	 * @see PubSub.Broker#shutdown()
	 */
	@Override
	public void shutdown() {
		run = false;
	}

	/*
	 * Check running state, stop more s'onEvent' calls when run is false and size of queue is also 0
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		T item;
		while((getRun() || dataQueue.getSize() != 0)) {
			if (!dataQueue.isEmpty()) {
				item = dataQueue.take();
				for(Subscriber<T> s : subscriberList) {
					s.onEvent(item);
				}
			}
		}
	}
		

}
