package com.livenoproblem.travelsketch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.livenoproblem.travelsketch.Struct.Event;
import com.livenoproblem.travelsketch.Struct.Travel;


import java.util.Calendar;

public class manageEvent extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    static final int ADD_EVENT=1,MANAGE_EVENT=2;

    Travel trav;
    Event event;
    int commandCode;

    Calendar startTime,endTime;
    Location space;
    TextView actText;

    Button startTimeBtn,endTimeBtn,spaceBtn;
    MenuItem revertItem;

    static Location lastLocation = null;
    static String lastAct="자가용 이동 | 렌트카 이동 | 저녁식사 | 장보기";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event);

        ActionBar actionBar = getSupportActionBar();

        Intent receiveIntent = getIntent();
        commandCode=receiveIntent.getIntExtra("command",-1);
        if(commandCode==-1){ Log.e("manageEvent","get wrong commandCode"); commandCode=ADD_EVENT;}
        trav = (commandCode==ADD_EVENT) ? (Travel)receiveIntent.getSerializableExtra("travel") : null;
        event = (commandCode==MANAGE_EVENT) ? (Event)receiveIntent.getSerializableExtra("event") : null;
        actionBar.setTitle((commandCode==ADD_EVENT) ? "이벤트 추가" : "이벤트 수정");

        startTimeBtn = (Button)findViewById(R.id.startTimeBtn);
        endTimeBtn = (Button)findViewById(R.id.endTimeBtn);
        spaceBtn = (Button)findViewById(R.id.spaceBtn);
        actText = (TextView)findViewById(R.id.actText);

        initData();
        displayData();
    }

    private void initData(){
        switch (commandCode){
            case ADD_EVENT:
                startTime = trav.getLastTime();
                endTime = (Calendar)startTime.clone();
                endTime.add(Calendar.HOUR_OF_DAY,1);
                space = null;
                actText.setText(lastAct);
                break;
            case MANAGE_EVENT:
                startTime = event.getStartTime();
                endTime = event.getEndTime();
                space = event.getSpace();
                actText.setText(event.getAction());
                break;
        }
    }

    private static String convertTimeToString(Calendar c){
        return c.get(Calendar.YEAR) + "/" +
                (c.get(Calendar.MONTH)+1) + "/" +
                c.get(Calendar.DAY_OF_MONTH) + " " +
                Event.getTimeString(c);
    }

    private void displayData(){
        TextView start=findViewById(R.id.startTimeText),
                end=findViewById(R.id.endTimeText),
                act=findViewById(R.id.actText);
        Button spaceBtn = findViewById(R.id.spaceBtn);

        start.setText(convertTimeToString(startTime));
        end.setText(convertTimeToString(endTime));
        spaceBtn.setText("장소 : " + ((space!=null) ? "지정" : "미지정"));
    }

    private Travel getAddResult(){
        Event newbie = makeEvent();
        boolean result = trav.addEvent(newbie);
        if(result==false){
            return null;
        }
        return trav;
    }

    private Event makeEvent(){
        return new Event(startTime, endTime,space, actText.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_menu, menu);
        if(commandCode==MANAGE_EVENT) {
            MenuItem removeItem = menu.findItem(R.id.action_remove);
            removeItem.setVisible(true);
        }
        revertItem = menu.findItem(R.id.action_revert);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch(item.getItemId()){
            case R.id.action_finish:
                lastAct = actText.getText().toString();
                lastLocation = space;
                intent.putExtra("event",makeEvent());
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.action_remove:
                finish();
                break;
            case R.id.action_revert:
                intent.putExtra("event",event);
                setResult(RESULT_OK,intent);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000)
            Toast.makeText(getApplicationContext(),"현재 수정 사항은 없어집니다. 종료하시려면 다시 눌러주세요.",Toast.LENGTH_SHORT).show();
        else
            onOptionsItemSelected(revertItem);
        backKeyPressedTime=System.currentTimeMillis();
    }

    private Calendar control;
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        control.set(Calendar.HOUR_OF_DAY,i);
        control.set(Calendar.MINUTE,i1);
        control.set(Calendar.SECOND,0);
        if (control == startTime){
            if(endTime.compareTo(startTime)<=0){
                endTime = (Calendar)startTime.clone();
                endTime.add(Calendar.HOUR_OF_DAY,2);
            }
        }
        displayData();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        control.set(i,i1,i2);
        displayData();
        TimePickerDialog timePickerDialog = new TimePickerDialog(manageEvent.this,
                this,control.get(Calendar.HOUR_OF_DAY),control.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view==startTimeBtn || view==endTimeBtn){
            control = (view==startTimeBtn) ? startTime : endTime;
            DatePickerDialog datePickerDialog = new DatePickerDialog(manageEvent.this,
                    this,control.get(Calendar.YEAR),control.get(Calendar.MONTH),control.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        }
        else if(view==spaceBtn){
            //TODO 지도 연결하기
        }
    }
}
