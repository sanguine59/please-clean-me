package singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NamePool {
	private static NamePool instance = null;
	private final List<String> availableNames = new ArrayList<>();
	private final Random random = new Random();

	private NamePool() {
		generateNames();
	}

	public static synchronized NamePool getInstance() {
		if (instance == null) {
			instance = new NamePool();
		}
		return instance;
	}

	private void generateNames() {
		for (char a = 'A'; a <= 'Z'; a++) {
			for (char b = 'A'; b <= 'Z'; b++) {
				availableNames.add("" + a + b);
			}
		}
	}

	public synchronized String addName() {
		int index = random.nextInt(availableNames.size());
		return availableNames.remove(index);
	}
}
