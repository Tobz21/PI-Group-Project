package test.java;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.Interface;
import main.java.PIApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest {
    private final PrintStream standardOut = System.out;
    private final InputStream standardIn = System.in;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    // WARNING!!!!, testing user input doesn't work as it should.
    // You can only write an input stream once and it cannot be changed
    void simulateUserInput(String input){ // helper function: puts input into system.in
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
    }

    @BeforeEach
    public void setUp() { // for each test, sets the output to one that we can read
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void clearOutAndIn(){ // for each test, sets in and out back to standard
        System.setOut(standardOut);
        System.setIn(standardIn);
    }

    @Test
    void programTerminatesCorrectly() {
        final String expectedOutput = "Program terminated.";
        Interface.main(new String[]{});
        String actualOutput = outputStreamCaptor.toString().trim();
        assertTrue(actualOutput.contains(expectedOutput), "Expected the output to contain 'Program terminated.' but was: " + actualOutput);
    }

    @Test
    void getUserInput_Test1(){ // multiple lines in, normal string out case. "Hey there\n bee" should give "Hey there"
        String testString = "Hey there\n bee";
        String expected = "Hey there";
        simulateUserInput(testString);
        assertEquals(expected, Interface.getUserInput("say so"));
        assertEquals("say so", outputStreamCaptor.toString());
    }

    @Test
    void getUserInput_Test2(){ // empty string in, empty string out case
        String testString = "";
        String expected = "";
        simulateUserInput(testString);
        assertThrows(NoSuchElementException.class, () -> Interface.getUserInput(("")));
        assertEquals(expected, outputStreamCaptor.toString());
    }

    @Test
    void getUserInput_Test3(){ // new line in, output which contains new line case
        String testString = "\n";
        String expected = "";
        simulateUserInput(testString);
        assertEquals(expected , Interface.getUserInput("enter a new line\n hey"));
        assertEquals("enter a new line\n hey", outputStreamCaptor.toString());
    }

    @Test
    void chooseMenuOption_Test1(){  //the following cases assume there are 5 function options shown to the user,plus number 6 as exit
        String testString = "2";    //case of input in range
        PIApp testApp = new PIApp();
        simulateUserInput(testString);
        assertTrue(Interface.chooseMenuOption(testApp));
    }

    @Test
    void chooseMenuOption_Test2(){  //input out of range
        String testString = "0";
        PIApp testApp = new PIApp();
        simulateUserInput(testString);
        assertTrue(Interface.chooseMenuOption(testApp));
    }

    @Test
    void chooseMenuOption_Test3(){  //the exit branch
        String testString = "6";
        PIApp testApp = new PIApp();
        simulateUserInput(testString);
        assertFalse(Interface.chooseMenuOption(testApp));
    }

    @Test
    void chooseMenuOption_Test4(){   //invalid input type
        String testString = "a";
        PIApp testApp = new PIApp();
        simulateUserInput(testString);
        assertTrue(Interface.chooseMenuOption(testApp));
    }
}
