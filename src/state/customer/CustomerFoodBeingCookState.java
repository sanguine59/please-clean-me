package state.customer;

import model.Customer;

public class CustomerFoodBeingCookState implements CustomerState{
	private Customer customer;
	public CustomerFoodBeingCookState(Customer customer) {
		this.customer = customer;
		display();
	}
	@Override
	public void display() {
		// TODO Auto-generated method stub
		customer.setCurrentAction("Food being cook (" + customer.getAssignedChef().getName() + ")");
	}
	
	

}
