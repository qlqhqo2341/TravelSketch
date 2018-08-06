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
        for(int i=0;i<events.size();i++){
            Event obj = events.get(i);
            //Find first bigger node at list..
            if(newbieStarttime.compareTo(obj.getStartTime())>0){
                //Check overlap if prev is null
                if(prev==null){
                    if(obj.getStartTime().compareTo(newbieEndtime)>0){
                        events.add(0,e);
                        return true;
                    }else{
                        Log.e(overlapTag,overlapStr);
                        return false;
                    }
                }
                //Check overlap
                if(prev.getEndTime().compareTo(newbieStarttime)<0 &&
                        obj.getStartTime().compareTo(newbieEndtime)>0){
                    events.add(i,e);
                    break;
                }else{
                    Log.e(overlapTag,overlapStr);
                    return false;
                }
            }else{
                prev=obj;
            }
        }
        events.add(e);
        return true;
    }
    public boolean removeEvent(Event e){
        events.remove(e);
        return false;
    }
    public Event[] getEvents(){
        return events.toArray(new Event[events.size()]); // TODO need to be check correct works.
    }
}