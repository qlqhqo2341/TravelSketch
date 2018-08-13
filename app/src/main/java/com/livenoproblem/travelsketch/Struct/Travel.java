package com.livenoproblem.travelsketch.Struct;

import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Travel implements Serializable{
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
            nextCond = i==events.size() || events.get(i).getStartTime().compareTo(newbieEndtime) > 0;

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

    public int getEventIndex(Event e){
        return events.indexOf(e);
    }
    public boolean changeEvent(Event origin, Event newbie){
        final String tag = "ChangingEvent";
        ArrayList<Event> test = new ArrayList<Event>(events);
        if (test.remove(origin)==false)
            Log.e(tag,"There is not origin Event");
        test.add(newbie);

        Collections.sort(test);
        Event prev = null;
        for(Event n : test){
            if(prev==null || prev.getEndTime().compareTo(n.getStartTime()) <= 0){
                prev=n;
            }
            else{
                return false;
            }
        }
        events = test;
        return true;
    }

    public Event continuousLastEvent(){
        Event prev = null;
        for(Event e : events){
            if(prev==null){
                prev=e;
                continue;
            }
            if(prev.getEndTime().compareTo(e.getStartTime()) < 0 )
                return prev;
            prev=e;
        }
        return events.get(events.size()-1);
    }
}