package observer.event;

import model.Order;

public class WaiterEvent {
	public enum WaiterEventType {
        IDLE,
        TAKING_ORDER,
        WAITING_FOR_CHEF,
        BRING_ORDER_TO_CHEF,
        DELIVERING_FOOD,
        DELIVERY_DONE
    }

    private final WaiterEventType type;
    private final Order order;

    public WaiterEvent(WaiterEventType type, Order order) {
        this.type = type;
        this.order = order;
    }

    public WaiterEventType getType() {
        return type;
    }

    public Order getOrder() {
        return order;
    }
    
}