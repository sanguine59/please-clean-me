package facade;

import java.util.ArrayList;
import java.util.List;

import factory.ChefFactory;
import factory.WaiterFactory;
import mediator.RestaurantMediator;
import model.Chef;
import model.ChefStat;
import model.Waiter;
import singleton.Restaurant;

public class StaffManager {
	public static final int MAX_STAFF_PER_ROLE = 7;
	public static final int MAX_STAT = 5;
	public static final int WAITER_HIRE_UNIT_COST = 150;
	public static final int CHEF_HIRE_UNIT_COST = 200;
	public static final int UPGRADE_COST = 150;

	private final RestaurantMediator mediator;
	private final Restaurant scoreboard = Restaurant.getInstance();
	private final WaiterFactory waiterFactory = new WaiterFactory();
	private final ChefFactory chefFactory = new ChefFactory();
	private final List<Waiter> waiters = new ArrayList<>();
	private final List<Chef> chefs = new ArrayList<>();

	public StaffManager(RestaurantMediator mediator) {
		this.mediator = mediator;
	}

	public List<Waiter> getWaiters() {
		return waiters;
	}

	public List<Chef> getChefs() {
		return chefs;
	}

	public int waiterHireCost() {
		return waiters.size() * WAITER_HIRE_UNIT_COST;
	}

	public int chefHireCost() {
		return chefs.size() * CHEF_HIRE_UNIT_COST;
	}

	public void seedStaff(int countWaiter, int countChef) {
		for (int i = 0; i < countWaiter; i++) {
			spawnWaiter();
		}
		for (int i = 0; i < countChef; i++) {
			spawnChef();
		}
	}

	public boolean hireNewWaiter() {
		int cost = waiterHireCost();
		if (scoreboard.getMoney() < cost) return false;
		if (waiters.size() >= MAX_STAFF_PER_ROLE) return false;

		scoreboard.setMoney(scoreboard.getMoney() - cost);
		spawnWaiter();
		return true;
	}

	public boolean hireNewChef() {
		int cost = chefHireCost();
		if (scoreboard.getMoney() < cost) return false;
		if (chefs.size() >= MAX_STAFF_PER_ROLE) return false;

		scoreboard.setMoney(scoreboard.getMoney() - cost);
		spawnChef();
		return true;
	}

	public boolean upgradeWaiterSpeed(int index) {
		if (scoreboard.getMoney() < UPGRADE_COST) return false;
		if (index < 0 || index >= waiters.size()) return false;
		Waiter selected = waiters.get(index);
		if (selected.getSpeed() >= MAX_STAT) return false;

		selected.setSpeed(selected.getSpeed() + 1);
		scoreboard.setMoney(scoreboard.getMoney() - UPGRADE_COST);
		return true;
	}

	public boolean upgradeChef(int index, ChefStat stat) {
		if (stat == null) return false;
		if (scoreboard.getMoney() < UPGRADE_COST) return false;
		if (index < 0 || index >= chefs.size()) return false;
		Chef selected = chefs.get(index);

		switch (stat) {
			case SPEED:
				if (selected.getSpeed() >= MAX_STAT) return false;
				selected.setSpeed(selected.getSpeed() + 1);
				break;
			case SKILL:
				if (selected.getSkill() >= MAX_STAT) return false;
				selected.setSkill(selected.getSkill() + 1);
				break;
			default:
				return false;
		}

		scoreboard.setMoney(scoreboard.getMoney() - UPGRADE_COST);
		return true;
	}

	private void spawnWaiter() {
		Waiter newWaiter = (Waiter) waiterFactory.createNpc(mediator);
		waiters.add(newWaiter);
		mediator.addWaiter(newWaiter);
		new Thread(newWaiter).start();
	}

	private void spawnChef() {
		Chef newChef = (Chef) chefFactory.createNpc(mediator);
		chefs.add(newChef);
		mediator.addChef(newChef);
		new Thread(newChef).start();
	}
}
