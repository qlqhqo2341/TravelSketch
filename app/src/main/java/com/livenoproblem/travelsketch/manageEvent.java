package com.livenoproblem.travelsketch;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.livenoproblem.travelsketch.Struct.Event;
import com.livenoproblem.travelsketch.Struct.Travel;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class manageEvent extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    static final int ADD_EVENT=1,MANAGE_EVENT=2;

    Travel trav;
    Event event;
    int commandCode;

    Calendar startTime,endTime;
    Location space;
    String act;

    Button startTimeBtn,endTimeBtn,spaceBtn,deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event);

        ActionBar actionBar = getActionBar();

        Intent receiveIntent = getIntent();
        commandCode=receiveIntent.getIntExtra("command",-1);
        if(commandCode==-1){ Log.e("manageEvent","get wrong commandCode"); commandCode=ADD_EVENT;}
        trav = (Travel)receiveIntent.getSerializableExtra("travel");
        event = (commandCode==MANAGE_EVENT) ? (Event)receiveIntent.getSerializableExtra("event") : null;

        startTimeBtn = (Button)findViewById(R.id.startTimeBtn);
        endTimeBtn = (Button)findViewById(R.id.endTimeBtn);
        spaceBtn = (Button)findViewById(R.id.spaceBtn);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        deleteBtn.setVisibility((commandCode==ADD_EVENT) ? View.VISIBLE : View.INVISIBLE);

        initData();
        displayData();

        Toast.makeText(getApplicationContext(),"작성이 완료되면 뒤로가기를 눌러주세요.",Toast.LENGTH_SHORT).show();
    }

    private void initData(){
        switch (commandCode){
            case ADD_EVENT:
                startTime = trav.getContinuousLastTime();
                endTime = new GregorianCalendar(startTime.get(Calendar.YEAR),
                        startTime.get(Calendar.MONTH),
                        startTime.get(Calendar.DAY_OF_MONTH),
                        startTime.get(Calendar.HOUR_OF_DAY)+1,
                        startTime.get(Calendar.MINUTE),
                        startTime.get(Calendar.SECOND));
                space = null;
                act="자가용 이동 | 렌트카 이동 | 저녁식사 | 장보기";
                break;
            case MANAGE_EVENT:
                startTime = event.getStartTime();
                endTime = event.getEndTime();
                space = event.getSpace();
                act = event.getAction();
                break;
        }
    }

    private String convertTimeToString(Calendar c){
        return c.get(Calendar.YEAR) + "/" +
                c.get(Calendar.MONTH) + "/" +
                c.get(Calendar.DAY_OF_MONTH) + " " +
                c.get(Calendar.HOUR) + ":"+
                c.get(Calendar.MINUTE);
    }

    private void displayData(){
        TextView start=findViewById(R.id.startTimeText),
                end=findViewById(R.id.endTimeText),
                act=findViewById(R.id.actText);
        Button spaceBtn = findViewById(R.id.spaceBtn);

        start.setText(convertTimeToString(startTime));
        end.setText(convertTimeToString(endTime));
        act.setText(this.act);
        spaceBtn.setText("장소 : " + ((space!=null) ? "지정" : "미지정"));
    }

    private Travel getResult(){
        Event newbie = new Event(startTime,endTime,space,act);

        if(commandCode==MANAGE_EVENT) trav.removeEvent(event);
        boolean result = trav.addEvent(newbie);
        if(result==false){
            if(commandCode==MANAGE_EVENT) trav.addEvent(event);
            return trav; //TODO 에러표시
        }
        return trav;
    }

    @Override
    public void onBackPressed() {
        //TODO 데이터 체크후 토스트 띄우고 돌아가자!
        Intent intent = new Intent();
        intent.putExtra("travel",getResult());
        setResult(RESULT_OK,intent);
        super.onBackPressed();
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
    }

    @Override
    public void onClick(View view) {
        if(view==startTimeBtn || view==endTimeBtn){
            control = (view==startTimeBtn) ? startTime : endTime;
            DatePickerDialog datePickerDialog = new DatePickerDialog(manageEvent.this,
                    this,control.get(Calendar.YEAR),control.get(Calendar.MONTH),control.get(Calendar.DAY_OF_MONTH));
            TimePickerDialog timePickerDialog = new TimePickerDialog(manageEvent.this,
                    this,control.get(Calendar.HOUR_OF_DAY),control.get(Calendar.MINUTE),false);
            timePickerDialog.show();
            datePickerDialog.show();
        }
        else if(view==spaceBtn){

        }
        else if(view==deleteBtn){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
