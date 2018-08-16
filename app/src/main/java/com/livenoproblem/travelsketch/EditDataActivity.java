package com.livenoproblem.travelsketch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;


public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "리스트수정";


    private EditText editable_item;

    DatabaseHelper mDatabaseHelper;

    private String selectedName;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);
        editable_item = (EditText) findViewById(R.id.editable_item);
        mDatabaseHelper = new DatabaseHelper(this);

        //ListDataActivity 인텐트얻기
        Intent receivedIntent = getIntent();

        //아이템 id얻기
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //name얻기
        selectedName = receivedIntent.getStringExtra("name");

        //현재 선택한 이름을 표시
        editable_item.setText(selectedName);


        // 플로팅 액션 버튼
        FloatingActionButton fab0 = findViewById(R.id.fab_action0);
        fab0.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                String item = editable_item.getText().toString();
                if(!item.equals("")){
                    mDatabaseHelper.updateName(item,selectedID,selectedName);

                    Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                    startActivity(intent);
                }else{
                    toastMessage("내용을 입력해주세요");
                }
            }
        });

        FloatingActionButton fab1 = findViewById(R.id.fab_action1);
        fab1.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                mDatabaseHelper.deleteName(selectedID,selectedName);
                editable_item.setText("");
                Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                startActivity(intent);
                toastMessage("리스트삭제");
            }

        });

    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}