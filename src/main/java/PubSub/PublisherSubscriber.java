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
		ArrayList<Publisher> pubList = new ArrayList<>();
		long sTime = System.currentTimeMillis();
		// FileData<AmazonData> fd = new FileData().setType(AmazonData.class).read(fName);
		ArrayList<AmazonData> f1 = new DataBuilder<AmazonData>().setType(AmazonData.class).read(fName1).build();
		ArrayList<AmazonData> f2 = new DataBuilder<AmazonData>().setType(AmazonData.class).read(fName2).build();
		int count = 0;
		SyncBroker syncBroker = SyncBroker.getInstance();
		Publisher<AmazonData> p1 = new Publisher<>(f1, syncBroker);
		Publisher<AmazonData> p2 = new Publisher<>(f2, syncBroker);
		Subscribers s1 = new Subscribers(syncBroker, 1);
		Subscribers s2 = new Subscribers(syncBroker, 2);
		Thread t1 = new Thread(p1);
		Thread t2 = new Thread(p2);
		Thread t3 = new Thread(s1);
		Thread t4 = new Thread(s2);
		int total = f1.size() + f2.size();
		pubList.add(p1);
		pubList.add(p2);
		
		syncBroker.registerPubList(pubList);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		//syncBroker.shutdown();

		t1.join();
		t2.join();
		t3.join();
		t4.join();
		
		if((p1.getRunning() == false) && (p2.getRunning() == false)) {
			System.out.println("RUNNING FALSE FOR BOTH< CALLING SHUTDOWN()");
			syncBroker.shutdown();
		}
		
		System.out.println("Exited threads");
		s1.process();
		s2.process();
		
		long eTime = System.currentTimeMillis();
		System.out.println("EXEC TIME : " + ((eTime-sTime)/1000));
		
		System.out.println("TOTAL SIZE : " + total);
		System.out.printf("LEN OF S1 : %d \t LEN OF S2 : %d\n\n",s1.filterData.size(),s2.filterData.size());
		System.out.println(s1.countEqual + " : " + s2.countEqual);
		System.out.println("\n\n" + s1.countOther + " : " + s2.countOther);
		//System.out.printf("LEN OF S1 FILE : %d \t LEN OF S2 FILE : %d",s1.o,s2.filterData.size());

		//p1.display();
		/*
		System.out.println("\n~~~S1~~~\n");
		s1.display();
		System.out.println("\n~~~S2~~~\n");
		s2.display();
		*/
	}

}
