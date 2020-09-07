package com.example.cse_competition2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
/*
2020.8.20 영훈
-게임 선택 버튼을 누르면 게임선택 activity로 이동
-기록 선택 버튼을 누르면 기록 activity로 이동
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(getApplicationContext(), "This is MainActivity", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View V){ //버튼 클릭에 대한 이벤트 처리
        switch(V.getId()){
            case R.id.select_button: //게임 선택 버튼의 경우 게임 선택 activity로 이동
                Intent select = new Intent(V.getContext(),GameSelectActivity.class);
                startActivity(select);
                break;
            case R.id.record_button: //기록 선택 버튼의 경우 기록 activity로 이동
                Intent record = new Intent(V.getContext(),RecordActivity.class);
                startActivity(record);
                break;
        }
    }

    //핸드폰 back버튼을 누르면 StartActivity로 이동
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(back);
    }

}