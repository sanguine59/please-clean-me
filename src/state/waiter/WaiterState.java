package state.waiter;

import model.Waiter;

public interface WaiterState {
	void display();
	void handle(Waiter waiter);
}
