package com.example.cse_competition2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse_competition2020.record.RecordActivity;

/*
2020.8.20 영훈
-게임 선택 버튼을 누르면 게임선택 activity로 이동
-기록 선택 버튼을 누르면 기록 activity로 이동
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(getApplicationContext(), "This is MainActivity", Toast.LENGTH_LONG).show();
        Intent intent = getIntent(); //StartActivity에서 id가 넘어옴
        user_id = intent.getExtras().getString("id");
        //Toast.makeText(getApplicationContext(), "" + user_id, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View V){ //버튼 클릭에 대한 이벤트 처리
        switch(V.getId()){
            case R.id.back_button: // back 버튼을 누르면 뒤로 이동
                onBackPressed();
                break;
            case R.id.select_button: //게임 선택 버튼의 경우 게임 선택 activity로 이동
                Intent select = new Intent(V.getContext(),GameSelectActivity.class);
                select.putExtra("id", user_id);
                startActivity(select);
                break;
            case R.id.record_button: //기록 선택 버튼의 경우 기록 activity로 이동
                Intent record = new Intent(V.getContext(), RecordActivity.class);
                record.putExtra("id", user_id);
                startActivity(record);
                break;
        }
    }

    // Back버튼을 누르면 StartActivity로 이동
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(back);
    }
}