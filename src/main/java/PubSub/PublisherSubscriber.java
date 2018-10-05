package PubSub;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PublisherSubscriber {
	public static void main(String[] args) throws IOException, InterruptedException {
		String fName1 = "Apps_for_Android.json";
		String fName2 = "Home_and_Kitchen.json";
		long sTime = System.currentTimeMillis();
		int count = 0;
		//Broker<AmazonData> broker = new SyncBroker<AmazonData>();
		
		Broker<AmazonData> broker = new AsyncOBroker<AmazonData>();
		
		Publisher<AmazonData> p1 = new Publisher<>(fName1, AmazonData.class, broker);
		Publisher<AmazonData> p2 = new Publisher<>(fName2, AmazonData.class, broker);
	
		Subscribers s1 = new Subscribers(broker, 1);
		Subscribers s2 = new Subscribers(broker, 2);
		Thread t1 = new Thread(p1);
		Thread t2 = new Thread(p2);
		
		Thread tt = new Thread((Runnable) broker);

		
		t1.start();
		t2.start();
		tt.start();


		t1.join();
		t2.join();
		
		broker.shutdown();
		
		tt.join();

		long eTime = System.currentTimeMillis();
		System.out.println("EXEC TIME : " + ((eTime-sTime)/1000));
		System.out.println("Exited threads");

		System.out.printf("LEN OF S1 : %d \t LEN OF S2 : %d\n\n",s1.getCount(),s2.getCount());
		System.out.println(s1.countEqual + " : " + s2.countEqual);
	}

}
