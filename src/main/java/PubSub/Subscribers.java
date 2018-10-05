package PubSub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Subscribers implements Subscriber<AmazonData>, Runnable {
	private Broker<AmazonData> broker;
	private AmazonData jsonData = new AmazonData();
	int countEqual = 0;
	int countOther = 0;
	int count = 0;
	private int count1 = 0;
	private int count2 = 0;
	private int mode;
	String outputFile;

	private BufferedWriter write;
	public int getCount1() { return count1;}
	public int getCount2() { return count2;}
	
	public int getCount() { return count; }
	
	public Subscribers(Broker<AmazonData> broker, int mode) {
		this.broker = broker;
		this.mode = mode;
		broker.subscribe(this);
		setup();
	}
	
		
	@Override
	public void onEvent(AmazonData item) {
		// TODO Auto-generated method stub
		item = (AmazonData) item;
		if((item.getUnixReviewTime() < 1362268800 && mode == 1) || (item.getUnixReviewTime() > 1362268800 && mode == 2) ) {
			try {
			write.write(item.toString() + "\n");
			}
			catch (IOException i) {
				System.out.print("IO ERROR");
			}
			count++;

		}

		else if (item.getUnixReviewTime() == 1362268800){
			countEqual++;
		}
		
		//if ( (count + countEqual) % 100000 == 0) {
		//	System.out.println("RECEVIED : " + (count+countEqual));
		//}
		//System.out.println("RECEIEVED FROM BROKER OBJECT : " + count + "MODE : " + mode + " \n " + item.toString());
		
	}
	
	
	private void setup() {
		if(mode == 1) {
			outputFile = "old.json";
		}
		else {
			outputFile = "new.json";
		}
		try {
			write = new BufferedWriter(new FileWriter(new File(outputFile)));
		} catch (IOException e1) {
			System.out.println("IO ERROR @ SETUP");
		}
		System.out.println(outputFile);
	}

	@Override
	public void run() {
		
	}

	
}
