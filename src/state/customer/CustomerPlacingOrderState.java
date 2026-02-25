package state.customer;

import model.Customer;

public class CustomerPlacingOrderState implements CustomerState {
	private Customer customer;
	
	public CustomerPlacingOrderState(Customer customer) {
		this.customer = customer;
		display();
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		customer.setCurrentAction("Ordering (" + customer.getAssignedWaiter().getName() + ')');
		
	}
	
	
}
