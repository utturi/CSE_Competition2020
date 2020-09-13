package com.example.cse_competition2020.game3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse_competition2020.MainActivity;
import com.example.cse_competition2020.R;

public class FaceGameSelectActivity extends AppCompatActivity {
    private Button happy, sad, surprised, angry;
    private ImageView back_button;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_game_select);

        Intent intent = getIntent(); // FaceGameSetActivity에서 user_id & gameNum 이 넘어옴
        user_id = intent.getExtras().getString("id");

        back_button = (ImageView) findViewById(R.id.back_button);
        happy = (Button)findViewById(R.id.happy_button);
        sad = (Button)findViewById(R.id.sad_button);
        surprised = (Button)findViewById(R.id.surprised_button);
        angry= (Button)findViewById(R.id.angry_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FaceGameStartActivity.class);
                intent.putExtra("gameNum",3); // Game3에 대한 정보
                intent.putExtra("game3_name", "happy"); // happy_game
                intent.putExtra("id",user_id);
                startActivity(intent);
            }
        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FaceGameStartActivity.class);
                intent.putExtra("gameNum",3);
                intent.putExtra("game3_name", "sad"); // sad_game
                intent.putExtra("id",user_id);
                startActivity(intent);
            }
        });

        surprised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FaceGameStartActivity.class);
                intent.putExtra("gameNum",3);
                intent.putExtra("game3_name", "surprised"); // surprised_game
                intent.putExtra("id",user_id);
                startActivity(intent);
            }
        });

        angry.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FaceGameStartActivity.class);
                intent.putExtra("gameNum",3);
                intent.putExtra("game3_name", "angry"); // angry_game
                intent.putExtra("id",user_id);
                startActivity(intent);
            }
        });
    }

    // Back 버튼 누르면 이전 엑티비티로 이동
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), FaceGameSetActivity.class);
        back.putExtra("id", user_id); //user_id를 다시 main으로 넘겨줌
        back.putExtra("gameNum", 3);
        startActivity(back);
    }
}
