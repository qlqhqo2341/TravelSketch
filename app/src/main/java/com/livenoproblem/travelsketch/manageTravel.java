package com.livenoproblem.travelsketch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.livenoproblem.travelsketch.Struct.Event;
import com.livenoproblem.travelsketch.Struct.Travel;

import java.util.Calendar;

public class manageTravel extends Activity {
    float dpFactor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_travel);

        //get dpFactor
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dpFactor=displayMetrics.density;
        Log.i("TEST","dpFactor : " + dpFactor);

        Travel trav = new Travel();
        trav.addEvent(new Event());
        trav.addEvent(new Event());
        initGrid(trav);
    }

    private void initGrid(Travel trav){
        LinearLayout eventList = (LinearLayout)findViewById(R.id.eventList);
        Event[] events = trav.getEvents();
        for(Event e : events){
            LinearLayout eventLayout = new LinearLayout(getApplicationContext());
            eventLayout.setOrientation(LinearLayout.HORIZONTAL);

            LayoutParams textp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            textp.weight=1.0f; // check equal all TODO
            TextView timeText,spaceText,actText;
            timeText = new TextView(getApplicationContext());
            timeText.setText(e.getStartTimeString() + " ~ " + e.getEndTimeString());
            spaceText = new TextView(getApplicationContext());
            spaceText.setText(e.getSpace().toString());
            actText = new TextView(getApplicationContext());
            actText.setText(e.getAction());
            eventLayout.addView(timeText,textp);
            eventLayout.addView(spaceText,textp);
            eventLayout.addView(actText,textp);

            // convert 5dp to px
            int margin=(int)(5*dpFactor);

            LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            p.setMargins(margin,margin,margin,margin);
            eventList.addView(eventLayout,p);
        }
    }

}
