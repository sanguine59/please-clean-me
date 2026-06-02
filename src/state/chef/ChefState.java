package state.chef;

import model.Chef;

public interface ChefState {
	void display();
	void handle(Chef chef);
}
