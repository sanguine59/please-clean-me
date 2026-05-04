package main;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import facade.GameFacade;
import facade.ChefFacade;
import facade.WaiterFacade;

import model.Chef;
import model.Waiter;
import singleton.Restaurant;

public class Main {
    GameFacade game = new GameFacade(this);
	Scanner scan = new Scanner(System.in);
	
	public Main() {
		mainMenu();
	}
	
	public void mainMenu() {
		int choice;
        do {
            System.out.println("Main menu");
            System.out.println("1. Play New Restaurant");
            System.out.println("2. High Score");
            System.out.println("3. Exit");
            System.out.print("Input [1..3]: ");

            choice = getIntInput();

            if (choice == 1) {
                System.out.println("Enter Restaurant Name [3-15 characters]:");
                String name = scan.nextLine();
                if (name.length() > 2 && name.length() < 16) {
                    game.startGame(name);
                    return; // Exit main menu thread, game runs in background
                } else {
                    System.out.println("Please Enter A Valid Name");
                }
            } else if (choice == 3) {
                System.exit(0);
            }
        } while (true);
    }
	
	public int getIntInput() {
		try {
			int choice = scan.nextInt();
			scan.nextLine();
			return choice;
		} catch (InputMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			scan.nextLine();
			return -1;
		}
	}

    public void pauseGame() {
        game.pauseGame();
        int choice;
        do {
            System.out.println("\n--- GAME PAUSED ---");
            System.out.println("1. Continue Game");
            System.out.println("2. Upgrade Restaurant");
            System.out.println("3. Close Business");
            System.out.print("Input [1..3]: ");
            choice = getIntInput();

            if (choice == 1) break;
            if (choice == 2) showUpgradeMenu();
            if (choice == 3) System.exit(0);

        } while (true);
        game.resumeGame();
	}
    
    private void displayCurrentStatus() {
        Restaurant sb = game.getScoreboard();
        System.out.println("\nMoney: Rp." + sb.getMoney());
        System.out.println("Seats: " + game.getMaxSeats());
        System.out.println("Waiters: " + game.getWaiters().size());
        System.out.println("Chefs: " + game.getChefs().size());
        System.out.println("====================================\n");
    }

    private void showUpgradeMenu() {
        int choice;
        do {
            displayCurrentStatus();
            System.out.println("1. Increase Restaurant's Seat (Rp. " + (100 * game.getMaxSeats()) + ')');
            System.out.println("2. Hire New Employee");
            System.out.println("3. Upgrade Waiter");
            System.out.println("4. Upgrade Chef");
            System.out.println("5. Back to Pause Menu");
            System.out.print("Input [1..5]: ");
            choice = getIntInput();

            if (choice == 1) {
                if (game.increaseSeats()) System.out.println("Seats increased successfully!");
                else System.out.println("Failed to increase seats. Not enough money or at max capacity.");
            } else if (choice == 2) {
                showHireMenu();
            } else if (choice == 3) {
                showUpgradeWaiterMenu();
            } else if (choice == 4) {
                showUpgradeChefMenu();
            }
        } while (choice != 5);
    }

    private void showHireMenu() {
        int choice;
        do {
            displayCurrentStatus();
            System.out.println("1. Hire New Waiter (Rp. " + (game.getWaiters().size() * 150) + ") ");
            System.out.println("2. Hire New Chef (Rp. " + (game.getChefs().size() * 200) + ") ");
            System.out.println("3. Back");
            System.out.print("Input [1..3]: ");
            choice = getIntInput();

            if (choice == 1) {
                if (game.hireNewWaiter()) System.out.println("New waiter hired!");
                else System.out.println("Failed to hire waiter. Not enough money or at max capacity.");
            } else if (choice == 2) {
                if (game.hireNewChef()) System.out.println("New chef hired!");
                else System.out.println("Failed to hire chef. Not enough money or at max capacity.");
            }
        } while (choice != 3);
    }

    private void showUpgradeWaiterMenu() {
        int choice;
        List<Waiter> waiters = game.getWaiters();
        do {
            System.out.println("\n--- Upgrade Waiter (Cost: Rp. 150) ---");
            for (int i = 0; i < waiters.size(); i++) {
                System.out.printf("%d. %s Speed: %d\n", i + 1, waiters.get(i).getName(), waiters.get(i).getSpeed());
            }
            System.out.print("Input waiter number to upgrade [0 to Back]: ");
            choice = getIntInput();

            if (choice > 0 && choice <= waiters.size()) {
                if (game.upgradeWaiterSpeed(choice - 1)) System.out.println("Upgrade successful!");
                else System.out.println("Upgrade failed. Not enough money or waiter is max speed.");
            }
        } while (choice != 0);
    }

    private void showUpgradeChefMenu() {
        int choice;
        List<Chef> chefs = game.getChefs();
        do {
            System.out.println("\n--- Upgrade Chef (Cost: Rp. 150) ---");
            for (int i = 0; i < chefs.size(); i++) {
                System.out.printf("%d. %s Speed: %d | Skill: %d\n", i + 1, chefs.get(i).getName(), chefs.get(i).getSpeed(), chefs.get(i).getSkill());
            }
            System.out.print("Input chef number to upgrade [0 to Back]: ");
            choice = getIntInput();
            
            if (choice > 0 && choice <= chefs.size()) {
                System.out.print("Upgrade 'speed' or 'skill'?: ");
                String stat = scan.nextLine();
                if (game.upgradeChef(choice - 1, stat)) System.out.println("Upgrade successful!");
                else System.out.println("Upgrade failed. Invalid stat, not enough money, or chef is max level.");
            }
        } while (choice != 0);
    }
	
	
}