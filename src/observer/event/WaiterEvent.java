package observer.event;

public class WaiterEvent {
	public enum WaiterEventType {
		IDLE,
		TAKING_ORDER,
		WAITING_FOR_CHEF,
		BRING_ORDER_TO_CHEF,
		DELIVERING_FOOD
	}

	private final WaiterEventType type;

	public WaiterEvent(WaiterEventType type) {
		this.type = type;
	}

	public WaiterEventType getType() {
		return type;
	}
}
