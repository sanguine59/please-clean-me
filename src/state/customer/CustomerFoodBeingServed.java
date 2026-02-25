package state.customer;

import model.Customer;

public class CustomerFoodBeingServed implements CustomerState {
	private Customer customer;
	public CustomerFoodBeingServed(Customer customer) {
		this.customer = customer;
		display();
	}
	@Override
	public void display() {
		// TODO Auto-generated method stub
		customer.setCurrentAction("Food being served (" + customer.getAssignedWaiter().getName() + ')');;
	}

}
