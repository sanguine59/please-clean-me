package factory;

import mediator.RestaurantMediator;
import model.Chef;
import model.Npc;

public class WaiterFactory extends BaseFactory{
	@Override
    public Npc createNpc(RestaurantMediator mediator) {
        return new Chef(getRandomName(), mediator);
    }
}