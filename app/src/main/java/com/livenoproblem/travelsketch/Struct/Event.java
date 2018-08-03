package com.livenoproblem.travelsketch.Struct;

import android.location.Location;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class Event implements Comparable<Event>{
    private Calendar startTime,endTime;
    private Location space;
    private String action;

    public Event(Calendar startTime, Calendar endTime,Location space, String action){
        this.startTime=startTime;
        this.endTime=endTime;
        this.space=space;
        this.action=action;
    }

    public Event(){
        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();
        endTime.add(Calendar.SECOND,3);
        space = null;
        action = "default";
    }

    public Calendar getStartTime(){
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    @Override
    public int compareTo(@NonNull Event event) {
        return startTime.compareTo(event.getStartTime());
    }

}
