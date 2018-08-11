package com.livenoproblem.travelsketch.Struct;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class Travel {
    private ArrayList<Event> events;

    public Travel(){
        events = new ArrayList<Event>();
    }
    public boolean addEvent(Event e){
        Calendar newbieStarttime = e.getStartTime();
        Calendar newbieEndtime = e.getEndTime();
        Event prev = null;

        final String overlapTag = "dataError",overlapStr = "There is overlap event. reject";
        int i;
        for(i=0;i<=events.size();i++){
            boolean prevCond,nextCond;
            prevCond = i==0 || events.get(i-1).getEndTime().compareTo(newbieStarttime) < 0;
            nextCond = i==events.size() || events.get(i).getStartTime().compareTo(newbieEndtime)>0;

            if(prevCond && nextCond){
                events.add(i,e);
                break;
            }
        }

        if(i==events.size()+1){
            Log.e(overlapTag,overlapStr);
            return false;
        }
        else
            return true;
    }
    public boolean removeEvent(Event e){
        events.remove(e);
        return false;
    }
    public Event[] getEvents(){
        return events.toArray(new Event[events.size()]);
    }
}