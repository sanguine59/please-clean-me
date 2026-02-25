package singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NamePool {
	private static NamePool instance = null;
	private List<String> availableNames;
	private List<String> usedNames;
	private Random random;
	private NamePool() {
		this.availableNames = new ArrayList<String>();
		this.usedNames = new ArrayList<String>();
		this.random = new Random();
		generateNames();
	}
	
	public static synchronized NamePool getInstance() {
		if(instance == null) {
			instance = new NamePool();
		}
		return instance;
	}
	
	
	public void generateNames() {
		for(char a = 'A'; a <= 'Z'; a++) {
			for(char b = 'A'; b <= 'Z'; b++) {
				availableNames.add(""+a+b);
			}
		}
	}
	
	
	public synchronized String addName() {
		int index = random.nextInt(availableNames.size());
		String name = availableNames.remove(index);
		
		usedNames.add(name);
		return name;
	}
	
	public synchronized void removeName(String name) {
		if(usedNames.remove(name)) {
			availableNames.add(name);
		}
	}
	
	
}
