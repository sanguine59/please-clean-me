package state.chef;

import model.Chef;
import observer.event.ChefEvent;

public class ChefCookState implements ChefState {
	private static final int BASE_DURATION_SEC = 6;
	private static final long POLL_MS = 100L;
	private final Chef chef;

	public ChefCookState(Chef chef) {
		this.chef = chef;
		display();
	}

	@Override
	public void display() {
		chef.setCurrentAction("cooking (" + chef.getAssignedCustomer().getName() + ")");
	}

	@Override
	public void handle(Chef chef) {
		chef.notifyObserver(new ChefEvent(ChefEvent.ChefEventType.START_COOKING));
		long durationMs = (BASE_DURATION_SEC - chef.getSpeed()) * 1000L;
		try {
			Thread.sleep(durationMs);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
		chef.notifyObserver(new ChefEvent(ChefEvent.ChefEventType.COOKING_DONE));
		while (chef.getState() == this) {
			try {
				Thread.sleep(POLL_MS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
}
