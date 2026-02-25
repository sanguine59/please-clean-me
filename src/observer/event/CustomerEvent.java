package observer.event;

import model.Order;
import observer.event.ChefEvent.ChefEventType;

public class CustomerEvent {
	public enum CustomerEventType{
		REQUEST_ORDER,
		PLACING_ORDER,
		WAITING_FOR_FOOD,
		FOOD_BEING_COOK,
		FOOD_BEING_SERVE,
		EATING,
		DONE_EATING
	}
	
	private final CustomerEventType type;
	private final Order order;
	
	public CustomerEvent(CustomerEventType type, Order order) {
		this.type = type;
		this.order = order;
		
	}
	
	public CustomerEventType getType() {
        return type;
    }

    public Order getOrder() {
        return order;
    }
}
