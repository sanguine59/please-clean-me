package factory;

import mediator.RestaurantMediator;
import model.Npc;
import model.Waiter;
import singleton.NamePool;

public class WaiterFactory implements NpcFactory{

	@Override
	public Npc createNpc(RestaurantMediator mediator) {
		// TODO Auto-generated method stub
		String name = NamePool.getInstance().addName();
		return new Waiter(name, mediator);
	}

}
