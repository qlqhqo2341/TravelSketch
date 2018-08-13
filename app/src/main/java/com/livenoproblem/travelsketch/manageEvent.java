package com.livenoproblem.travelsketch;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.livenoproblem.travelsketch.Struct.Event;
import com.livenoproblem.travelsketch.Struct.Travel;

import java.util.Calendar;

public class manageEvent extends AppCompatActivity {
    static final int ADD_EVENT=1,MANAGE_EVENT=2;

    Travel trav;
    int commandCode;

    Calendar startTime,endTime;
    Location space;
    String act;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event);

        Intent receiveIntent = getIntent();
        commandCode=receiveIntent.getIntExtra("command",ADD_EVENT);
        trav = (Travel)receiveIntent.getSerializableExtra("travel");
    }
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onStop() {
        super.onStop();

        Intent intent = new Intent();

    }
}
