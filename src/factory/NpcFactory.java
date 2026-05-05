package factory;

import mediator.RestaurantMediator;
import model.Npc;

public interface NpcFactory {
    Npc createNpc(RestaurantMediator mediator);
}