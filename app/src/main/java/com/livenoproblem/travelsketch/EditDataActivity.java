package com.livenoproblem.travelsketch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
        getSupportActionBar().setTitle("여행노트 수정");

        //ListDataActivity 인텐트얻기
        Intent receivedIntent = getIntent();

        //아이템 id얻기
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //name얻기
        selectedName = receivedIntent.getStringExtra("name");

        //현재 선택한 이름을 표시
        editable_item.setText(selectedName);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);

        MenuItem removeItem = menu.findItem(R.id.action_remove);
        removeItem.setVisible(true);

        MenuItem saveItem = menu.findItem(R.id.action_save);
        saveItem.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        switch(menuitem.getItemId()){
            case R.id.action_remove:
                mDatabaseHelper.deleteName(selectedID,selectedName);
                editable_item.setText("");
                Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                startActivity(intent);
                toastMessage("리스트삭제");
                finish();
                break;
            case R.id.action_save:
                String item = editable_item.getText().toString();
                if(!item.equals("")){
                    mDatabaseHelper.updateName(item,selectedID,selectedName);

                    Intent intent1 = new Intent(EditDataActivity.this, ListDataActivity.class);
                    startActivity(intent1);
                }else{
                    toastMessage("내용을 입력해주세요");
                }
                finish();
                break;
            default:
                return super.onOptionsItemSelected(menuitem);
        }
        return true;
    }


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
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