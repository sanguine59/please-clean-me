package state.customer;

import model.Customer;

public class CustomerWaitingForFoodState implements CustomerState {
	private static final long INITIAL_WAIT_MS = 4000L;
	private static final long POLL_MS = 100L;
	private final Customer customer;

	public CustomerWaitingForFoodState(Customer customer) {
		this.customer = customer;
		display();
	}

	@Override
	public void display() {
		customer.setCurrentAction("(wait food)");
	}

	@Override
	public void handle(Customer customer) {
		try {
			Thread.sleep(INITIAL_WAIT_MS);
			customer.setTolerance(customer.getTolerance() - 1);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
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
