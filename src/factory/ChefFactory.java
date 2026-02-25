package factory;

import mediator.RestaurantMediator;
import model.Chef;
import model.Npc;
import singleton.NamePool;

public class ChefFactory implements NpcFactory {

	@Override
	public Npc createNpc(RestaurantMediator mediator) {
		// TODO Auto-generated method stub
		
		String name = NamePool.getInstance().addName();
		return new Chef(name, mediator);
	}

}
