package state.chef;

import model.Chef;

public class ChefCookDoneState implements ChefState {
	private Chef chef;
	public ChefCookDoneState(Chef chef) {
		this.chef = chef;
		display();
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		chef.setStateName("idle");
		chef.setCurrentAction("done cooking(" + chef.getAssignedCustomer().getName() + ")");
	}
	
}
