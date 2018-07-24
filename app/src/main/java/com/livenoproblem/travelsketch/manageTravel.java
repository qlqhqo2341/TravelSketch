package com.livenoproblem.travelsketch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class manageTravel extends Activity {
    private boolean scrollMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_travel);
        initGrid();
        Switch sw = (Switch)findViewById(R.id.scrollSwitch);
        scrollMode = sw.isChecked();
        sw.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                scrollMode=b;
                //TODO Change Scollview settings
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initGrid(){
        LinearLayout lines[] = new  LinearLayout[]{(LinearLayout)findViewById(R.id.timeLine),
            (LinearLayout)findViewById(R.id.spaceLine),
            (LinearLayout)findViewById(R.id.actLine)};

        for(LinearLayout l : lines){
            for(int i=0;i<24;i++){
                TextView next = new TextView(getApplicationContext());
                next.setText(String.format("%02d:%02d",i,0));
                next.setOnTouchListener(new View.OnTouchListener(){
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if(scrollMode==true)
                            return true;
                        TextView tv = (TextView)view;
                        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                            tv.setText(tv.getText()+"D");
                        else if(motionEvent.getAction()==motionEvent.ACTION_UP)
                            tv.setText(tv.getText()+"U");
                        else if(motionEvent.getAction()==motionEvent.ACTION_MOVE)
                            tv.setText(tv.getText()+"M");
                        return true;
                    }
                });
                l.addView(next);
            }

//            l.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(manageTravel.this, String.format("%s",view.getClass().toString()), Toast.LENGTH_SHORT).show();
//                }
//            });

        }
    }


}
