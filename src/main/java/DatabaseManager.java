package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.sqlite.SQLiteException;

public class DatabaseManager {
	Connection conn;
	
	public DatabaseManager() {
		try {
			conn = createDatabase();
			
			createTables();
		} catch (SQLiteException e) {
			// All good, tables already exist
		} catch (SQLException e) {
			System.out.println("That action could not be completed.");
		}
	}
	
	private Connection createDatabase() throws SQLException {
		String url = "jdbc:sqlite:db.db";

        return DriverManager.getConnection(url);
	}
	
	private void createTables() throws SQLException, SQLiteException {
		String[] commands = {
			"CREATE TABLE Data(dataID INT PRIMARY KEY NOT NULL, kind INT, time BIGINT, duration BIGINT)",
			"CREATE TABLE Goal(goalID INT PRIMARY KEY NOT NULL, kind INT, value BIGINT)"
		};
		
		for (int i=0; i<commands.length; i++) {
			executeCommand(commands[i]);
		}
	}
	
	private ResultSet executeCommand(String command, String...parameters) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(command);

		for (int i = 0; i < parameters.length; i++) {
			statement.setString(i + 1, parameters[i]);
        }
		
		statement.execute();
		ResultSet results = statement.getResultSet();
		
		return results;
	}

	public List<Activity> getAllData() throws SQLException {
		List<Activity> activities = new ArrayList<Activity>();
		String query = "SELECT Data.time, Data.duration, Data.kind FROM Data";
		
		ResultSet rs;
		try {
            rs = executeCommand(query);
		} catch (SQLException e) {
			System.out.println("That action could not be completed.");
			return activities;
		}
		while (rs.next()) {
			String time = rs.getString("time");
        	String duration = rs.getString("duration");
        	String name = Activity.kindFrom(rs.getInt("kind")).toString();
        	
			Activity activity = new Activity(time, duration, name);
			activities.add(activity);
		}
		
		return activities;
	}
	
	public boolean logData(Activity activity) {
		try {
			System.out.println(String.valueOf(activity.getKind()));
			executeCommand(
				"INSERT INTO Data VALUES (?, ?, ?)",
				String.valueOf(activity.getKind()),
				String.valueOf(activity.getTime()),
				String.valueOf(activity.getDuration())
			);
		} catch (SQLException e) {
			return false;
		}
		
		return true;
	}
	
	public boolean setGoal(ActivityKind aKind, long value) {
		try {
			int kindInt = Activity.kindTo(aKind);
			
			executeCommand(
				"DELETE FROM Goals WHERE "
				+ "kind=?",
				String.valueOf(kindInt)
			);
			
			executeCommand(
				"INSERT INTO Goals VALUES (?)",
				String.valueOf(kindInt),
				String.valueOf(value)
			);
			
			return true;
		} catch (SQLException e) {
			System.out.println("That action could not be completed.");
		}
		
		return false;
	}

	public Optional<Long> getGoal(ActivityKind aKind) {
		try {
			int kindInt = Activity.kindTo(aKind);
			
			ResultSet results = executeCommand(
				"SELECT value from Goals WHERE "
				+ "kind = ?",
				String.valueOf(kindInt)
			);
			
			if (results.next()) {
				return Optional.of(results.getLong("value"));
			}
		} catch (SQLException e) {
			System.out.println("That action could not be completed.");
		}
		
		return Optional.empty();
	}
}
