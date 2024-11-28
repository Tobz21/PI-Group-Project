package test.java;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.PIApp;

class PIAppTest {
	
	private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private InputStream stdIn;
	private PrintStream stdOut = System.out;

	@BeforeEach
	public void initStream() {
	    System.setOut(new PrintStream(outputStream));
		stdIn = System.in;
	}
	
	@AfterEach
	public void revertStream() {
	    System.setOut(stdOut);
		System.setIn(stdIn);
	}

	@Test
	void test() {
		System.out.print("hello");
		assertEquals("hello", outputStream.toString());
	}

	@Test
	void setDataTestV() { //test that should be valid
		String autoInput = "Social\n5\n2\n";
		PIApp testApp = new PIApp();
		try {
			System.setIn(new ByteArrayInputStream(autoInput.getBytes()));
			System.setOut(new PrintStream(outputStream));
			testApp.setData();
			assertTrue(outputStream.toString().contains("Logged successfully"));
			assertTrue(outputStream.toString().contains("Please enter which kind of Activity to log, Work or Social:"));
		} catch (Exception e){
			System.out.println("Exception caught: " + e.getMessage());
        	e.printStackTrace();
			fail ("Exception thrown");
		}
	}
	@Test
	void setDataTestIV1() { //test for no duplication in database
		String autoInput = "Social\n5\n2\n";
		PIApp testApp = new PIApp();
		try {
			System.setIn(new ByteArrayInputStream(autoInput.getBytes()));
			testApp.setData();
			assertTrue(outputStream.toString().contains("Log was unsuccessful"));
		} catch (Exception e){
			fail ("Exception thrown");
		}
	}

	@Test
	void setDataTestIV2() { //test for no negative numbers
		String autoInput = "Social\n-3\n3\n-2\n2";
		PIApp testApp = new PIApp();
		try {
			System.setIn(new ByteArrayInputStream(autoInput.getBytes()));
			testApp.setData();
			assertTrue(outputStream.toString().contains("Time must be input in epoch format, non-negative"));
			assertTrue(outputStream.toString().contains("Duration must be non-negative and not 0")); 
			assertTrue(outputStream.toString().contains("Logged successfully"));
		} catch (Exception e){
			fail ("Exception thrown");
		}
	}

	@Test
	void setDataTestIV3() { //test for no invalid inputs
		String autoInput = "Wo\nWork\na\n4\nb\n5";
		PIApp testApp = new PIApp();
		try {
			System.setIn(new ByteArrayInputStream(autoInput.getBytes()));
			testApp.setData();
			assertTrue(outputStream.toString().contains("Invalid input. Please enter Work or Social."));
			assertTrue(outputStream.toString().contains("Duration must be non-negative and not 0")); 
			assertTrue(outputStream.toString().contains("Invalid input. Please enter a valid integer"));
			assertTrue(outputStream.toString().contains("Logged successfully"));
		} catch (Exception e){
			fail ("Exception thrown");
		}
	}

	@Test
	void setGoalTestV() { //test for valid usage
		String autoInput = "Social\n3\n";
		PIApp testApp = new PIApp();
		try {
			System.setIn(new ByteArrayInputStream(autoInput.getBytes()));
			testApp.setGoal();
			assertTrue(outputStream.toString().contains("Set goal successfully")); 

		} catch (Exception e){
			fail ("Exception thrown");
		}
	}

	@Test
	void setGoalTestIV1() { //test for no negative/0 numbers
		String autoInput = "Social\n-3\n3\n";
		PIApp testApp = new PIApp();
		try {
			System.setIn(new ByteArrayInputStream(autoInput.getBytes()));
			testApp.setGoal();
			assertTrue(outputStream.toString().contains("Duration must be non-negative and not 0")); 
			assertTrue(outputStream.toString().contains("Set goal successfully")); 
		} catch (Exception e){
			fail ("Exception thrown");
		}
	}

	@Test
	void setGoalTestIV2() { //test for invalid inputs
		String autoInput = "Socia\nSocial\na\n4\n";
		PIApp testApp = new PIApp();
		try {
			System.setIn(new ByteArrayInputStream(autoInput.getBytes()));
			testApp.setGoal();
			assertTrue(outputStream.toString().contains("Invalid input. Please enter Work or Social."));
			assertTrue(outputStream.toString().contains("Invalid input. Please enter a valid integer.")); 
			assertTrue(outputStream.toString().contains("Set goal successfully")); 
		} catch (Exception e){
			fail ("Exception thrown");
		}
	}
	@Test
	void viewDataTest() {

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
			statement.execute("CREATE TABLE data (time BIGINT, duration BIGINT, kind INT");
			statement.execute("INSERT INTO data VALUES (1, 1, 1)");
		}
		catch (SQLException e) {
			fail("Failed to set up test environment.");
		}

		try {
			PIApp app = new PIApp();
			app.viewData();
			assertEquals("Data viewed", outputStream.toString());
		} catch (Exception e) {
			fail("Exception thrown");
		}

		try{
			Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
			Statement statement = conn.createStatement();
			statement.execute("DROP TABLE data");
		}
		catch (SQLException e) {
			fail("Failed to clean up test environment.");
		}
	}

	@Test
	void viewGoalTest() {

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
			statement.execute("CREATE TABLE goal (value BIGINT, kind INT)");
			statement.execute("INSERT INTO goal VALUES (1, 1, 1)");
		}
		catch (SQLException e) {
			fail("Failed to set up test environment.");
		}

		try {
			PIApp app = new PIApp();
			app.viewGoal();
			assertEquals("Goal viewed", outputStream.toString());
		} catch (Exception e) {
			fail("Exception thrown");
		}

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
}
