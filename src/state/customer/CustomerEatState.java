package state.customer;

import model.Customer;
import observer.event.CustomerEvent;

public class CustomerEatState implements CustomerState {
	private static final long EAT_DURATION_MS = 6000L;
	private static final long POLL_MS = 100L;
	private final Customer customer;

	public CustomerEatState(Customer customer) {
		this.customer = customer;
		display();
	}

	@Override
	public void display() {
		customer.setCurrentAction("(eating)");
	}

	@Override
	public void handle(Customer customer) {
		try {
			Thread.sleep(EAT_DURATION_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
		customer.notifyObserver(new CustomerEvent(CustomerEvent.CustomerEventType.DONE_EATING));
		while (customer.getState() == this) {
			try {
				Thread.sleep(POLL_MS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
}
