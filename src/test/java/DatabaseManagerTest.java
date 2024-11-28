package test.java;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import main.java.Activity;
import main.java.DatabaseManager;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class DatabaseManagerTest {

	private ResultSet executeCommand(Connection conn, String command, String...parameters) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(command);

		for (int i = 0; i < parameters.length; i++) {
			statement.setString(i + 1, parameters[i]);
        }
		
		statement.execute();
		ResultSet results = statement.getResultSet();
		
		return results;
	}
	
	boolean databaseAndTablesExist(Connection conn) throws SQLException {
		String filePath = "test.db";
		File dbFile = new File(filePath);
		
		if (!Files.exists(dbFile.toPath())) {
			return false;
		}
		
		String[] commands = {
			"SELECT name FROM sqlite_master WHERE type='table' AND name='Data'",
			"SELECT name FROM sqlite_master WHERE type='table' AND name='Goal'"
		};
		
		for (int i=0; i<commands.length; i++) {
			String result = executeCommand(conn, commands[i]).getString("name");
			
			if (result == null) {
				return false;
			}
		}
		
		return true;
	}

	@Test
	void DatabaseManager_Test1() { // Database doesn't exist when constructor is called
		String filePath = "test.db";
		File dbFile = new File(filePath);
		
		try {
			Files.deleteIfExists(dbFile.toPath());
		} catch (IOException e) {
			fail("Failed to set up test environment.");
		}
		
		File f = new File(filePath);
		if(f.exists() && !f.isDirectory()) { 
		    fail("Failed to set up test environment.");
		}
		
		new DatabaseManager();
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:db.db");
		} catch (SQLException e1) {
			e1.printStackTrace();
			fail("Failed to set up test environment.");
		}
		
		try {
			assertTrue(databaseAndTablesExist(conn));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to set up test environment");
		}
	}

	@Test
	void DatabaseManager_Test2() { // Database does exist when constructor is called
		String filePath = "test.db";
		
		try { // Create the database
			DriverManager.getConnection("jdbc:sqlite:test.db").close();
		} catch (SQLException e) {
			fail("Failed to set up test environment.");
		}
		
		File f = new File(filePath);
		if(!f.exists()) { 
		    fail("Failed to set up test environment.");
		}
		
		new DatabaseManager();
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:db.db");
		} catch (SQLException e1) {
			e1.printStackTrace();
			fail("Failed to set up test environment.");
		}
		
		try {
			assertTrue(databaseAndTablesExist(conn));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to set up test environment");
		}
	}
	
	@Test
	void DatabaseManager_Test3() { // Database and tables already exist
		new DatabaseManager();
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:db.db");
		} catch (SQLException e1) {
			e1.printStackTrace();
			fail("Failed to set up test environment.");
		}
		
		try {
			assertTrue(databaseAndTablesExist(conn));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to set up test environment");
		}
	}

	@Test
	void viewData() {
		String filePath = "db.db";

		try { // Create the database
			DriverManager.getConnection("jdbc:sqlite:db.db").close();;
		} catch (SQLException e) {
			fail("Failed to set up test environment.");
		}

		File f = new File(filePath);
		if(!f.exists()) {
		    fail("Failed to set up test environment.");
		}

		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
			Statement statement = conn.createStatement();
			statement.execute("INSERT INTO activityKind VALUES (1, 'WORK')");
			statement.execute("CREATE TABLE data (time BIGINT, duration BIGINT, kind INT");
			statement.execute("INSERT INTO data VALUES (1, 1, 1)");
		}
		catch (SQLException e) {
			fail("Failed to set up test environment.");
		}

		try {
			DatabaseManager dbm = new DatabaseManager();
			dbm.getAllData();
		} catch (SQLException e) {
			fail("Failed to view data.");
		}

		try{
			Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
			Statement statement = conn.createStatement();
			statement.execute("DROP TABLE data");
			statement.execute("DROP TABLE activityKind");
		}
		catch (SQLException e) {
			fail("Failed to clean up test environment.");
		}
	}

	@Test
	void viewGoal() {
		String filePath = "db.db";
		try { // Create the database
			DriverManager.getConnection("jdbc:sqlite:db.db").close();;
		} catch (SQLException e) {
			fail("Failed to set up test environment.");
		}

		File f = new File(filePath);
		if(!f.exists()) {
		    fail("Failed to set up test environment.");
		}

		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
			Statement statement = conn.createStatement();
			statement.execute("INSERT INTO activityKind VALUES (1, 'WORK')");
			statement.execute("CREATE TABLE goal (value BIGINT, kind INT)");
			statement.execute("INSERT INTO goal VALUES (1, 1, 1)");
		}
		catch (SQLException e) {
			fail("Failed to set up test environment.");
		}

		DatabaseManager dbm = new DatabaseManager();
		dbm.getGoal(Activity.kindFrom(1));

		try{
			Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
			Statement statement = conn.createStatement();
			statement.execute("DROP TABLE goal");
			statement.execute("DROP TABLE activityKind");
		}
		catch (SQLException e) {
			fail("Failed to clean up test environment.");
		}
	}
	@Test
	void getAllDataTest(){
		String filePath = "mytest123.db";
		try { // Create the database
			Connection conn = DriverManager.getConnection("jdbc:sqlite:mytest123.db");
			Statement statement = conn.createStatement();
			statement.execute("CREATE TABLE Data(kind INT, time BIGINT, duration BIGINT)");
			statement.execute("INSERT INTO ActivityKind VALUES (0, 'SOCIAL')");
			statement.execute("INSERT INTO Data VALUES (0, 234234, 3)"); //kind time duration
			
		} catch (SQLException e) { 
			fail("Failed to set up test environment.");
		}

		File f = new File(filePath);
		if(!f.exists()) {
		    fail("Failed to set up test environment.");
		}
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(new Activity("123", "1" , "WORK"));
		try{
			DatabaseManager dbm = new DatabaseManager();
			activities = dbm.getAllData();

		} catch (Exception e){
			fail("failed to get the data");
		}
			assertEquals(1, activities.size());
			assertEquals(234234, activities.get(0).getTime());
			assertEquals(3, activities.get(0).getDuration());
			assertEquals("SOCIAL", activities.get(0).getKind());
		
	}
}
