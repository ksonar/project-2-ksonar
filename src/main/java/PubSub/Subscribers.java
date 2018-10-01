package PubSub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Subscribers implements Subscriber<AmazonData>, Runnable {
	private Broker broker;
	private AmazonData jsonData = new AmazonData();
	private BlockingQueue<AmazonData> filtered = new ArrayBlockingQueue<>(100);
	ArrayList<AmazonData> filterData = new ArrayList<>();
	int countEqual = 0;
	int countOther = 0;
	private int count = 1;
	private int mode;

	private BufferedWriter write;
	//BufferedWriter write = new BufferedWriter(new FileWriter(new File(outputFile)));
	//BufferedWriter write = new BufferedWriter(new FileWriter(new File(outputFile)));

	
	public Subscribers(Broker broker, int mode) {
		this.broker = broker;
		this.mode = mode;

		System.out.println(jsonData.getClass());
		broker.subscribe(this);

	}
	
		
	@Override
	public void onEvent(AmazonData item) {
		// TODO Auto-generated method stub
		//type.
		item = (AmazonData) item;
		if(item.getUnixReviewTime() < 1362268800 && mode == 1) {
			filterData.add(item);
			//if(count<100)
			//	System.out.println("OLD");
			//filtered.add(item);
		}
		else if(item.getUnixReviewTime() > 1362268800 && mode == 2) {
			filterData.add(item);
			//if(count<100)
			//	System.out.println("NEW");
			//filtered.add(item);
		}
		else if (item.getUnixReviewTime() == 1362268800){
			countEqual++;
		}
		//else {
		//	countOther++;
		//}

		//System.out.println("RECEIEVED FROM BROKER OBJECT : " + count + "MODE : " + mode + " \n " + item.toString());
		count++;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("RUN RUN RUN " + mode);
	}
	
	public void display() {
		for(AmazonData a : filterData) {
			System.out.println(a.toString());
		}
	}
	
	public void process() throws IOException {
		String outputFile;
		System.out.println("HEY");
		if(mode == 1) {
			outputFile = "old.json";
		}
		else {
			outputFile = "new.json";
		}
		try {
			write = new BufferedWriter(new FileWriter(new File(outputFile)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(outputFile);
		for(AmazonData a : filterData) {
			//System.out.println("...");
			write.write(a.toString() + "\n");
		}
		
		write.close();

	}


}
