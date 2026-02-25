package state.chef;

import model.Chef;

public class ChefIdleState implements ChefState {
	private Chef chef;
	public ChefIdleState(Chef chef) {
		this.chef = chef;
		display();
	}
	
	
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		chef.setStateName("idle");
		chef.setCurrentAction("(idle)");
	}
	
}
