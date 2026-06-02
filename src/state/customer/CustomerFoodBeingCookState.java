package state.customer;

import model.Customer;

public class CustomerFoodBeingCookState implements CustomerState {
	private static final long INITIAL_WAIT_MS = 4000L;
	private static final long POLL_MS = 100L;
	private final Customer customer;

	public CustomerFoodBeingCookState(Customer customer) {
		this.customer = customer;
		display();
	}

	@Override
	public void display() {
		customer.setCurrentAction("Food being cook (" + customer.getAssignedChef().getName() + ")");
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
