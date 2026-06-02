package observer.event;

public class ChefEvent {
	public enum ChefEventType {
		IDLE,
		START_COOKING,
		COOKING_DONE
	}

	private final ChefEventType type;

	public ChefEvent(ChefEventType type) {
		this.type = type;
	}

	public ChefEventType getType() {
		return type;
	}
}
