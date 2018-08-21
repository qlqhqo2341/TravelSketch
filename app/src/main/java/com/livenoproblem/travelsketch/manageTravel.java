package com.livenoproblem.travelsketch;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.livenoproblem.travelsketch.Struct.Event;
import com.livenoproblem.travelsketch.Struct.Travel;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

public class manageTravel extends AppCompatActivity implements View.OnClickListener{
    float dpFactor;
    HashMap<LinearLayout,Event> LayoutToEvent;

    Travel trav;
    Event managing=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_travel);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("일정 관리");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_finish:
                Intent intent = new Intent();
                intent.putExtra("travel",trav);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.action_revert:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void initGrid(){
        LinearLayout eventList = findViewById(R.id.eventList);
        eventList.removeAllViews();
        LayoutToEvent.clear();

        Event[] events = trav.getEvents();

        Calendar prevDate = null;

        // convert 5dp to px
        int margin=(int)(5*dpFactor);

        // LinearLayout params
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        p.setMargins(margin,margin,margin,margin);

        // Inner textview layoutParams.
        LayoutParams textp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        textp.weight=1.0f;

        for(Event e : events){
            Calendar date = e.getStartTime();
            if(prevDate == null || Event.compareDate(prevDate,date)!=0) {
                TextView dateText = new TextView(getApplicationContext());
                dateText.setText(String.format("%04d-%02d-%02d",
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH)+1,
                        date.get(Calendar.DAY_OF_MONTH)));
                dateText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                eventList.addView(dateText,p);
            }
            prevDate = date;

            LinearLayout eventLayout = new LinearLayout(getApplicationContext());
            eventLayout.setOrientation(LinearLayout.HORIZONTAL);
            eventLayout.setOnClickListener(this);

            LayoutToEvent.put(eventLayout,e);

            TextView timeText,spaceText,actText;
            timeText = new TextView(getApplicationContext());
            timeText.setText(e.getStartTimeString() + " ~ \n" + e.getEndTimeString());
            spaceText = new TextView(getApplicationContext());
            spaceText.setText(e.getSpace()==null ? "미지정" : e.getSpace().toString());
            actText = new TextView(getApplicationContext());
            actText.setText(e.getAction());
            eventLayout.addView(timeText,textp);
            eventLayout.addView(spaceText,textp);
            eventLayout.addView(actText,textp);

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

            Event e = LayoutToEvent.get(view);
            intent.putExtra("event",e);
            managing=e;
            trav.removeEvent(managing);
            startActivityForResult(intent,manageEvent.MANAGE_EVENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case manageEvent.ADD_EVENT:
                if(resultCode==RESULT_OK){
                    Event event = (Event)data.getSerializableExtra("event");
                    if(event==null)
                        break;

                    boolean result = trav.addEvent(event);
                    if(result==false)
                        Toast.makeText(getApplicationContext(),"시간이 겹쳐 추가에 실패했습니다.",Toast.LENGTH_SHORT).show();
                }
                break;
            case manageEvent.MANAGE_EVENT:
                if(resultCode==RESULT_OK){
                    Event modified = (Event)data.getSerializableExtra("event");
                    if(modified==null)
                        managing=null;
                    else{
                        boolean result = trav.addEvent(modified);
                        if(result==false){
                            Toast.makeText(getApplicationContext(),"시간이 겹쳐 수정에 실패했습니다.",Toast.LENGTH_SHORT).show();
                            trav.addEvent(managing);
                        }
                    }
                }
                break;
        }
        initGrid();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //TODO 저장여부 한번 더 확인.
    }
}
