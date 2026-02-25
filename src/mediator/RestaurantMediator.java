package mediator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import main.Main;
import model.Chef;
import model.Customer;
import model.Waiter;
import observer.Observer;
import observer.event.ChefEvent;
import observer.event.CustomerEvent;
import observer.event.WaiterEvent;
import observer.event.WaiterEvent.WaiterEventType;
import singleton.Restaurant;
import state.chef.ChefCookState;
import state.chef.ChefIdleState;
import state.customer.CustomerFoodBeingCookState;
import state.customer.CustomerFoodBeingServed;
import state.customer.CustomerPlacingOrderState;
import state.customer.CustomerWaitingForFoodState;
import state.waiter.WaiterBringFoodState;
import state.waiter.WaiterBringOrderState;
import state.waiter.WaiterIdleState;
import state.waiter.WaiterTakingOrderState;
import state.waiter.WaiterWaitCookState;

public class RestaurantMediator implements Observer {
    private List<Waiter> waiters = new ArrayList<>();
    private List<Chef> chefs = new ArrayList<>();
    private Restaurant scoreboard = Restaurant.getInstance();
    private final Queue<Customer> waitingCustomers = new LinkedList<>();
    private final Queue<Waiter> waitingWaiters = new LinkedList<>();
    private final Queue<Waiter> idleWaiters = new LinkedList<>();
    private final Queue<Chef> idleChefs = new LinkedList<>();
    private final Object pauseLock = new Object();
    
    public Object getPauseLock() {
		return pauseLock;
	}

	private volatile boolean isPaused = false;
    
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
    
    public List<Chef> getChefs(){
    	return chefs;
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
    
    @Override
    public synchronized void update(Object subject, Object event) {
        if(subject instanceof Customer && event instanceof CustomerEvent) {
        	Customer customer = (Customer) subject;
        	CustomerEvent customerEvent = (CustomerEvent) event;
        	if(customerEvent.getType() == CustomerEvent.CustomerEventType.REQUEST_ORDER) {
        		if(!idleWaiters.isEmpty()) {
        			Waiter waiter = idleWaiters.poll();
        			assignWaiterAndCustomer(waiter, customer);
        		} else {
        			waitingCustomers.add(customer);
        			
        		}
        	} else if(customerEvent.getType() == CustomerEvent.CustomerEventType.DONE_EATING) {
        		// customer leave, implement later
        		scoreboard.setScore(scoreboard.getScore() + (30 * customer.getAssignedChef().getSkill()));
        		scoreboard.setMoney(scoreboard.getMoney() + (30 * customer.getAssignedChef().getSkill()));
        		customer.setStateName("Done");
        	}
        }
        if(subject instanceof Waiter && event instanceof WaiterEvent) {
        	Waiter waiter = (Waiter) subject;
        	WaiterEvent waiterEvent = (WaiterEvent) event;
        	if(waiterEvent.getType() == WaiterEvent.WaiterEventType.IDLE) {
        		if(!waitingCustomers.isEmpty()) {
        			Customer customer = waitingCustomers.poll();
            		assignWaiterAndCustomer(waiter, customer);
        		} else {
        			idleWaiters.add(waiter);
        		}
        
        	} else if(waiterEvent.getType() == WaiterEvent.WaiterEventType.TAKING_ORDER) {
        		waiter.setState(new WaiterWaitCookState(waiter));
        	} else if(waiterEvent.getType() == WaiterEventType.WAITING_FOR_CHEF) {
        		if(!idleChefs.isEmpty()) {
        			Chef chef = idleChefs.poll();
        			assignWaiterAndChef(waiter, chef);
        		} else {
        			waitingWaiters.add(waiter);
        		}
        	} else if(waiterEvent.getType() == WaiterEvent.WaiterEventType.BRING_ORDER_TO_CHEF) {
        		waiter.getAssignedChef().setState(new ChefCookState(waiter.getAssignedChef()));
        	} else if(waiterEvent.getType() == WaiterEvent.WaiterEventType.DELIVERING_FOOD) {
//        		waiter.setState(new WaiterBringFoodState(waiter));
        		waiter.getAssignedCustomer().setState(new CustomerFoodBeingServed(waiter.getAssignedCustomer()));
        	}
        }
        if(subject instanceof Chef && event instanceof ChefEvent) {
        	Chef chef = (Chef) subject;
        	ChefEvent chefEvent = (ChefEvent) event;
        	if(chefEvent.getType() == ChefEvent.ChefEventType.IDLE) {
        		if(!waitingWaiters.isEmpty()) {
        			Waiter waiter = waitingWaiters.poll();
        			assignWaiterAndChef(waiter, chef);
        		} else {
        			idleChefs.add(chef);
        		}
        	} else if (chefEvent.getType() == ChefEvent.ChefEventType.START_COOKING){
        		chef.getAssignedCustomer().setState(new CustomerFoodBeingCookState(chef.getAssignedCustomer()));
        	} else if(chefEvent.getType() == ChefEvent.ChefEventType.IS_COOKING) {
        		
        	} else if(chefEvent.getType() == ChefEvent.ChefEventType.COOKING_DONE) {
        		chef.getAssignedWaiter().setState(new WaiterBringFoodState(chef.getAssignedWaiter()));
        		chef.setState(new ChefIdleState(chef));
        	}
        }
    }

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}
}