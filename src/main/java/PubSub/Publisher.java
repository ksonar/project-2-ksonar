package PubSub;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
/*
 * Generic publisher, each takes a different file as input and publishes JSON objects to broker.
 * @author ksonar
 */

public class Publisher<T> implements Runnable {
	private Broker<T> broker;
	String fName;
	Class<T> type;
	String line;
	Gson gson = new GsonBuilder().create();
	BufferedReader f = null;
	T item;
	
	/*
	 * Constructor to initialzie the publisher.
	 * @params fName, type, broker
	 */
	
	public Publisher(String fName, Class<T> type, Broker<T> broker) {
		this.fName = fName;
		this.broker = broker;
		this.type = type;
	}	
	
	/*
	 * Each publisher will read a single JSON object at a time and sent to the broker.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int count = 0;
		long sTime = System.currentTimeMillis();
		
		try (BufferedReader f = Files.newBufferedReader(Paths.get(fName), StandardCharsets.ISO_8859_1)) {
			while((line = f.readLine()) != null) {
				item = gson.fromJson(line, type);
				broker.publish(item);
				count++;
			}
		LogData.log.info("DONE READING AND PUBLISHING : " + count);
		}
		catch (NoSuchFileException i){
			LogData.log.warning("MESSAGE : NO SUCH FILE!");
			System.exit(1);
		}
		catch (JsonSyntaxException i) {
			LogData.log.warning("MESSAGE : JSON ERROR!");
		} catch (IOException e) {
			LogData.log.warning("MESSAGE : IO ERROR!");
		} 
		long eTime = System.currentTimeMillis();
		System.out.println("TIME PUB : " + (eTime-sTime)/1000.0 + '\n');
		}
	}
	
