package state.waiter;

import model.Waiter;
import observer.event.WaiterEvent;

public class WaiterIdleState implements WaiterState {
	private static final long POLL_MS = 100L;
	private final Waiter waiter;

	public WaiterIdleState(Waiter waiter) {
		this.waiter = waiter;
		display();
	}

	@Override
	public void display() {
		waiter.setCurrentAction("(idle)");
	}

	@Override
	public void handle(Waiter waiter) {
		waiter.notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.IDLE));
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
