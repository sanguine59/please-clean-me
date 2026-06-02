package state.chef;

import model.Chef;
import observer.event.ChefEvent;

public class ChefIdleState implements ChefState {
	private static final long POLL_MS = 100L;
	private final Chef chef;

	public ChefIdleState(Chef chef) {
		this.chef = chef;
		display();
	}

	@Override
	public void display() {
		chef.setCurrentAction("(idle)");
	}

	@Override
	public void handle(Chef chef) {
		chef.notifyObserver(new ChefEvent(ChefEvent.ChefEventType.IDLE));
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
