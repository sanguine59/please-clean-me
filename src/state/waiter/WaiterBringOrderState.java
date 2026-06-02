package state.waiter;

import model.Waiter;
import observer.event.WaiterEvent;

public class WaiterBringOrderState implements WaiterState {
	private static final long DELIVERY_MS = 1000L;
	private static final long POLL_MS = 100L;
	private final Waiter waiter;

	public WaiterBringOrderState(Waiter waiter) {
		this.waiter = waiter;
		display();
	}

	@Override
	public void display() {
		waiter.setCurrentAction("Bringing order to chef (" + waiter.getAssignedChef().getName() + ')');
	}

	@Override
	public void handle(Waiter waiter) {
		try {
			Thread.sleep(DELIVERY_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
		waiter.notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.BRING_ORDER_TO_CHEF));
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
