package model;

public class Order {
	private Customer customer;
	private Waiter waiter;
	private Chef chef;
	
	public Order(Customer customer, Waiter waiter, Chef chef) {
		this.customer = customer;
		this.waiter = waiter;
		this.setChef(chef);
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Waiter getWaiter() {
		return waiter;
	}

	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}

	public Chef getChef() {
		return chef;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}
	
	
}
