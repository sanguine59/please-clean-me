package state.waiter;

import model.Waiter;

public class WaiterIdleState implements WaiterState{
	private Waiter waiter;
	public WaiterIdleState(Waiter waiter) {
		this.waiter = waiter;display();
	}
	@Override
	public void display() {
		// TODO Auto-generated method stub
		waiter.setStateName("idle");
		waiter.setCurrentAction("(idle)");
	}

}
