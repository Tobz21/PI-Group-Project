package main.java;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static main.java.Interface.getUserInput;

public class PIApp {
	
	public void viewData() {
		DatabaseManager dbm = new DatabaseManager();
		List<Activity> activities = new ArrayList<Activity>();
		try {
			activities = dbm.getAllData();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("There was an error fetching data from the database.");
		}
		for (Activity activity : activities) {
			System.out.println("Activity: " + activity.getKind() + " Time: " + activity.getTime() + " Duration: " + activity.getDuration());
		}
	}
	
	public void setData() {
		String input = Interface.getUserInput("Please enter which kind of Activity to log, Work or Social:");
		while (!input.equalsIgnoreCase("WORK") && !input.equalsIgnoreCase("SOCIAL")) {
			input = Interface.getUserInput("Invalid input. Please enter Work or Social.");
		}
		input = input.toUpperCase();
		String kind;
		if (input.equals("SOCIAL")) {
			kind = "SOCIAL";
		} else {
			kind = "WORK";
		}
		long number;
		while (true) {
			input = Interface.getUserInput("Please enter the time (hour) which the activity starts");
			try {
                number = Long.parseLong(input); // 
				if (number < 0) {
                    System.out.println("Time must be input in epoch format, non-negative");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter an epoch time");
            }
		}
		String time = input;
		while (true) {
			input = Interface.getUserInput("Please enter the duration (hours) which the activity lasts for:");
			try {
                number = Integer.parseInt(input);
				if (number <= 0) {
                    System.out.println("Duration must be non-negative and not 0");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
		}
		String duration = input;
		DatabaseManager dbSet = new DatabaseManager();
		boolean success = dbSet.logData(new Activity(time, duration, kind));
		if (success){
			System.out.println("Logged successfully");
		} else {
			System.out.println("Log was unsuccessful");
		}
	}
	
	public void viewGoal(){
		DatabaseManager dbm = new DatabaseManager();
		String activity = getUserInput("Please enter the kind of activity you want to view:");
		while (!activity.equalsIgnoreCase("Work") && !activity.equalsIgnoreCase("Work")) {
			activity = getUserInput("Please enter the kind of activity you want to view:");
		}
		
		ActivityKind kind = ActivityKind.valueOf(activity.toUpperCase());
		
		Optional<Long> goalOptional = dbm.getGoal(kind);
		if (goalOptional.isPresent()) {
			System.out.println("Activity: " + activity + " Goal: " + goalOptional.get());
		} else {
			System.out.println("No goal set for this activity");
		}
	}

	public void setGoal() {
		String input = Interface.getUserInput("Please enter which kind of Activity to set the goal, Work or Social:");
		while (!input.equalsIgnoreCase("WORK") && !input.equalsIgnoreCase("SOCIAL")) {
			input = Interface.getUserInput("Invalid input. Please enter Work or Social.");
		}
		input = input.toUpperCase();
		ActivityKind kind;
		if (input.equals("SOCIAL")) {
			kind = ActivityKind.SOCIAL;
		} else {
			kind = ActivityKind.WORK;
		}
		int number;
		while (true) {
			input = Interface.getUserInput("Please enter the goal time (hours):");
			try {
                number = Integer.parseInt(input);
                if (number <= 0) {
                    System.out.println("Duration must be non-negative and not 0");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
		}
		long goal = number;
		DatabaseManager dbSet = new DatabaseManager();
		boolean success = dbSet.setGoal(kind , goal);
		if (success){
			System.out.println("Set goal successfully");
		} else {
			System.out.println("Goal setting was unsuccessful");
		}
	}

	public void compareData() {
		
	}
}
