package com.livenoproblem.travelsketch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.livenoproblem.travelsketch.Struct.Event;
import com.livenoproblem.travelsketch.Struct.Travel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;

public class manageTravel extends AppCompatActivity implements View.OnClickListener{
    float dpFactor;
    HashMap<LinearLayout,Event> LayoutToEvent;

    Travel trav;
    Event managing=null;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_travel);
        container = (LinearLayout)findViewById(R.id.eventList);

        com.getbase.floatingactionbutton.FloatingActionButton fab1 = findViewById(R.id.fab_action1);
        fab1.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                //캡처
                Bitmap captureView = loadBitmapFromView(container);
                FileOutputStream fos;
                try{
                    String str = Environment.getExternalStorageDirectory().toString();
                    fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/capture.jpeg");
                    captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    Toast.makeText(getApplicationContext(), "캡쳐성공!", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "캡쳐실패", Toast.LENGTH_LONG).show();
                }

                //전송부분
                //Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString()+"/capture.jpeg"));
                Uri uri = FileProvider.getUriForFile(getApplicationContext(),"com.livenoproblem.travelsketch.fileprovider", new File(Environment.getExternalStorageDirectory().toString()+"/capture.jpeg"));
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "TravelSketch";
                String ShareSub = "일정공유";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody); //주제
                myIntent.putExtra(Intent.EXTRA_TEXT,ShareSub); //제목
                myIntent.putExtra(Intent.EXTRA_STREAM, uri);
                myIntent.setType("image/*");
                startActivity(Intent.createChooser(myIntent, "일정공유"));
                //카카오톡에서는 이미지만 전송됨

            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("일정 관리");

        //get dpFactor
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dpFactor=displayMetrics.density;
        Log.i("TEST","dpFactor : " + dpFactor);

        ((com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.addEventBtn)).setOnClickListener(this);

        Intent receive = getIntent();
        trav = (Travel) receive.getSerializableExtra("travel");
        LayoutToEvent = new HashMap<LinearLayout, Event>();
        initGrid();



    }

    public static Bitmap loadBitmapFromView(View v)
    {

        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Drawable bgDrawable = v.getBackground();
        if(bgDrawable!=null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);
        v.draw(c);
        return b;
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
        LayoutParams textp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textp.setMargins(margin,margin,margin,margin);

        if(events.length==0){
            TextView textView = new TextView(getApplicationContext());
            textView.setText("이벤트를 추가해주세요.");
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            eventList.addView(textView);
            return;
        }

        for(Event e : events){
            Calendar date = e.getStartTime();
            if(prevDate == null || Event.compareDate(prevDate,date)!=0) {
                TextView dateText = new TextView(getApplicationContext());
                String text = Event.getDateString(date);
                dateText.setText(text);
                dateText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                dateText.setTextColor(Color.DKGRAY);
                dateText.setTypeface(null, Typeface.ITALIC);
                eventList.addView(dateText,p);
            }
            prevDate = date;

            LinearLayout eventLayout = new LinearLayout(getApplicationContext());
            eventLayout.setOrientation(LinearLayout.HORIZONTAL);
            eventLayout.setOnClickListener(this);

            LayoutToEvent.put(eventLayout,e);

            TextView timeText,spaceText,actText;
            timeText = new TextView(getApplicationContext());
            spaceText = new TextView(getApplicationContext());
            actText = new TextView(getApplicationContext());

            timeText.setText(e.getStartTimeString() + " ~ \n" + e.getEndTimeString());
            String spaceStr = "장소 : " + ( (e.getSpace()!=null) ? e.getSpace().description : "미지정");
            actText.setText(spaceStr + "\n" + e.getAction());

            for(TextView textView : new TextView[]{timeText,spaceText,actText}) {
                textView.setTextColor(Color.BLACK);
                eventLayout.addView(textView,textp);
            }
            eventList.addView(eventLayout,p);
        }

        eventList.addView(new TextView(getApplicationContext()),
                new LayoutParams(LayoutParams.MATCH_PARENT,(int)(60*dpFactor)));
    }

    @Override
    public void onClick(View view) {
        Location prevLocation = new Location("");
        if(view==findViewById(R.id.addEventBtn)) {
            Intent intent = new Intent(getApplicationContext(), manageEvent.class);
            intent.putExtra("command",manageEvent.ADD_EVENT);
            intent.putExtra("travel", trav);

            startActivityForResult(intent, manageEvent.ADD_EVENT);
        }
        if(view.getClass() == LinearLayout.class){
            Intent intent = new Intent(getApplicationContext(), manageEvent.class);

            Event e = LayoutToEvent.get(view);
            managing=e;
            intent.putExtra("command",manageEvent.MANAGE_EVENT);
            intent.putExtra("event",e);
            trav.removeEvent(e);
            intent.putExtra("travel",trav);
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

    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000)
            Toast.makeText(getApplicationContext(),"현재 수정 사항은 없어집니다. 종료하려면 다시 눌러주세요.",Toast.LENGTH_SHORT).show();
        else
            super.onBackPressed();
        backKeyPressedTime=System.currentTimeMillis();
    }
}
