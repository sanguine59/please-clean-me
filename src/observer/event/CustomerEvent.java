package observer.event;

public class CustomerEvent {
	public enum CustomerEventType {
		REQUEST_ORDER,
		DONE_EATING
	}

	private final CustomerEventType type;

	public CustomerEvent(CustomerEventType type) {
		this.type = type;
	}

	public CustomerEventType getType() {
		return type;
	}
}
