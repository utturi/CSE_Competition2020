package com.example.cse_competition2020.game2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.cse_competition2020.R;

import java.util.Timer;
import java.util.TimerTask;

public class ChangeActivity extends AppCompatActivity {
    String user_id;
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
                    Intent game2 = new Intent(getApplicationContext(), Game2Activity.class);
                    game2.putExtra("id",user_id);
                    startActivity(game2);
                }
                else timer_text.setText(timer_sec + "초");
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

        //Toast.makeText(getApplicationContext(), "This is changeActivity", Toast.LENGTH_LONG).show();
        testStart(); //위에서 만들어놓은 실시간으로 text값 변경하는 함수 호출
    }
}