package mediator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import model.Chef;
import model.Customer;
import model.Waiter;
import observer.Observer;
import observer.event.ChefEvent;
import observer.event.CustomerEvent;
import observer.event.WaiterEvent;
import singleton.Restaurant;
import state.chef.ChefCookState;
import state.chef.ChefIdleState;
import state.customer.CustomerFoodBeingCookState;
import state.customer.CustomerFoodBeingServed;
import state.customer.CustomerPlacingOrderState;
import state.customer.CustomerWaitingForFoodState;
import state.waiter.WaiterBringFoodState;
import state.waiter.WaiterBringOrderState;
import state.waiter.WaiterTakingOrderState;
import state.waiter.WaiterWaitCookState;

public class RestaurantMediator implements Observer {
	private static final int BASE_REWARD = 30;

	private final List<Waiter> waiters = new ArrayList<>();
	private final List<Chef> chefs = new ArrayList<>();
	private final Restaurant scoreboard = Restaurant.getInstance();
	private final Queue<Customer> waitingCustomers = new LinkedList<>();
	private final Queue<Waiter> waitingWaiters = new LinkedList<>();
	private final Queue<Waiter> idleWaiters = new LinkedList<>();
	private final Queue<Chef> idleChefs = new LinkedList<>();
	private final Object pauseLock = new Object();
	private volatile boolean isPaused = false;

	public Object getPauseLock() {
		return pauseLock;
	}

	public void pauseSimulation() {
		setPaused(true);
	}

	public void resumeSimulation() {
		synchronized (pauseLock) {
			setPaused(false);
			pauseLock.notifyAll();
		}
	}

	public void addWaiter(Waiter waiter) {
		this.waiters.add(waiter);
	}

	public void addChef(Chef chef) {
		this.chefs.add(chef);
	}

	public List<Waiter> getWaiters() {
		return waiters;
	}

	public List<Chef> getChefs() {
		return chefs;
	}

	@Override
	public synchronized void update(Object subject, Object event) {
		if (subject instanceof Customer && event instanceof CustomerEvent) {
			handleCustomerEvent((Customer) subject, (CustomerEvent) event);
		} else if (subject instanceof Waiter && event instanceof WaiterEvent) {
			handleWaiterEvent((Waiter) subject, (WaiterEvent) event);
		} else if (subject instanceof Chef && event instanceof ChefEvent) {
			handleChefEvent((Chef) subject, (ChefEvent) event);
		}
	}

	private void handleCustomerEvent(Customer customer, CustomerEvent event) {
		switch (event.getType()) {
			case REQUEST_ORDER:
				if (!idleWaiters.isEmpty()) {
					assignWaiterAndCustomer(idleWaiters.poll(), customer);
				} else {
					waitingCustomers.add(customer);
				}
				break;
			case DONE_EATING:
				int reward = BASE_REWARD * customer.getAssignedChef().getSkill();
				scoreboard.setScore(scoreboard.getScore() + reward);
				scoreboard.setMoney(scoreboard.getMoney() + reward);
				customer.markDone();
				break;
			default:
				break;
		}
	}

	private void handleWaiterEvent(Waiter waiter, WaiterEvent event) {
		switch (event.getType()) {
			case IDLE:
				if (!waitingCustomers.isEmpty()) {
					assignWaiterAndCustomer(waiter, waitingCustomers.poll());
				} else {
					idleWaiters.add(waiter);
				}
				break;
			case TAKING_ORDER:
				waiter.setState(new WaiterWaitCookState(waiter));
				break;
			case WAITING_FOR_CHEF:
				if (!idleChefs.isEmpty()) {
					assignWaiterAndChef(waiter, idleChefs.poll());
				} else {
					waitingWaiters.add(waiter);
				}
				break;
			case BRING_ORDER_TO_CHEF:
				waiter.getAssignedChef().setState(new ChefCookState(waiter.getAssignedChef()));
				break;
			case DELIVERING_FOOD:
				waiter.getAssignedCustomer().setState(new CustomerFoodBeingServed(waiter.getAssignedCustomer()));
				break;
			default:
				break;
		}
	}

	private void handleChefEvent(Chef chef, ChefEvent event) {
		switch (event.getType()) {
			case IDLE:
				if (!waitingWaiters.isEmpty()) {
					assignWaiterAndChef(waitingWaiters.poll(), chef);
				} else {
					idleChefs.add(chef);
				}
				break;
			case START_COOKING:
				chef.getAssignedCustomer().setState(new CustomerFoodBeingCookState(chef.getAssignedCustomer()));
				break;
			case COOKING_DONE:
				chef.getAssignedWaiter().setState(new WaiterBringFoodState(chef.getAssignedWaiter()));
				chef.setState(new ChefIdleState(chef));
				break;
			default:
				break;
		}
	}

	private synchronized void assignWaiterAndCustomer(Waiter waiter, Customer customer) {
		waiter.setAssignedCustomer(customer);
		customer.setAssignedWaiter(waiter);

		waiter.setState(new WaiterTakingOrderState(waiter));
		customer.setState(new CustomerPlacingOrderState(customer));
	}

	private synchronized void assignWaiterAndChef(Waiter waiter, Chef chef) {
		waiter.setAssignedChef(chef);
		waiter.getAssignedCustomer().setAssignedChef(chef);

		chef.setAssignedCustomer(waiter.getAssignedCustomer());
		chef.setAssignedWaiter(waiter);

		waiter.setState(new WaiterBringOrderState(waiter));
		waiter.getAssignedCustomer().setState(new CustomerWaitingForFoodState(waiter.getAssignedCustomer()));
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}
}
