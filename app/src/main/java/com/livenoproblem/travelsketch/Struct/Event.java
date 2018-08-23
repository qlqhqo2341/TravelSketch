package com.livenoproblem.travelsketch.Struct;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;

public class Event implements Serializable, Comparable<Event>{
    private Calendar startTime,endTime;
    private Space space;
    private String action;


    public Event(Calendar startTime, Calendar endTime, String spaceId, String spaceDescription, String action) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.space = new Space(spaceId,spaceDescription);
        this.action = action;
        timeCheck();
    }
    public Event(){
        startTime = Calendar.getInstance();
        startTime.set(Calendar.SECOND,0);
        endTime = (Calendar)startTime.clone();
        endTime.add(Calendar.SECOND,3);
        space = null;
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
    public static String getTimeString(Calendar time){
        int hour=time.get(Calendar.HOUR);
        String AMPM = (time.get(Calendar.AM_PM)==Calendar.AM) ? "AM" : "PM";
        if(hour==0) hour=12;
        return String.format("%2s %02d:%02d",AMPM,hour,time.get(Calendar.MINUTE));
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public Space getSpace() {
        return space;
    }

    @Override
    public int compareTo(@NonNull Event event) {
        return startTime.compareTo(event.getStartTime());
    }

    public static int compareDate(Calendar one, Calendar two){
        Date oned = Date.valueOf(String.format("%04d-%02d-%02d",
                one.get(Calendar.YEAR),
                one.get(Calendar.MONTH)+1,
                one.get(Calendar.DAY_OF_MONTH)));
        Date twod = Date.valueOf(String.format("%04d-%02d-%02d",
                two.get(Calendar.YEAR),
                two.get(Calendar.MONTH)+1,
                two.get(Calendar.DAY_OF_MONTH)));
        return oned.compareTo(twod);
    }

    public class Space implements Serializable{
        public final String id, description;
        public Space(String id, String description){
            this.id=id; this.description=description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }
}
