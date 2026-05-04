package facade;

import factory.ChefFactory;
import factory.WaiterFactory;
import mediator.RestaurantMediator;
import model.Chef;
import model.Customer;
import model.Waiter;
import singleton.Restaurant;
import java.util.List;

public class WaiterFacade {
    private RestaurantMediator mediator;
    private List<Waiter> waiters;
    private WaiterFactory waiterFactory;
    private Restaurant scoreboard = Restaurant.getInstance();

    public WaiterFacade(GameFacade game) {
        this.mediator = game.getMediator();
        this.waiters = game.getWaiters();
        this.waiterFactory = game.getWaiterFactory();
    }

	public boolean hireNewWaiter() {
		int cost = waiters.size() * 150;
		
		if(scoreboard.getMoney() < cost) return false;
		
		if(waiters.size() >= 7) return false;
	
		scoreboard.setMoney(scoreboard.getMoney() - cost);
		Waiter newWaiter = (Waiter) waiterFactory.createNpc(mediator);
		waiters.add(newWaiter);
		mediator.addWaiter(newWaiter);
		new Thread(newWaiter).start();
		return true;
	}
	
	public boolean upgradeWaiterSpeed(int waiterIndex) {
        if (scoreboard.getMoney() < 150) return false;
        Waiter selected = waiters.get(waiterIndex);
        if (selected.getSpeed() >= 5) return false;
        
        selected.setSpeed(selected.getSpeed() + 1);
        scoreboard.setMoney(scoreboard.getMoney() - 150);
        return true;
    }
}
