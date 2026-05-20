package facade;

import factory.ChefFactory;
import factory.WaiterFactory;
import mediator.RestaurantMediator;
import model.Chef;
import model.Customer;
import model.Waiter;
import singleton.Restaurant;
import java.util.List;

public class ChefFacade {
	
    private RestaurantMediator mediator;
    private List<Chef> chefs;
    private ChefFactory chefFactory;
    private Restaurant scoreboard = Restaurant.getInstance();

    public ChefFacade(GameFacade game) {
        this.mediator = game.getMediator();
        this.chefs = game.getChefs();
        this.chefFactory = game.getChefFactory();
    }
	
	public boolean hireNewChef() {
        int cost = chefs.size() * 200;
        
        if (scoreboard.getMoney() < cost) return false;
        
        if (chefs.size() >= 7) return false;
        
        scoreboard.setMoney(scoreboard.getMoney() - cost);
        Chef newChef = (Chef) chefFactory.createNpc(mediator);
        chefs.add(newChef);
        mediator.addChef(newChef);
        new Thread(newChef).start();
        return true;
    }
	
	public boolean upgradeChef(int chefIndex, String statToUpgrade) {
    	if (scoreboard.getMoney() < 150) return false;
        Chef selected = chefs.get(chefIndex);

        if ("speed".equalsIgnoreCase(statToUpgrade)) {
            if (selected.getSpeed() >= 5) return false;
            selected.setSpeed(selected.getSpeed() + 1);
        } else if ("skill".equalsIgnoreCase(statToUpgrade)) {
            if (selected.getSkill() >= 5) return false;
            selected.setSkill(selected.getSkill() + 1);
        } else {
            return false;
        }
        
        scoreboard.setMoney(scoreboard.getMoney() - 150);
        return true;
    }
}
