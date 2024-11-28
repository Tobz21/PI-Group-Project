package main.java;

import java.sql.SQLException;
import java.util.Scanner;

public class Interface {

	public static void main(String[] args) {
		PIApp app = new PIApp();
		while (chooseMenuOption(app)) {
			// Loop will continue until chooseMenuOption returns false
		}
		System.out.println("Program terminated.");
	}

	public static String getUserInput(String prompt) {
		System.out.print(prompt);
		Scanner inputScanner = new Scanner(System.in);
		String input = inputScanner.nextLine();

		return input;
	}

	public static boolean chooseMenuOption(PIApp app) {
		boolean exit = false;
		System.out.println("What would you like to do");
		System.out.println("Please choose from the following options");
		System.out.println("1. View Data\n2. Set Data\n3. View goal\n4. Set Goal\n5. Compare Data\n6. Exit");
		
		String input = getUserInput("");
		
		switch (input) {
		case "1":
			app.viewData();
			break;
		case "2":
			app.setData();
			break;
		case "3":
			app.viewGoal();
			break;
		case "4":
			app.setGoal();
			break;
		case "5":
			app.compareData();
			break;
		case "6":
			exit = true;
			break;
		default:
			System.out.println("Invalid input");
		}
		return !exit;
	}
}
