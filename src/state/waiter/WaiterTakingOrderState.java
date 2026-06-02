package state.waiter;

import model.Waiter;
import observer.event.WaiterEvent;

public class WaiterTakingOrderState implements WaiterState {
	private static final int BASE_DURATION_SEC = 6;
	private static final long POLL_MS = 100L;
	private final Waiter waiter;

	public WaiterTakingOrderState(Waiter waiter) {
		this.waiter = waiter;
		display();
	}

	@Override
	public void display() {
		waiter.setCurrentAction("taking order (" + waiter.getAssignedCustomer().getName() + ")");
	}

	@Override
	public void handle(Waiter waiter) {
		long durationMs = (BASE_DURATION_SEC - waiter.getSpeed()) * 1000L;
		try {
			Thread.sleep(durationMs);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
		waiter.notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.TAKING_ORDER));
		while (waiter.getState() == this) {
			try {
				Thread.sleep(POLL_MS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
}
