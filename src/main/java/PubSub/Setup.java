package PubSub;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/*
 * Class to read from config file and setup all resources for the Pub-Sub framework
 * @author ksonar
 */

public class Setup<T> {

	private String cFile;
	private Config c;
	private Class<T> type;
	private ArrayList<Publisher<T>> publishers = new ArrayList<>();
	private ArrayList<Subscribers<T>> subscribers = new ArrayList<>();
	private Broker<T> broker;
	
	private ArrayList<Thread> publisherThreads = new ArrayList<>();
	private Thread brokerThread = new Thread();
	
	private long sTime, eTime;
	
	public void displayCount() {
		String x = String.format("\nOLD : %d \t NEW : %d\t EQUAL : %d %d \t RECEIVED : %d %d",subscribers.get(0).getCount(),subscribers.get(1).getCount(),subscribers.get(0).getEqualCount(),subscribers.get(1).getEqualCount(),subscribers.get(0).getCount1(),subscribers.get(1).getCount1());
		System.out.println(x);
		LogData.log.info(x + "\n\nDONE\n~~~~~~~");
	}
	
	/*
	 * Constructor that reads and builds the required configurations
	 * @params cFile, type
	 */
	public Setup(String cFile, Class<T> type) throws IOException {
		this.cFile = cFile;
		this.type = type;
		read();
		build();
	}

	/*
	 * Read the config file and store into an object
	 */
	public void read() {
		Gson gson = new GsonBuilder().create();
		try {
		BufferedReader f = Files.newBufferedReader(Paths.get(cFile));
		c = gson.fromJson(f, Config.class);
		System.out.println(c.toString() + '\n');
		LogData.log.info(c.toString());
		}
		catch (IOException | NullPointerException i) {
			LogData.log.warning("NO SUCH FILE");
			System.out.println("NO SUCH FILE");
			System.exit(1);
		}
		catch (JsonSyntaxException i) {
			LogData.log.warning("NO SUCH FILE");
		}		
	}
	
	/*
	 * Setup broker, publishers and subscribers and required threads
	 */
	public void build() throws IOException {
		setBroker();
		setPub();
		setSub();
	}
	
	/*
	 * Start all required threads
	 */
	public void start() {
		sTime = System.currentTimeMillis();
		for(Thread t : publisherThreads) {
			t.start();
			LogData.log.info("PUB THREAD STARTED : " + t);
		}
		
		if((c.type().equals("AsyncOrdered"))) {
			brokerThread.start();
			LogData.log.info("ASYNCORDERED BROKER THREAD STARTED : " + brokerThread);
		}
		
	}
	
	public void join() {
		for(Thread t : publisherThreads) {
			try {
				t.join();
				LogData.log.info("PUB THREAD JOINED : " + t);
				eTime = System.currentTimeMillis();
				LogData.log.info("PUB EXEC TIME : "+ (eTime-sTime)/1000.0);
			} catch (InterruptedException e) {
				LogData.log.warning("THREAD JOIN ERROR");
			}
		}
		broker.shutdown();
		System.out.println("SHUTDOWN EXEC TIME : "+ (eTime-sTime)/1000.0);
		if((c.type().equals("AsyncOrdered"))) {
			try {
				brokerThread.join();
				LogData.log.info("ASYNCORDERD THREAD JOINED : " + brokerThread);
			} catch (InterruptedException e) {
				LogData.log.warning("ASYNCORDERED BROKER THREAD JOIN ERROR");
			}
		}
		for(Subscribers<T> s : subscribers) {
			s.close();
		}
		LogData.log.info("CLOSED SUBSCRIBER FILES");
	}
	
	public void setBroker() {
		String brokerType = c.type();
		if(brokerType.equals("Sync")) {
			broker = new SyncBroker<T>();
		}
		else if(brokerType.equals("AsyncOrdered")) {
			broker = new AsyncOBroker<T>();
			brokerThread = new Thread((Runnable) broker);
		}
		else if(brokerType.equals("AsyncUnordered")) {
			broker = new AyncUBroker<T>();
		}
		else {
			LogData.log.warning("INVALID BROKER, please try again");
			System.exit(1);
		}
	}
	
	public void setPub() {
		for(String fName : c.pubs()) {
			publishers.add(new Publisher<T>(fName, type, broker));
		}
		LogData.log.info("#Pubs : " + publishers.size());
		for(Publisher<T> p : publishers) {
			publisherThreads.add(new Thread(p));
		}
		LogData.log.info("#PubThreads : " + publisherThreads.size());
	}
	
	public void setSub() throws IOException {
		for(String fName : c.subs()) {
			subscribers.add(new Subscribers(broker, fName, c.unix()));
		}
		LogData.log.info("#Subs : " + subscribers.size());
		
	}
}
