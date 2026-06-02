package state.customer;

import model.Customer;

public interface CustomerState {
	void display();
	void handle(Customer customer);
}
