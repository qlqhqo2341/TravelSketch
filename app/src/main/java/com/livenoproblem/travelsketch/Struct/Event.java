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
        this.space = (spaceId!=null) ? new Space(spaceId,spaceDescription) : null;
        this.action = action;
        timeCheck();
    }
    public Event(){
        startTime = Calendar.getInstance();
        startTime.set(Calendar.SECOND,0);
        endTime = (Calendar)startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY,2);
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
    public static String getDateString(Calendar date){
        return String.format("%04d-%02d-%02d (%s요일)",
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH)+1,
                date.get(Calendar.DAY_OF_MONTH),
                new String[]{null,"일","월","화","수","목","금","토"}[date.get(Calendar.DAY_OF_WEEK)]);
    }
    public int compareTimeTo(Calendar now){
        //시간을 기준으로 이벤트의 상태를 반환
        //시작전 -1, 진행중 0, 종료후 1

        boolean isNowOverStart = now.compareTo(startTime)>=0,
                isNowOverEnd = now.compareTo(endTime)>=0;
        if(isNowOverStart && isNowOverEnd)
            return -1;
        else if(!isNowOverStart && !isNowOverEnd)
            return 1;
        else if(isNowOverStart && !isNowOverEnd)
            return 0;
        else{
            Log.e("Event","guess to end time is before start. Compare is " + (endTime.compareTo(startTime)>0? "Cool" :" Wrong"));
            return 0;
        }
    }
}
