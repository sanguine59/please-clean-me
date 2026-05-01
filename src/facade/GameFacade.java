package facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import factory.ChefFactory;
import factory.CustomerFactory;
import factory.WaiterFactory;
import main.Main;
import main.reader;
import mediator.RestaurantMediator;
import model.Chef;
import model.Customer;
import model.Waiter;
import singleton.Restaurant;

public class GameFacade {
	Restaurant scoreboard = Restaurant.getInstance();
	RestaurantMediator mediator = new RestaurantMediator();
	CustomerFactory customerFactory = new CustomerFactory();
	WaiterFactory waiterFactory = new WaiterFactory();
	ChefFactory chefFactory = new ChefFactory();
	List<Waiter> waiters = new ArrayList<>();
	List<Chef> chefs = new ArrayList<>();
	List<Customer> seats = new ArrayList<>();
	int maxSeats = 4;
	Random random = new Random();
	
	public GameFacade(Main main) {
    this.main = main;
}
	
	public void startGame(String resturantName) {
		scoreboard.setName(resturantName);
		startingStaff(2, 2);
		
		new Thread(new reader(mediator, main)).start();
		new Thread(this::mainGameLoop).start();
	}
	
	private void startingStaff(int countWaiter, int countChef) {
		for(int i = 0; i < countWaiter; i++) {
			Waiter newWaiter = (Waiter) waiterFactory.createNpc(mediator);
			waiters.add(newWaiter);
			mediator.addWaiter(newWaiter);
			new Thread(newWaiter).start();
		}
		for(int i = 0; i < countChef; i++) {
			Chef newChef = (Chef) chefFactory.createNpc(mediator);
			chefs.add(newChef);
			mediator.addChef(newChef);
			new Thread(newChef).start();
		}
	}
	
	private void mainGameLoop() {
		while(true) {
			synchronized(mediator.getPauseLock()) {
				while(mediator.isPaused()) {
					try {
						mediator.getPauseLock().wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			if (seats.size() < maxSeats && random.nextInt(100) < 25) {
	            Customer newCustomer = (Customer) customerFactory.createNpc(mediator);
	            seats.add(newCustomer);
	            new Thread(newCustomer).start();
	        }
	        seats.removeIf(c -> "Done".equals(c.getStateName()));
	        
	        display();
	
	        try {
	            Thread.sleep(1000);
	        } catch (InterruptedException e) {
	        	e.printStackTrace();
	        }
		}
	}
	
	public boolean increaseSeats() {
		int cost = 100 * maxSeats;
		if(scoreboard.getMoney() < cost ) return false;
		if(maxSeats >= 13) return false;
		
		
		scoreboard.setMoney(scoreboard.getMoney() - cost);
		maxSeats++;
		return true;
	}
	
	public void pauseGame() {
		mediator.pauseSimulation();
	}
	public void resumeGame() {
		mediator.resumeSimulation();
	}
	
	private void display() {
		System.out.println("\n\n\n");
        System.out.println("Restaurant: " + scoreboard.getName());
        System.out.println("Score: " + scoreboard.getScore());		
        System.out.println("Money: Rp." + scoreboard.getMoney());
        System.out.println("--- Waiters ---");
        waiters.forEach(w -> System.out.println(w.getName() + " " + w.getCurrentAction()));
        System.out.println("--- Chefs ---");
        chefs.forEach(c -> System.out.println(c.getName() + " " + c.getCurrentAction()));
        System.out.println("--- Customers ---");
        seats.forEach(c -> System.out.println(c.getName() + " (" + c.getTolerance() + ") " + c.getCurrentAction()));
    }

	public RestaurantMediator getMediator() {
    return mediator;
	}

	public List<Chef> getChefs() {
		return chefs;
	}

	public List<Waiter> getWaiters() {
		return waiters;
	}

	public ChefFactory getChefFactory() {
		return chefFactory;
	}

	public WaiterFactory getWaiterFactory() {
		return waiterFactory;
	}
}
