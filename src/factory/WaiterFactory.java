package factory;

import mediator.RestaurantMediator;
import model.Npc;
import model.Waiter;

public class WaiterFactory extends BaseFactory {
	@Override
	public Npc createNpc(RestaurantMediator mediator) {
		return new Waiter(getRandomName(), mediator);
	}
}
