package com.livenoproblem.travelsketch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.livenoproblem.travelsketch.Struct.Event;
import com.livenoproblem.travelsketch.Struct.Travel;

import java.util.HashMap;

public class manageTravel extends AppCompatActivity implements View.OnClickListener{
    float dpFactor;
    HashMap<LinearLayout,Event> LayoutToEvent;

    Travel trav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_travel);

        //get dpFactor
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dpFactor=displayMetrics.density;
        Log.i("TEST","dpFactor : " + dpFactor);

        ((FloatingActionButton)findViewById(R.id.addEventBtn)).setOnClickListener(this);

        Intent receive = getIntent();
        trav = (Travel) receive.getSerializableExtra("travel");
        LayoutToEvent = new HashMap<LinearLayout, Event>();
        initGrid();
    }

    private void initGrid(){
        LinearLayout eventList = findViewById(R.id.eventList);
        eventList.removeAllViews();
        LayoutToEvent.clear();

        Event[] events = trav.getEvents();
        for(Event e : events){
            LinearLayout eventLayout = new LinearLayout(getApplicationContext());
            eventLayout.setOrientation(LinearLayout.HORIZONTAL);
            eventLayout.setOnClickListener(this);

            LayoutToEvent.put(eventLayout,e);

            LayoutParams textp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            textp.weight=1.0f;
            TextView timeText,spaceText,actText;
            timeText = new TextView(getApplicationContext());
            timeText.setText(e.getStartTimeString() + " ~ " + e.getEndTimeString());
            spaceText = new TextView(getApplicationContext());
            spaceText.setText(e.getSpace()==null ? "미지정" : e.getSpace().toString());
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

    @Override
    public void onClick(View view) {
        if(view==findViewById(R.id.addEventBtn)) {
            Intent intent = new Intent(getApplicationContext(), manageEvent.class);
            intent.putExtra("command",manageEvent.ADD_EVENT);
            intent.putExtra("travel", trav);

            startActivityForResult(intent, manageEvent.ADD_EVENT);
        }
        if(view.getClass() == LinearLayout.class){
            Intent intent = new Intent(getApplicationContext(), manageEvent.class);
            intent.putExtra("command",manageEvent.MANAGE_EVENT);
            intent.putExtra("travel",trav);

            Event e = LayoutToEvent.get(view);
            intent.putExtra("event",e);
            startActivityForResult(intent,manageEvent.MANAGE_EVENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Travel trav = (Travel)data.getSerializableExtra("travel");
            this.trav = trav;
            initGrid();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Intent intent = new Intent();
        intent.putExtra("travel",trav);
        setResult(RESULT_OK,intent);
    }
}
