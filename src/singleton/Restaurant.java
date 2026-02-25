package singleton;

public class Restaurant {
	private static Restaurant instance = null;
	private int score;
	private int money;
	private String name;
	
	public static synchronized Restaurant getInstance() {
		if(instance == null) {
			instance = new Restaurant();
		}
		return instance;
	}
	
	private Restaurant() {
		this.score = 0;
		this.money = 1300;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
