package PubSub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Subscriber class to write JSON objects to a file depending on unixTime
 * @author ksonar
 */

public class Subscribers<T> implements Subscriber<T> {
	private AmazonData jsonData = new AmazonData();
	private int countEqual = 0;
	private int count = 0;
	private int count1 = 0;
	private String fName;
	private int unixTime;
	private BufferedWriter write;
	public static BufferedWriter write2;
	private Broker<T> broker;
	
	public int getCount1() { return count1;}
	
	public int getCount() { return count; }
	
	public int getEqualCount() { return countEqual; }
	/*
	 * Subscriber constructor that will initialize and subscribe itself to the broker
	 * @params broker, fName, unixTime
	 */
	public Subscribers(Broker<T> broker, String fName, int unixTime) {
		this.fName = fName;
		this.unixTime = unixTime;
		this.broker = broker;
		setup();
		
		broker.subscribe(this);
		LogData.log.info("SUBSCRIBER ADDED");
	}
	
	/*
	 * Close subscriber file
	 * @see PubSub.Subscriber#close()
	 */
	@Override
	public void close()  {
		try {
			write.close();
		} catch (IOException e) {
			LogData.log.warning("UNABLE TO CLOSE FILE");
		}
		
	}
	/*
	 * Initialize a write object to append data to a file.
	 */
	private void setup() {
		try {
			write = new BufferedWriter(new FileWriter(new File(fName)));
		} catch (IOException e1) {
			LogData.log.warning("IOException @ Setup");
			System.out.println("IO ERROR @ SETUP");
		}
	}
	
	/*
	 * Filter received data to store selective JSON objects to file.
	 * @see PubSub.Subscriber#onEvent(java.lang.Object)
	 */
	@Override
	public synchronized void onEvent(T item) {
		jsonData = (AmazonData) item;
		count1++;
		
		if((jsonData.getUnixReviewTime() < unixTime && fName.equals("old.json")) || (jsonData.getUnixReviewTime() > unixTime && fName.equals("new.json")) ) {
			try {
				write.write(item.toString());
				
			} 
			catch (IOException e) {
				LogData.log.warning("IO Exception @ onEvent");
			}
				count++;
		}
		if(jsonData.getUnixReviewTime() == 1362268800) {
				countEqual++;
			
		}

	}
}
