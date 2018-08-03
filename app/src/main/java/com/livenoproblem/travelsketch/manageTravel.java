package com.livenoproblem.travelsketch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.livenoproblem.travelsketch.Struct.Event;
import com.livenoproblem.travelsketch.Struct.Travel;

public class manageTravel extends Activity {
    float dpFactor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_travel);
//        dpFactor = getResources().getDisplayMetrics().densityDpi; TODO error getResource() NULL pointer
        dpFactor=1.0f;

        Travel trav = new Travel();
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
//            textp.weight=1.0f; // check equal all TODO
            TextView timeText,spaceText,actText;
            timeText = new TextView(getApplicationContext());
            spaceText = new TextView(getApplicationContext());
            actText = new TextView(getApplicationContext());
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
