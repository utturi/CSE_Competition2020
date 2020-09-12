package com.example.cse_competition2020.game3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse_competition2020.GameSelectActivity;
import com.example.cse_competition2020.R;

public class FaceGameSetActivity extends AppCompatActivity {
    String user_id;
    private Button guideButton, startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_game_set);

        Intent intent = getIntent(); // GameSelectActivity에서 id가 넘어옴
        user_id = intent.getExtras().getString("id");

        guideButton = (Button)findViewById(R.id.game3guide_button);
        startButton = (Button)findViewById(R.id.game3start_button);

        // 게임 설명 버튼
        guideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(FaceGameSetActivity.this);
                dlg.setTitle("게임 설명"); //제목
                //메시지(설명란)
                dlg.setMessage("");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.show();
            }
        });

        // 게임 시작 버튼
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent game3 = new Intent(view.getContext(), FaceGameSelectActivity.class);
                game3.putExtra("gameNum",3);
                game3.putExtra("id",user_id);
                startActivity(game3);
            }
        });
    }

    //사용자가 되돌아가기 버튼이외에 핸드폰 back버튼을 눌렀을때 GameSelectActivity로 넘어감
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), GameSelectActivity.class);
        back.putExtra("id", user_id);
        startActivity(back);
    }
}