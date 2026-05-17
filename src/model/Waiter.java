package model;

import mediator.RestaurantMediator;
import observer.event.WaiterEvent;
import state.customer.CustomerEatState;
import state.customer.CustomerPlacingOrderState;
import state.waiter.WaiterBringFoodState;
import state.waiter.WaiterBringOrderState;
import state.waiter.WaiterIdleState;
import state.waiter.WaiterState;
import state.waiter.WaiterTakingOrderState;
import state.waiter.WaiterWaitCookState;

public class Waiter extends Npc {
	private String name;
	private String currentAction;
	private int speed;
	private Customer assignedCustomer;
	private Chef assignedChef;
	private Order assignedOrder;
	private WaiterState state;
	private String stateName;
	private RestaurantMediator mediator;

	public Waiter(String name, RestaurantMediator mediator) {
		this.name = name;
		this.speed = 0;
		this.setMediator(mediator);
		this.state = new WaiterIdleState(this);
		this.registerObserver(mediator);
	}

	@Override
	protected void handleCurrentState() {
		if(this.state instanceof WaiterIdleState) {
			notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.IDLE, null));
			while(this.state instanceof WaiterIdleState) {
				try {
					Thread.sleep(100);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else if(this.state instanceof WaiterTakingOrderState) {
			int duration = 6 - this.getSpeed();
			duration = duration * 1000;
			try {
				Thread.sleep(duration);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.TAKING_ORDER, getAssignedOrder()));
			while(this.state instanceof WaiterTakingOrderState) {
				try {
					Thread.sleep(100);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else if(this.state instanceof WaiterWaitCookState) {
			notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.WAITING_FOR_CHEF, getAssignedOrder()));
			while(this.state instanceof WaiterWaitCookState) {
				try {
					Thread.sleep(100);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else if(this.state instanceof WaiterBringOrderState) {
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.BRING_ORDER_TO_CHEF, getAssignedOrder()));
			while(this.state instanceof WaiterBringOrderState) {
				try {
					Thread.sleep(100);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else if(this.state instanceof WaiterBringFoodState) {
			notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.DELIVERING_FOOD, getAssignedOrder()));
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			this.setState(new WaiterIdleState(this));
			this.getAssignedCustomer().setState(new CustomerEatState(this.getAssignedCustomer()));
			while(this.state instanceof WaiterBringFoodState) {
				try {
					Thread.sleep(100);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void takeOrder(Customer customer) {
		this.setAssignedCustomer(customer);
		setState(new WaiterTakingOrderState(this));
		customer.setAssignedWaiter(this);
		customer.setState(new CustomerPlacingOrderState(customer));
	}

	public void deliverFood(Order order) {
		this.setAssignedOrder(order);
		setState(new WaiterBringFoodState(this));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
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

	public Order getAssignedOrder() {
		return assignedOrder;
	}

	public void setAssignedOrder(Order assignedOrder) {
		this.assignedOrder = assignedOrder;
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

	public void setMediator(RestaurantMediator mediator) {
		this.mediator = mediator;
	}
}