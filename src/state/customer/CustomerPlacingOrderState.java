package state.customer;

import model.Customer;

public class CustomerPlacingOrderState implements CustomerState {
	private static final long POLL_MS = 100L;
	private final Customer customer;

	public CustomerPlacingOrderState(Customer customer) {
		this.customer = customer;
		display();
	}

	@Override
	public void display() {
		customer.setCurrentAction("Ordering (" + customer.getAssignedWaiter().getName() + ')');
	}

	@Override
	public void handle(Customer customer) {
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
