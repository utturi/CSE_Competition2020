package com.example.cse_competition2020;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse_competition2020.game1.EyeGameStartActivity;
import com.example.cse_competition2020.game2.VoiceGameStartActivity;
import com.example.cse_competition2020.game3.FaceGameSetActivity;

import java.util.Timer;
import java.util.TimerTask;

public class ChangeActivity extends AppCompatActivity {
    String user_id;
    int gameNum;
    String game3_name="";
    //GameSelectActivity set=new GameSelectActivity();
    //private TimerTask second;
    private TimerTask sec;
    private TextView timer_text;
    private int count;
    Timer timer = new Timer();

    private void count3sec(){ //3초 count
        timer_text = (TextView) findViewById(R.id.change_text);
        count=3;
        sec=new TimerTask() {
            @Override
            public void run() {
                if(count==0){
                    sec.cancel();
                    Intent gameStart = null;
                    Log.d("게임 번호 : ",gameNum+"");
                    switch(gameNum){ //게임번호별 액티비티 실행
                        case 1:
                            Intent intent = getIntent();
                            user_id = intent.getExtras().getString("id"); //user id를 받아서 저장
                            long addr = intent.getLongExtra("subface", 0); //입력받은 사용자 사진을 받음
                            int eye_1[] = intent.getIntArrayExtra("eye_1");
                            int eye_2[] = intent.getIntArrayExtra("eye_2");

                            gameStart = new Intent(getApplicationContext(), EyeGameStartActivity.class);
                            gameStart.putExtra("id",user_id);
                            gameStart.putExtra("subface", addr);
                            gameStart.putExtra("eye_1",eye_1);
                            gameStart.putExtra("eye_2",eye_2);
                            break;
                        case 2:
                            gameStart = new Intent(getApplicationContext(), VoiceGameStartActivity.class);
                            break;
                        case 3:
                            gameStart = new Intent(getApplicationContext(), FaceGameSetActivity.class);
                            gameStart.putExtra("game3_name", game3_name);
                            break;
                    }
                    gameStart.putExtra("id",user_id);
                    /*timer.cancel();
                    second.cancel();*/
                    startActivity(gameStart);
                }
                else{
                    timer_text.setText(count+"");
                    if(count == 1){ //카운트가 1초일때 빨간색 글씨로 바뀜
                        timer_text.setTextColor(Color.RED);
                    }
                    count--;
                }

            }
        };
        Timer timer=new Timer();
        timer.schedule(sec,0,1000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        Intent intent = getIntent(); //StartActivity에서 id가 넘어옴
        user_id = intent.getExtras().getString("id");
        gameNum = intent.getIntExtra("gameNum",0);
        if(gameNum == 3){
            game3_name = intent.getStringExtra("game3_name");
        }
        //Toast.makeText(getApplicationContext(), "This is changeActivity", Toast.LENGTH_LONG).show();
        //testStart(); //위에서 만들어놓은 실시간으로 text값 변경하는 함수 호출
        count3sec();
    }

    @Override
    public void onBackPressed() { //back 버튼 눌러도 이전으로 돌아갈 수 없음
    }
}