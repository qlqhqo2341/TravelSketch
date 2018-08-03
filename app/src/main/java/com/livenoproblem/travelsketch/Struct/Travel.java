package com.livenoproblem.travelsketch.Struct;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;

public class Travel {
    private ArrayList<Event> events;

    public Travel(){
        events = new ArrayList<Event>();
    }
    public boolean addEvent(Event e){
        // TODO check overlap event at time
        events.add(e);
        sort();
        return true;
    }
    private void sort(){
        Collections.sort(events);
    }
    public Event[] getEvents(){
        return events.toArray(new Event[events.size()]); // TODO need to be check correct works.

    }
}