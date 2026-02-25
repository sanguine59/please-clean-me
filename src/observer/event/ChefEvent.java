package observer.event;

import model.Order;

public class ChefEvent {
    public enum ChefEventType {
        IDLE,
        START_COOKING,
        IS_COOKING,
        COOKING_DONE
    }

    private final ChefEventType type;
    private final Order order;

    public ChefEvent(ChefEventType type, Order order) {
        this.type = type;
        this.order = order;
    }

    public ChefEventType getType() {
        return type;
    }

    public Order getOrder() {
        return order;
    }
}
