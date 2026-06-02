package state.waiter;

import model.Waiter;
import observer.event.WaiterEvent;
import state.customer.CustomerEatState;

public class WaiterBringFoodState implements WaiterState {
	private static final long DELIVERY_MS = 1000L;
	private final Waiter waiter;

	public WaiterBringFoodState(Waiter waiter) {
		this.waiter = waiter;
		display();
	}

	@Override
	public void display() {
		waiter.setCurrentAction("Bringing food to customer (" + waiter.getAssignedCustomer().getName() + ")");
	}

	@Override
	public void handle(Waiter waiter) {
		waiter.notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.DELIVERING_FOOD));
		try {
			Thread.sleep(DELIVERY_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
		waiter.getAssignedCustomer().setState(new CustomerEatState(waiter.getAssignedCustomer()));
		waiter.setState(new WaiterIdleState(waiter));
	}
}
