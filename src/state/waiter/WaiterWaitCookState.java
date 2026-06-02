package state.waiter;

import model.Waiter;
import observer.event.WaiterEvent;

public class WaiterWaitCookState implements WaiterState {
	private static final long POLL_MS = 100L;
	private final Waiter waiter;

	public WaiterWaitCookState(Waiter waiter) {
		this.waiter = waiter;
		display();
	}

	@Override
	public void display() {
		waiter.setCurrentAction("(wait cook)");
	}

	@Override
	public void handle(Waiter waiter) {
		waiter.notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.WAITING_FOR_CHEF));
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
