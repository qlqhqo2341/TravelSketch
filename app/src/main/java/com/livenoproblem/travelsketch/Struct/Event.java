package com.livenoproblem.travelsketch.Struct;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;

public class Event implements Serializable, Comparable<Event>{
    private Calendar startTime,endTime;
    private Location space;
    private String action;

    public Event(Calendar startTime, Calendar endTime,Location space, String action) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.space = space;
        this.action = action;
        timeCheck();
    }
    public Event(){
        startTime = Calendar.getInstance();
        startTime.set(Calendar.SECOND,0);
        endTime = (Calendar)startTime.clone();
        endTime.add(Calendar.SECOND,3);
        space = new Location("gps");
        action = "default";
    }
    private void timeCheck(){
        if (endTime.compareTo(startTime) < 0) {
            String tag = "EventModifier";
            Log.e(tag, "Start Time : " + startTime.toString());
            Log.e(tag, "End time : " + endTime.toString());
            Log.e(tag, "Endtime is before startTime. Modify it.");
            endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    private String getTimeString(Calendar time){
        return String.valueOf(time.get(Calendar.HOUR)) + ':' +
            String.valueOf(time.get(Calendar.MINUTE)) + ':' +
            String.valueOf(time.get(Calendar.SECOND));
}
    public Calendar getStartTime(){
        return startTime;
    }

    public String getStartTimeString(){
        return getTimeString(startTime);
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
        timeCheck();
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public String getEndTimeString(){
        return getTimeString(endTime);
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
        timeCheck();
    }

    public Location getSpace() {
        return space;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public int compareTo(@NonNull Event event) {
        return startTime.compareTo(event.getStartTime());
    }
}
