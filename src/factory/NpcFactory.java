package factory;

import mediator.RestaurantMediator;
import model.Npc;

public interface NpcFactory {
	public Npc createNpc(RestaurantMediator mediator);
}
