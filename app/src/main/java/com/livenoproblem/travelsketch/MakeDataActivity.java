package com.livenoproblem.travelsketch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class MakeDataActivity extends AppCompatActivity {

    private static final String TAG = "리스트작성";

    DatabaseHelper mDatabaseHelper;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_write);
        editText = (EditText) findViewById(R.id.editText);
        mDatabaseHelper = new DatabaseHelper(this);

        // 플로팅 액션 버튼
        FloatingActionButton fab0 = findViewById(R.id.fab_action0);
        fab0.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                String newEntry = editText.getText().toString();
                if (editText.length() != 0) {
                    AddData(newEntry);
                    editText.setText("");
                    Intent intent = new Intent(MakeDataActivity.this, ListDataActivity.class);
                    startActivity(intent);
                } else {
                    toastMessage("내용을 입력해주세요.");
                }


            }
        });

    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("저장성공");
        } else {
            toastMessage("저장실패");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}