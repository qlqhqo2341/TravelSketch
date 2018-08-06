package com.livenoproblem.travelsketch.Struct;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;

public class Event implements Comparable<Event>{
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

    public Event(){
        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();
        endTime.add(Calendar.SECOND,3);
        space = new Location("gps");
        action = "default";
    }

    public Calendar getStartTime(){
        return startTime;
    }

    public String getStartTimeString(){
        StringBuilder sb = new StringBuilder();
        sb.append(startTime.get(Calendar.HOUR));
        sb.append(':');
        sb.append(startTime.get(Calendar.MINUTE));
        sb.append(':');
        sb.append(startTime.get(Calendar.SECOND));
        return sb.toString();
    }
    public String getEndTimeString(){
        StringBuilder sb = new StringBuilder();
        sb.append(endTime.get(Calendar.HOUR));
        sb.append(':');
        sb.append(endTime.get(Calendar.MINUTE));
        sb.append(':');
        sb.append(endTime.get(Calendar.SECOND));
        return sb.toString();
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
        timeCheck();
    }


    public Calendar getEndTime() {
        return endTime;
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
