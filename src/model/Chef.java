package model;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mediator.RestaurantMediator;
import observer.Observer;
import observer.Subject;
import observer.event.ChefEvent;
import state.chef.ChefCookDoneState;
import state.chef.ChefCookState;
import state.chef.ChefIdleState;
import state.chef.ChefState;

public class Chef extends Npc implements Subject, Runnable{
	private String name;
	private String currentAction;
	private int skill;
	private int speed;
	private ChefState state;
	private String stateName;
	private Customer assignedCustomer;
	private Waiter assignedWaiter;
	private Order assignedOrder;
	private RestaurantMediator mediator;
	private ArrayList<Observer> observers = new ArrayList<>();
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
	
	public Chef(String name, RestaurantMediator mediator) {
		this.name = name;
		this.speed = 0;
		this.skill = 1;
		this.setMediator(mediator);
		this.state = new ChefIdleState(this);
		this.registerObserver(mediator);
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ChefState getState() {
		return state;
	}

	public void setState(ChefState state) {
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

	public Waiter getAssignedWaiter() {
		return assignedWaiter;
	}

	public void setAssignedWaiter(Waiter assignedWaiter) {
		this.assignedWaiter = assignedWaiter;
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

	@Override
	public void run() {
		
		while(true) {
			pauseLock.lock();
			try {
				while(isPaused) {
					System.out.println("a");
					unpaused.await();
					System.out.println("b");
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			} finally {
				pauseLock.unlock();
			}
			
			
			
			if(this.state instanceof ChefIdleState) {
	        	notifyObserver(new ChefEvent(ChefEvent.ChefEventType.IDLE, getAssignedOrder()));
	        	while (this.state instanceof ChefIdleState) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else if(this.state instanceof ChefCookState) {
	        	notifyObserver(new ChefEvent(ChefEvent.ChefEventType.START_COOKING, getAssignedOrder()));
	        	int duration = 6 - this.getSpeed();
	        	duration = duration * 1000;
	        	try {
					Thread.sleep(duration);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	notifyObserver(new ChefEvent(ChefEvent.ChefEventType.COOKING_DONE, getAssignedOrder()));
	        	while (this.state instanceof ChefCookState) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        } else if(this.state instanceof ChefCookDoneState) {
	        	// need a waiter to take food to go back to idle
	        	
	        	while (this.state instanceof ChefCookDoneState) {
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
	
	public void cookOrder(Order order) {
        this.setAssignedOrder(order);
        setAssignedCustomer(order.getCustomer());
        
        this.setState(new ChefCookState(this));
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
