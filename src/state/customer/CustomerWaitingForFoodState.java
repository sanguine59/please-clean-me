package state.customer;

import model.Customer;

public class CustomerWaitingForFoodState implements CustomerState{
	private Customer customer;
	public CustomerWaitingForFoodState(Customer customer) {
		this.customer = customer;display();
	}
	@Override
	public void display() {
		// TODO Auto-generated method stub
		customer.setCurrentAction("(wait food)");
	}
}
