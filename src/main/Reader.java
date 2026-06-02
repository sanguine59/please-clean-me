package main;

import java.util.Scanner;

import mediator.RestaurantMediator;

public class Reader implements Runnable {
	private final RestaurantMediator mediator;
	private final Main main;

	public Reader(RestaurantMediator mediator, Main main) {
		this.mediator = mediator;
		this.main = main;
	}

	@Override
	public void run() {
		Scanner scan = new Scanner(System.in);
		while (true) {
			String input = scan.nextLine();
			if (input.isEmpty()) {
				if (mediator.isPaused()) {
					mediator.resumeSimulation();
				} else {
					mediator.pauseSimulation();
					main.pauseGame();
				}
			}
		}
	}
}
