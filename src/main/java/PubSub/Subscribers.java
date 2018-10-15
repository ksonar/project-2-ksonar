package PubSub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Subscribers implements Subscriber<AmazonData> {
	private Broker<AmazonData> broker;
	private AmazonData jsonData = new AmazonData();
	//private T jsonData = new type();
	int countEqual = 0;
	int countOther = 0;
	int count = 0;
	private int count1 = 0;
	private int count2 = 0;
	private String fName;
	String outputFile;

	private BufferedWriter write;
	public static BufferedWriter write2;
	public int getCount1() { return count1;}
	public int getCount2() { return count2;}
	
	public int getCount() { return count; }
	
	public int getEqualCount() { return countEqual; }
	
	public Subscribers(Broker broker, String fName) {
		this.broker = broker;
		this.fName = fName;
		System.out.println(fName);
		setup();
		//broker.s
		broker.subscribe(this);
		/*
		try {
			write = new BufferedWriter(new FileWriter(fName));
			write2 = new BufferedWriter(new FileWriter("equals.json"));
			//write = Files.newBufferedWriter(Paths.get(fName), StandardCharsets.ISO_8859_1);
			//write2 = Files.newBufferedWriter(Paths.get("equal.json"), StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	@Override
	public void close()  {
		System.out.println("CLOSING");
		try {
			write.close();
			write2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public synchronized void onEvent(AmazonData item) {
		// TODO Auto-generated method stub
		jsonData = (AmazonData) item;
		//System.out.println(count1 + " : " + jsonData);
		count1++;
		if((item.getUnixReviewTime() < 1362268800 && fName.equals("old.json")) || (item.getUnixReviewTime() > 1362268800 && fName.equals("new.json")) ) {
			if(fName.equals("old.json") && count >= 13600)
			{	
				count2++;
				//System.out.println(count2 + " : " + item.toString().replaceAll("\r\n", ""));
			}
			try {
				write.write(item.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
		}

		else if (item.getUnixReviewTime() == 1362268800){
			countEqual++;
			//synchronized(this) {
			try {
			write2.write(jsonData.toString() + "\n");
			}
			catch (IOException i) {
				System.out.print("IO ERROR");
			//}
			}
		}
	}
	
	private void setup() {
		outputFile = fName;
		try {
			write = new BufferedWriter(new FileWriter(new File(outputFile)));
			write2 = new BufferedWriter(new FileWriter(new File("equal.json")));
		} catch (IOException e1) {
			System.out.println("IO ERROR @ SETUP");
		}
		System.out.println(outputFile);
	}
}
