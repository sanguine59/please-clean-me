package facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import factory.CustomerFactory;
import main.Main;
import main.Reader;
import mediator.RestaurantMediator;
import model.Chef;
import model.ChefStat;
import model.Customer;
import model.Waiter;
import singleton.Restaurant;

public class GameFacade {
	public static final int INITIAL_SEATS = 4;
	public static final int MAX_SEATS = 13;
	public static final int SEAT_EXPANSION_UNIT_COST = 100;
	public static final int CUSTOMER_SPAWN_CHANCE_PERCENT = 25;
	public static final long GAME_TICK_MS = 1000L;
	public static final int STARTING_WAITERS = 2;
	public static final int STARTING_CHEFS = 2;

	private final Restaurant scoreboard = Restaurant.getInstance();
	private final RestaurantMediator mediator = new RestaurantMediator();
	private final CustomerFactory customerFactory = new CustomerFactory();
	private final StaffManager staff = new StaffManager(mediator);
	private final List<Customer> seats = new ArrayList<>();
	private final Random random = new Random();
	private final Main main;
	private int maxSeats = INITIAL_SEATS;

	public GameFacade(Main main) {
		this.main = main;
	}

	public void startGame(String restaurantName) {
		scoreboard.setName(restaurantName);
		staff.seedStaff(STARTING_WAITERS, STARTING_CHEFS);

		new Thread(new Reader(mediator, main)).start();
		new Thread(this::mainGameLoop).start();
	}

	public void pauseGame() {
		mediator.pauseSimulation();
	}

	public void resumeGame() {
		mediator.resumeSimulation();
	}

	public Restaurant getScoreboard() {
		return scoreboard;
	}

	public int getMaxSeats() {
		return maxSeats;
	}

	public List<Waiter> getWaiters() {
		return staff.getWaiters();
	}

	public List<Chef> getChefs() {
		return staff.getChefs();
	}

	public int seatExpansionCost() {
		return SEAT_EXPANSION_UNIT_COST * maxSeats;
	}

	public int waiterHireCost() {
		return staff.waiterHireCost();
	}

	public int chefHireCost() {
		return staff.chefHireCost();
	}

	public boolean increaseSeats() {
		int cost = seatExpansionCost();
		if (scoreboard.getMoney() < cost) return false;
		if (maxSeats >= MAX_SEATS) return false;

		scoreboard.setMoney(scoreboard.getMoney() - cost);
		maxSeats++;
		return true;
	}

	public boolean hireNewWaiter() {
		return staff.hireNewWaiter();
	}

	public boolean hireNewChef() {
		return staff.hireNewChef();
	}

	public boolean upgradeWaiterSpeed(int index) {
		return staff.upgradeWaiterSpeed(index);
	}

	public boolean upgradeChef(int index, ChefStat stat) {
		return staff.upgradeChef(index, stat);
	}

	private void mainGameLoop() {
		while (true) {
			synchronized (mediator.getPauseLock()) {
				while (mediator.isPaused()) {
					try {
						mediator.getPauseLock().wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
			}

			if (seats.size() < maxSeats && random.nextInt(100) < CUSTOMER_SPAWN_CHANCE_PERCENT) {
				Customer newCustomer = (Customer) customerFactory.createNpc(mediator);
				seats.add(newCustomer);
				new Thread(newCustomer).start();
			}
			seats.removeIf(Customer::isDone);

			display();

			try {
				Thread.sleep(GAME_TICK_MS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}

	private void display() {
		System.out.println("\n\n\n");
		System.out.println("Restaurant: " + scoreboard.getName());
		System.out.println("Score: " + scoreboard.getScore());
		System.out.println("Money: Rp." + scoreboard.getMoney());
		System.out.println("--- Waiters ---");
		getWaiters().forEach(w -> System.out.println(w.getName() + " " + w.getCurrentAction()));
		System.out.println("--- Chefs ---");
		getChefs().forEach(c -> System.out.println(c.getName() + " " + c.getCurrentAction()));
		System.out.println("--- Customers ---");
		seats.forEach(c -> System.out.println(c.getName() + " (" + c.getTolerance() + ") " + c.getCurrentAction()));
	}
}
