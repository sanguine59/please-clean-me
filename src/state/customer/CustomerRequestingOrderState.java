package state.customer;

import model.Customer;
import observer.event.CustomerEvent;

public class CustomerRequestingOrderState implements CustomerState {
	private static final long TICK_MS = 2000L;
	private final Customer customer;

	public CustomerRequestingOrderState(Customer customer) {
		this.customer = customer;
		display();
	}

	@Override
	public void display() {
		customer.setCurrentAction("(waiting for waiter)");
	}

	@Override
	public void handle(Customer customer) {
		customer.notifyObserver(new CustomerEvent(CustomerEvent.CustomerEventType.REQUEST_ORDER));
		while (customer.getState() == this) {
			try {
				Thread.sleep(TICK_MS);
				customer.setTolerance(customer.getTolerance() - 1);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
}
