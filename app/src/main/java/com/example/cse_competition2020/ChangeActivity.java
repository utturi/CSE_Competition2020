package com.example.cse_competition2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.example.cse_competition2020.GameSelectActivity;
import com.example.cse_competition2020.R;
import com.example.cse_competition2020.game1.EyeGameSetActivity;
import com.example.cse_competition2020.game1.EyeGameStartActivity;
import com.example.cse_competition2020.game2.Game2Activity;
import com.example.cse_competition2020.game3.FaceGameActivity;

import java.util.Timer;
import java.util.TimerTask;

public class ChangeActivity extends AppCompatActivity {
    String user_id;
    int gameNum;
    GameSelectActivity set=new GameSelectActivity();
    private TimerTask second;
    private TextView timer_text;
    private final Handler handler = new Handler(); //핸들러를 통해 초마다 text값 변경 3 -> 2 -> 1
    int timer_sec;
    int count;
    public void testStart() { //게임 시작전 3 -> 2 -> 1로 버퍼링? 화면을 띄어주기 위해 함수를 설정
        timer_text = (TextView) findViewById(R.id.change_text);
        timer_sec = 4;
        count = 0;
        second = new TimerTask() {
            @Override
            public void run() {
                //Log.i("Test", "Timer start");
                Update();
                timer_sec--;
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);
    }
    protected void Update() { //실시간으로 초마다 바뀌는 화면 3 -> 2 -> 1
        Runnable updater = new Runnable() {
            public void run() {
                if(timer_sec == 0){ //0초가 되면 game2Activity로 넘어감
                    Intent gameStart=null;
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
                            gameStart = new Intent(getApplicationContext(), Game2Activity.class);
                            break;
                        case 3:
                            gameStart = new Intent(getApplicationContext(), FaceGameActivity.class);
                            break;
                    }
                    gameStart.putExtra("id",user_id);
                    startActivity(gameStart);
                }
                else timer_text.setText(timer_sec+"");
            }
        };
        handler.post(updater);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        Intent intent = getIntent(); //StartActivity에서 id가 넘어옴
        user_id = intent.getExtras().getString("id");
        gameNum = intent.getIntExtra("gameNum",0);
        //Toast.makeText(getApplicationContext(), "This is changeActivity", Toast.LENGTH_LONG).show();
        testStart(); //위에서 만들어놓은 실시간으로 text값 변경하는 함수 호출
    }
    @Override
    public void onBackPressed() { //back 버튼 눌러도 이전으로 돌아갈 수 없음
    }
}