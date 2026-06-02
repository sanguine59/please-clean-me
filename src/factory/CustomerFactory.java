package factory;

import mediator.RestaurantMediator;
import model.Customer;
import model.Npc;

public class CustomerFactory extends BaseFactory {
	@Override
	public Npc createNpc(RestaurantMediator mediator) {
		return new Customer(getRandomName(), mediator);
	}
}
