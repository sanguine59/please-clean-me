package model;

import mediator.RestaurantMediator;
import state.waiter.WaiterIdleState;
import state.waiter.WaiterState;

public class Waiter extends Npc {
	private static final int INITIAL_SPEED = 0;

	private String name;
	private String currentAction;
	private int speed;
	private Customer assignedCustomer;
	private Chef assignedChef;
	private WaiterState state;
	private final RestaurantMediator mediator;

	public Waiter(String name, RestaurantMediator mediator) {
		this.name = name;
		this.speed = INITIAL_SPEED;
		this.mediator = mediator;
		this.state = new WaiterIdleState(this);
		this.registerObserver(mediator);
	}

	@Override
	protected void handleCurrentState() {
		state.handle(this);
	}

	public String getName() {
		return name;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public WaiterState getState() {
		return state;
	}

	public void setState(WaiterState state) {
		this.state = state;
	}

	public Customer getAssignedCustomer() {
		return assignedCustomer;
	}

	public void setAssignedCustomer(Customer assignedCustomer) {
		this.assignedCustomer = assignedCustomer;
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
}
