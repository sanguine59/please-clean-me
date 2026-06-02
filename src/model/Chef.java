package model;

import mediator.RestaurantMediator;
import state.chef.ChefIdleState;
import state.chef.ChefState;

public class Chef extends Npc {
	private static final int INITIAL_SPEED = 0;
	private static final int INITIAL_SKILL = 1;

	private String name;
	private String currentAction;
	private int skill;
	private int speed;
	private ChefState state;
	private Customer assignedCustomer;
	private Waiter assignedWaiter;
	private final RestaurantMediator mediator;

	public Chef(String name, RestaurantMediator mediator) {
		this.name = name;
		this.speed = INITIAL_SPEED;
		this.skill = INITIAL_SKILL;
		this.mediator = mediator;
		this.state = new ChefIdleState(this);
		this.registerObserver(mediator);
	}

	@Override
	protected void handleCurrentState() {
		state.handle(this);
	}

	public String getName() {
		return name;
	}

	public ChefState getState() {
		return state;
	}

	public void setState(ChefState state) {
		this.state = state;
	}

	public Customer getAssignedCustomer() {
		return assignedCustomer;
	}

	public void setAssignedCustomer(Customer assignedCustomer) {
		this.assignedCustomer = assignedCustomer;
	}

	public Waiter getAssignedWaiter() {
		return assignedWaiter;
	}

	public void setAssignedWaiter(Waiter assignedWaiter) {
		this.assignedWaiter = assignedWaiter;
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

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
