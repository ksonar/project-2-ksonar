package PubSub;

import java.util.ArrayList;

/*
 * Config class to load data from the configuration file
 * @author ksonar
 */

public class Config {
	private ArrayList<String> pubFiles;
	private ArrayList<String> subFiles;
	private String brokerType;
	private int unixTime;

	public String toString() {
		return pubFiles + "\t" + subFiles + "\t" + brokerType + '\t' + unixTime;
		
	}
	
	//getters
	public ArrayList<String> pubs() { return pubFiles; }
	public ArrayList<String> subs() { return subFiles; }
	public String type() { return brokerType; }
	public int unix() { return unixTime; }

}
