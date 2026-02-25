package state.waiter;

import model.Waiter;

public class WaiterBringOrderState implements WaiterState {
	private Waiter waiter;
	public WaiterBringOrderState(Waiter waiter) {
		this.waiter = waiter;display();
	}
	
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		waiter.setStateName("busy");
		waiter.setCurrentAction("Bringing order to chef (" + waiter.getAssignedChef().getName() + ')');
	}
	
}
