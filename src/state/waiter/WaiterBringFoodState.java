package state.waiter;

import model.Waiter;

public class WaiterBringFoodState implements WaiterState{
	
	private Waiter waiter;
	public WaiterBringFoodState(Waiter waiter) {
		this.waiter = waiter;
		display();
	}
	@Override
	public void display() {
		// TODO Auto-generated method stub
		waiter.setStateName("busy");
		waiter.setCurrentAction("Bringing food to customer (" + waiter.getAssignedCustomer().getName() + ")");
	}
	
}
