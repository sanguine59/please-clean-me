package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mediator.RestaurantMediator;
import observer.Observer;
import observer.Subject;
import observer.event.CustomerEvent;
import observer.event.CustomerEvent.CustomerEventType;
import state.customer.CustomerEatState;
import state.customer.CustomerFoodBeingCookState;
import state.customer.CustomerFoodBeingServed;
import state.customer.CustomerPlacingOrderState;
import state.customer.CustomerRequestingOrderState;
import state.customer.CustomerState;
import state.customer.CustomerWaitingForFoodState;
import state.waiter.WaiterBringFoodState;

public class Customer extends Npc implements Subject, Runnable{
	private String name;
	private int tolerance;
	private String currentAction;
	private CustomerState state;
	private String stateName;
	private Waiter assignedWaiter;
	private Chef assignedChef;
	private RestaurantMediator mediator;
	private List<Observer> observers = new ArrayList<>();
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
	
	public Customer(String name, RestaurantMediator mediator) {
		super();
		this.name = name;
		this.mediator = mediator;
		this.tolerance = 12;
		this.registerObserver(mediator);
		this.setState(new CustomerRequestingOrderState(this));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
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
	public CustomerState getState() {
		return state;
	}
	public void setState(CustomerState state) {
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
	public RestaurantMediator getMediator() {
		return mediator;
	}
	public void setMediator(RestaurantMediator mediator) {
		this.mediator = mediator;
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
			if(this.state instanceof CustomerRequestingOrderState) {
	        	notifyObserver(new CustomerEvent(CustomerEvent.CustomerEventType.REQUEST_ORDER, null));
	        	while (this.state instanceof CustomerRequestingOrderState) {
	                try {
	        
	                    Thread.sleep(2000);
	                    this.setTolerance(getTolerance() - 1);
	 
	                    
	                    if (getTolerance() <= 0) {
	                        
	                    }
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else if (this.state instanceof CustomerPlacingOrderState) {
	        	notifyObserver(new CustomerEvent(CustomerEvent.CustomerEventType.PLACING_ORDER, null));
	        	while (this.state instanceof CustomerPlacingOrderState) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else if(this.state instanceof CustomerWaitingForFoodState) {
	        	notifyObserver(new CustomerEvent(CustomerEvent.CustomerEventType.WAITING_FOR_FOOD, null));
	        	try {
					Thread.sleep(4000);
					this.setTolerance(getTolerance() - 1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	while (this.state instanceof CustomerWaitingForFoodState) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else if(this.state instanceof CustomerFoodBeingCookState) {
	        	notifyObserver(new CustomerEvent(CustomerEvent.CustomerEventType.FOOD_BEING_COOK, null));
	        	try {
					Thread.sleep(4000);
					this.setTolerance(getTolerance() - 1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	while (this.state instanceof CustomerFoodBeingCookState) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else if(this.state instanceof CustomerFoodBeingServed) {
	        	notifyObserver(new CustomerEvent(CustomerEvent.CustomerEventType.FOOD_BEING_SERVE, null));
	        	try {
					Thread.sleep(4000);
					this.setTolerance(getTolerance() - 1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	while (this.state instanceof CustomerFoodBeingServed) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else if(this.state instanceof CustomerEatState) {
	        	try {
					Thread.sleep(6000);
					
					// customer leave
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	notifyObserver(new CustomerEvent(CustomerEvent.CustomerEventType.DONE_EATING, null));
	        	while (this.state instanceof CustomerEatState) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
		}
        
    }
	public int getTolerance() {
		return tolerance;
	}
	public void setTolerance(int tolerance) {
		this.tolerance = tolerance;
	}
	
}
