package main.java;

public class Activity {

    long time;
    long duration;
    ActivityKind kind;

    public Activity (String time, String duration, String kind) throws NumberFormatException, IllegalArgumentException, NullPointerException {
        this.time = Long.parseLong(time);
        this.duration = Long.parseLong(duration);
        this.kind = ActivityKind.valueOf(kind);
    }

    public long getTime() {
        return this.time;
    }

    public long getDuration() {
        return this.duration;
    }

    public ActivityKind getKind() {
        return this.kind;
    }
    
    public static ActivityKind kindFrom(int i) {
    	if (i == 0) {
    		return ActivityKind.WORK;
    	} else if (i == 1) {
    		return ActivityKind.SOCIAL;
    	}
    	
    	return null;
    }
    
    public static int kindTo(ActivityKind kind) {
    	if (kind == ActivityKind.WORK) {
    		return 0;
    	} else if (kind == ActivityKind.SOCIAL) {
    		return 1;
    	}
    	
    	return -1;
    }
}
