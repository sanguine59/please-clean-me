package factory;

import mediator.RestaurantMediator;
import model.Customer;
import model.Npc;
import singleton.NamePool;

public class CustomerFactory implements NpcFactory {

	@Override
	public Npc createNpc(RestaurantMediator mediator) {
		// TODO Auto-generated method stub
		String name = NamePool.getInstance().addName();
		return new Customer(name, mediator);
	}
	
}
