package state.chef;

import model.Chef;

public class ChefCookState implements ChefState{
	private Chef chef;
	public ChefCookState(Chef chef) {
		this.chef = chef;
		display();
	}
	@Override
	public void display() {
		// TODO Auto-generated method stub
		chef.setStateName("busy");
		chef.setCurrentAction("cooking (" + chef.getAssignedCustomer().getName() + ")");
	}

}
