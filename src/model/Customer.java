package model;

import mediator.RestaurantMediator;
import state.customer.CustomerRequestingOrderState;
import state.customer.CustomerState;

public class Customer extends Npc {
	private static final int INITIAL_TOLERANCE = 12;

	private String name;
	private int tolerance;
	private String currentAction;
	private CustomerState state;
	private Waiter assignedWaiter;
	private Chef assignedChef;
	private RestaurantMediator mediator;
	private volatile boolean done;

	public Customer(String name, RestaurantMediator mediator) {
		this.name = name;
		this.mediator = mediator;
		this.tolerance = INITIAL_TOLERANCE;
		this.registerObserver(mediator);
		setState(new CustomerRequestingOrderState(this));
	}

	@Override
	protected void handleCurrentState() {
		state.handle(this);
	}

	public boolean isDone() {
		return done;
	}

	public void markDone() {
		this.done = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CustomerState getState() {
		return state;
	}

	public void setState(CustomerState state) {
		this.state = state;
	}

	public Waiter getAssignedWaiter() {
		return assignedWaiter;
	}

	public void setAssignedWaiter(Waiter assignedWaiter) {
		this.assignedWaiter = assignedWaiter;
	}

	public Chef getAssignedChef() {
		return assignedChef;
	}

	public void setAssignedChef(Chef assignedChef) {
		this.assignedChef = assignedChef;
	}

	public String getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}

	public RestaurantMediator getMediator() {
		return mediator;
	}

	public int getTolerance() {
		return tolerance;
	}

	public void setTolerance(int tolerance) {
		this.tolerance = tolerance;
	}
}
