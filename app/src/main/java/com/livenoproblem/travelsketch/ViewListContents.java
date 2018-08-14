package com.livenoproblem.travelsketch;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.livenoproblem.travelsketch.Struct.Travel;

import java.util.ArrayList;
import java.util.List;

public class ViewListContents extends AppCompatActivity {

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        ListView listView = (ListView) findViewById(R.id.listView);
        myDB = new DatabaseHelper(this);

        // 플로팅 액션 버튼

        //리스트 작성 레이아웃으로
        FloatingActionButton fab0 = findViewById(R.id.fab_action0);
        fab0.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), TravelNote.class);
                startActivity(intent);
            }
        });
        //메인화면으로
        FloatingActionButton fab1 = findViewById(R.id.fab_action1);
        fab1.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });


        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        if(data.getCount() == 0){
            Toast.makeText(this, "생성된 리스트가 없습니다.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), TravelNote.class);
            startActivity(intent);
        }else{
            while(data.moveToNext()){
                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);
            }
        }


    }
}