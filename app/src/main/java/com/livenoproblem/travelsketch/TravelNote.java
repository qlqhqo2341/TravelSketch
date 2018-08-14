package com.livenoproblem.travelsketch;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.regex.Pattern;

public class TravelNote extends AppCompatActivity {

    DatabaseHelper myDB;
    Button btnAdd,btnView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_write);
        editText = (EditText) findViewById(R.id.editText);
        myDB = new DatabaseHelper(this);

        // 플로팅 액션 버튼

        FloatingActionButton fab0 = findViewById(R.id.fab_action0);
        fab0.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                String newEntry = editText.getText().toString();
                if(editText.length()!= 0){
                    AddData(newEntry);
                    editText.setText("");
                    Intent intent = new Intent(TravelNote.this, ViewListContents.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(TravelNote.this, "텍스트를 입력하지 않았습니다.", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    public void AddData(String newEntry) {

        boolean insertData = myDB.addData(newEntry);

        if(insertData==true){
            Toast.makeText(this, "저장성공", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "저장과정에서 문제가 발생했습니다.", Toast.LENGTH_LONG).show();
        }
    }
}