package com.example.cse_competition2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cse_competition2020.game1.EyeGameSetActivity;
import com.example.cse_competition2020.game2.VoiceGameActivity;
import com.example.cse_competition2020.game3.FaceGameActivity;

/*
2020.08.18 영훈
각 게임을 누르면 해당 게임 activity로 이동
돌아가기 버튼 누르면 메인 activity로 이동
 */

public class GameSelectActivity extends AppCompatActivity implements View.OnClickListener {
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);
        Intent intent = getIntent(); //MainActivity에서 id가 넘어옴
        user_id = intent.getExtras().getString("id");

        //Toast.makeText(getApplicationContext(), "This is GameSelectActivity", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View V){
        switch(V.getId()){
            case R.id.eyegame_button: //눈 마주치기 게임 실행 (EyeGameSetActivity 실행)
                Intent eye = new Intent(V.getContext(),EyeGameSetActivity.class);
                eye.putExtra("id", user_id);
                startActivity(eye);
                break;
            case R.id.voicegame_button: //또박또박 말하기 게임 실행 (VoiceGameActivity 실행)
                Intent voice = new Intent(V.getContext(), VoiceGameActivity.class);
                voice.putExtra("id", user_id);
                startActivity(voice);
                break;
            case R.id.facegame_button: //표정 따라하기 게임 실행 (FaceGameActivity 실행)
                Intent face = new Intent(V.getContext(),FaceGameActivity.class);
                face.putExtra("id", user_id);
                startActivity(face);
                break;
            case R.id.back_button: //MainActivity 액티비티로 이동
                Intent back = new Intent(V.getContext(), MainActivity.class);
                back.putExtra("id", user_id); //user_id를 다시 main으로 넘겨줌
                startActivity(back);
                break;
        }
    }
    //핸드폰 back버튼을 누르면 MainActivity로 이동
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        back.putExtra("id", user_id); //user_id를 다시 main으로 넘겨줌
        startActivity(back);
    }
}