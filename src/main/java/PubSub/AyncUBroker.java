package PubSub;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * AsynUnorderedBroker will run tasks concurrently with a FixedThreadPool and an unbounded queue that will receive new tasks.
 * @author ksonar
 */

public class AyncUBroker<T> implements Broker<T> {
	ExecutorService executorService = Executors.newFixedThreadPool(4);
	private ArrayList<Subscriber<T>> subscriberList = new ArrayList<>();

	/*
	 * ExecutorService to execute jobs in the worker queue
	 * @see PubSub.Broker#publish(java.lang.Object)
	 */
	@Override
	public void publish(T item) {
		executorService.execute(new Runnable() {
		    public void run() {
				for(Subscriber<T> s : subscriberList) {
						s.onEvent(item);
				}		        
		    }
		});
	}

	@Override
	public void subscribe(Subscriber<T> subscriber) {
		subscriberList.add(subscriber);
	}
	
	/*
	 * Gracefully shutdown the broker, using await termination to block until all threads have finished
	 * @see PubSub.Broker#shutdown()
	 */
	@Override
	public void shutdown() {
		executorService.shutdown();
		try {
			while(!executorService.awaitTermination(200, TimeUnit.MILLISECONDS)) {}
		} catch (InterruptedException e1) {
			LogData.log.warning("COULD NOT SHUTDOWN PROPERLY");
		}
	}

}
