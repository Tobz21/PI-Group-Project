package test.java;

import org.junit.jupiter.api.Test;

import main.java.Activity;
import main.java.ActivityKind;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    @Test
    void activityConstructor_Test1(){ //kind invalid case
        String time = "111";
        String duration = "111";
        String kind = "";
        assertThrows(IllegalArgumentException.class,
                () -> {
            new Activity(time, duration, kind);
        });
    }

    @Test
    void activityConstructor_Test2(){ // duration invalid case
        String time = "111";
        String duration = "";
        String kind = "";
        assertThrows(NumberFormatException.class,
                () -> {
                    new Activity(time, duration, kind);
                });
    }

    @Test
    void activityConstructor_Test3(){ // time invalid case
        String time = "";
        String duration = "";
        String kind = "";
        assertThrows(NumberFormatException.class,
                () -> {
                    new Activity(time, duration, kind);
                });
    }

    @Test
    void activityConstructor_Test4(){ // lower case kind "work" case
        String time = "111";
        String duration = "111";
        String kind = "work";
        assertThrows(IllegalArgumentException.class,
                () -> {
                    new Activity(time, duration, kind);
                });
    }

    @Test
    void activityConstructor_Test5(){ // lower case kind "social" case
        String time = "111";
        String duration = "111";
        String kind = "social";
        assertThrows(IllegalArgumentException.class,
                () -> {
                    new Activity(time, duration, kind);
                });
    }

    @Test
    void activityConstructor_Test6(){ // time null case
        String time = null;
        String duration = "";
        String kind = "";
        assertThrows(NumberFormatException.class,
                () -> {
                    new Activity(time, duration, kind);
                });
    }

    @Test
    void activityConstructor_Test7(){ // duration null case
        String time = "1";
        String duration = null;
        String kind = "";
        assertThrows(NumberFormatException.class,
                () -> {
                    new Activity(time, duration, kind);
                });
    }

    @Test
    void activityConstructor_Test8(){ // kind null case
        String time = "1";
        String duration = "111";
        String kind = null;
        assertThrows(NullPointerException.class,
                () -> {
                    new Activity(time, duration, kind);
                });
    }

    @Test
    void getTime_Test1(){
        String time = "123";
        String duration = "2343";
        String kind = "SOCIAL";
        long expected = 123;
        Activity testActivity = new Activity(time, duration, kind);
        assertEquals(expected, testActivity.getTime());
    }

    @Test
    void getDuration_Test1(){
        String time = "123";
        String duration = "2343";
        String kind = "SOCIAL";
        long expected = 2343;
        Activity testActivity = new Activity(time, duration, kind);
        assertEquals(expected, testActivity.getDuration());
    }

    @Test
    void getKind_Test1(){ //kind = ActivityKind.WORK case
        String time = "111";
        String duration = "111";
        String kind = "WORK";
        ActivityKind expected = ActivityKind.WORK;
        Activity testActivity = new Activity(time, duration, kind);
        assertEquals(expected, testActivity.getKind());
    }

    @Test
    void getKind_Test2() { //kind = ActivityKind.SOCIAL case
        String time = "111";
        String duration = "111";
        String kind = "SOCIAL";
        ActivityKind expected = ActivityKind.SOCIAL;
        Activity testActivity = new Activity(time, duration, kind);
        assertEquals(expected, testActivity.getKind());
    }
}