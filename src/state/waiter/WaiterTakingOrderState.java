package state.waiter;

import model.Waiter;

public class WaiterTakingOrderState implements WaiterState{
	private Waiter waiter;
	public WaiterTakingOrderState(Waiter waiter) {
		this.waiter = waiter;display();
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		waiter.setStateName("busy");
		waiter.setCurrentAction("taking order (" + waiter.getAssignedCustomer().getName() + ")");
	}
	
}
