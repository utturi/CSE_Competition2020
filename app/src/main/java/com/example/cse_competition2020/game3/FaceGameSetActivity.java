package com.example.cse_competition2020.game3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse_competition2020.GameSelectActivity;
import com.example.cse_competition2020.R;

public class FaceGameSetActivity extends AppCompatActivity {
    String user_id;
    String user_name; //이름+년도
    private TextView top_text;
    private Button guideButton, startButton;
    private ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_game_set);

        Intent intent = getIntent(); // GameSelectActivity에서 id가 넘어옴
        user_id = intent.getExtras().getString("id");
        user_name = intent.getExtras().getString("user_name");
        top_text = (TextView)findViewById(R.id.user_name);
        top_text.setText(user_name);
        back_button = (ImageView)findViewById(R.id.back_button);
        guideButton = (Button)findViewById(R.id.game3guide_button);
        startButton = (Button)findViewById(R.id.game3start_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
                game3.putExtra("user_name",user_name);
                startActivity(game3);
            }
        });
    }

    //사용자가 되돌아가기 버튼이외에 핸드폰 back버튼을 눌렀을때 GameSelectActivity로 넘어감
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), GameSelectActivity.class);
        back.putExtra("id", user_id);
        back.putExtra("user_name",user_name);
        startActivity(back);
    }
}