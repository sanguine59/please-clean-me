package state.customer;

import model.Customer;

public class CustomerEatState implements CustomerState{
	private Customer customer;
	public CustomerEatState(Customer customer) {
		this.customer = customer;
		display();
	}
	@Override
	public void display() {
		// TODO Auto-generated method stub
		customer.setCurrentAction("(eating)");
	}

}
