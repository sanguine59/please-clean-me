package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mediator.RestaurantMediator;
import observer.Observer;
import observer.Subject;
import observer.event.WaiterEvent;
import state.customer.CustomerEatState;
import state.customer.CustomerFoodBeingServed;
import state.customer.CustomerPlacingOrderState;
import state.waiter.WaiterBringFoodState;
import state.waiter.WaiterBringOrderState;
import state.waiter.WaiterIdleState;
import state.waiter.WaiterState;
import state.waiter.WaiterTakingOrderState;
import state.waiter.WaiterWaitCookState;

public class Waiter extends Npc implements Subject, Runnable{
	private String name;
	private String currentAction;
	private int speed;
	private Customer assignedCustomer;
	private RestaurantMediator mediator;
	private Chef assignedChef;
	private String stateName;
	private WaiterState state;
	private Order assignedOrder;
	List<Observer> observers = new ArrayList<>();
	private final Lock pauseLock = new ReentrantLock();
	private final Condition unpaused = pauseLock.newCondition();
	private volatile boolean isPaused = false;
	
	public void pause() {
		isPaused = true;
	}
	
	public void resume() {
		pauseLock.lock();
		try {

			isPaused = false;
			unpaused.signalAll();
		} finally {
			pauseLock.unlock();
		}
		
	}
	
	public Waiter(String name, RestaurantMediator mediator) {
		this.name = name;
		this.speed = 0;
		this.setMediator(mediator);
		this.state = new WaiterIdleState(this);
		this.registerObserver(mediator);
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public void setAssignedOrder(Order assignedOrder) {
		this.assignedOrder = assignedOrder;
	}
	
	public Order getAssignedOrder() {
		return assignedOrder;
	}

	public void setAssignedChef(Chef assignedChef) {
		this.assignedChef = assignedChef;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public WaiterState getState() {
		return state;
	}

	public void setState(WaiterState state) {
		this.state = state;
	}

	@Override
	public void registerObserver(Observer o) {
		// TODO Auto-generated method stub
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub
		observers.remove(o);
	}

	@Override
	public void notifyObserver(Object event) {
		// TODO Auto-generated method stub
		for (Observer observer : observers) {
			observer.update(this, event);
		}
	}


	public String getCurrentAction() {
		return currentAction;
	}


	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}


	@Override
	public void run() {
		
		while(true) {
			pauseLock.lock();
			try {
				while(isPaused) {
					unpaused.await();
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			} finally {
				pauseLock.unlock();
			}
			if(this.state instanceof WaiterIdleState) {
	        	notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.IDLE, null));
	        	while (this.state instanceof WaiterIdleState) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else if(this.state instanceof WaiterTakingOrderState) {
	        	int duration = 6 - this.getSpeed();
	        	duration = duration * 1000;
	        	try {
	        		
					Thread.sleep(duration);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.TAKING_ORDER, getAssignedOrder()));
	        	
	        	while (this.state instanceof WaiterTakingOrderState) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else if(this.state instanceof WaiterWaitCookState) {
	        	// waiting for an available chef
	        	notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.WAITING_FOR_CHEF, getAssignedOrder()));
	        	while (this.state instanceof WaiterWaitCookState) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else if(this.state instanceof WaiterBringOrderState) {
	        	// give the order to the chef
	        	try {
					Thread.sleep(1000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.BRING_ORDER_TO_CHEF, getAssignedOrder()));
	        	while (this.state instanceof WaiterBringOrderState) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else if(this.state instanceof WaiterBringFoodState) {
	        	notifyObserver(new WaiterEvent(WaiterEvent.WaiterEventType.DELIVERING_FOOD, getAssignedOrder()));
	        	// bring the food to the customer that ordered
	        	try {
					Thread.sleep(1000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	this.setState(new WaiterIdleState(this));
	        	this.getAssignedCustomer().setState(new CustomerEatState(this.getAssignedCustomer()));;;
	 
	        	while (this.state instanceof WaiterBringFoodState) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
		}
        
    }


	public RestaurantMediator getMediator() {
		return mediator;
	}


	public void setMediator(RestaurantMediator mediator) {
		this.mediator = mediator;
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


	public int getSpeed() {
		return speed;
	}


	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
