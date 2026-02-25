package state.waiter;

import model.Waiter;

public class WaiterWaitCookState implements WaiterState {
	private Waiter waiter;
	
	public WaiterWaitCookState(Waiter waiter) {
		this.waiter = waiter;display();
	}
	@Override
	public void display() {
		// TODO Auto-generated method stub
		waiter.setStateName("busy");
		waiter.setCurrentAction("(wait cook)");
	}
	
}
