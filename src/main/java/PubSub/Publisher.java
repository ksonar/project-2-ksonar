package PubSub;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Publisher<T> implements Runnable {
	private Broker<T> broker;
	String fName;
	Class<T> type;
	
	public Publisher(String fName, Class<T> type, Broker<T> broker) {
		this.fName = fName;
		this.broker = broker;
		this.type = type;
	}	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int count = 0;
		int counter = 0;
		String line;
		Gson gson = new GsonBuilder().create();
		BufferedReader f = null;
		T item;
		
		try {
			f = Files.newBufferedReader(Paths.get(fName), StandardCharsets.ISO_8859_1);
			while((line = f.readLine()) != null) {
				item = gson.fromJson(line, type);
				broker.publish(item);
				count++;
				counter++;
				//System.out.println("Data from ");
				//data.add(gson.fromJson(line, type));
			}
		System.out.println("DONE READING AND PUBLISHING : " + count);
		}
		catch (NoSuchFileException i){
			System.out.println("MESSAGE : NO SUCH FILE!");
			System.exit(1);
		}
		catch (JsonSyntaxException i) {
			System.out.println("MESSAGE : JSON error!");
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
