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

public class DataBuilder<T> {
	private ArrayList<T> data = new ArrayList<>();;
	private Class<T> type;
	
	public DataBuilder<T> setType(Class<T> type) {
		this.type = type;
		return this;
	}
	
	public DataBuilder<T> read(String fName) throws IOException {
		String line;
		Gson gson = new GsonBuilder().create();
		BufferedReader f = null;
		
		try {
			f = Files.newBufferedReader(Paths.get(fName), StandardCharsets.ISO_8859_1);
			while((line = f.readLine()) != null) {
				data.add(gson.fromJson(line, type));
			}
		}
		catch (NoSuchFileException i){
			System.out.println("MESSAGE : NO SUCH FILE!");
			System.exit(1);
		}
		catch (JsonSyntaxException i) {
			System.out.println("MESSAGE : JSON error!");
		}
		System.out.println("DONE READING");
		
		return this;
	}
	
	public ArrayList<T> build() {
		return data;
	}
}
