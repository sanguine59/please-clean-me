package main;

import java.util.Scanner;

import mediator.RestaurantMediator;

public class reader implements Runnable{
	private RestaurantMediator mediator;
	private Main inMain;
	
	public reader(RestaurantMediator mediator, Main inMain) {
		this.mediator = mediator;
		this.inMain = inMain;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		while(true) {
			String input = scan.nextLine();
			if(input.isEmpty()) {
				if(mediator.isPaused()) {
					mediator.resumeSimulation();
		
				} else {
					mediator.pauseSimulation();
					inMain.pauseGame();
				}
			}
		}
	}
	
}
