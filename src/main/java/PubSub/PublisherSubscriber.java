package PubSub;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
import java.util.logging.Logger;
public class PublisherSubscriber {
	final static Logger log = Logger.getLogger(PublisherSubscriber.class.getName());
	//final static Logger logger = LogManager.getLogger(PublisherSubscriber.class);
	public static void main(String[] args) throws IOException, InterruptedException {
		log.info("WORKING MACHA");
		log.info("MY LOGGER IS WORKING");
		Setup<AmazonData> setup = new Setup<AmazonData>("config.json", AmazonData.class);
		System.out.println("\n~~~~~~~~~~~~~~~~~\n");
		long sTime = System.currentTimeMillis();
		setup.start();
		setup.join();
		long eTime = System.currentTimeMillis();
		System.out.println("EXEC TIME : " + ((eTime-sTime)/1000.0));
		setup.displayCount();
		/*
		
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

		long sTime1 = System.currentTimeMillis();
		t1.start();
		long sTime2 = System.currentTimeMillis();
		t2.start();
		long sTime3 = System.currentTimeMillis();
		tt.start();
		
		
	


		t1.join();
		long eTime1 = System.currentTimeMillis();
		
		t2.join();
		long eTime2 = System.currentTimeMillis();
		
		broker.shutdown();
		
		tt.join();
		long eTime3 = System.currentTimeMillis();
		
		System.out.println("EXEC TIME P1 : " + ((eTime1-sTime1)/1000.0));
		System.out.println("EXEC TIME P2 : " + ((eTime2-sTime2)/1000.0));
		System.out.println("EXEC TIME Async: " + ((eTime3-sTime3)/1000.0));

		long eTime = System.currentTimeMillis();
		System.out.println("EXEC TIME : " + ((eTime-sTime)/1000.0));
		System.out.println("Exited threads");

		System.out.printf("LEN OF S1 : %d \t LEN OF S2 : %d\n\n",s1.getCount(),s2.getCount());
		System.out.println(s1.countEqual + " : " + s2.countEqual);
		*/
		
	}

}
