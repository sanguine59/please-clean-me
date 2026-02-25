package state.customer;

import model.Customer;

public class CustomerRequestingOrderState implements CustomerState {
	private Customer customer;
	
	public CustomerRequestingOrderState(Customer customer) {
		this.customer = customer;
		display();
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		customer.setCurrentAction("(waiting for waiter)");
	}

}
