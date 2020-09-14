package com.example.cse_competition2020.game2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse_competition2020.ChangeActivity;
import com.example.cse_competition2020.GameSelectActivity;
import com.example.cse_competition2020.R;

public class VoiceGameSetActivity extends AppCompatActivity {
    String user_id;
    String user_name; //이름+년도
    private TextView top_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_game_set);
        Intent intent = getIntent(); //GameSelectActivity에서 id가 넘어옴
        user_id = intent.getExtras().getString("id");
        user_name = intent.getExtras().getString("user_name");
        top_text = (TextView)findViewById(R.id.user_name);
        top_text.setText(user_name);
        //intent.getStringExtra("id");
        //Toast.makeText(getApplicationContext(), "This is VoiceGameActivity", Toast.LENGTH_LONG).show();
    }

    public void onClick(final View V){ //버튼 이벤트 처리
        switch (V.getId()){
            case R.id.back_button: // back 버튼을 누르면 뒤로 이동
                onBackPressed();
                break;
            case R.id.game2guide_button: //게임2의 <게임 설명> 버튼에 대한 이벤트 처리
                //다이얼로그 띄어서 간단 설명
                AlertDialog.Builder dlg = new AlertDialog.Builder(VoiceGameSetActivity.this);
                dlg.setTitle("게임 설명"); //제목
                //메시지(설명란)
                dlg.setMessage("\n\n o 본 게임은 주어진 단어를 정확하게 읽는 게임입니다" +
                        "\n\n o 총 5문제로 진행되고, 한 문제당 3초의 시간이\n    주어집니다" +
                        "\n\n o 화면은 자동으로 다음 문제로 넘어갑니다");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dlg.show();
                break;
            case R.id.game2start_button: //게임2의 <게임 시작> 버튼에 대한 이벤트 처리
                Intent game2 = new Intent(V.getContext(), ChangeActivity.class);
                game2.putExtra("gameNum",2);
                game2.putExtra("id",user_id);
                game2.putExtra("user_name",user_name);
                startActivity(game2); //changeActivity로 넘어감, 3->2->1로 변화는 과정 출력하는 엑티비티
                break;
        }
    }

    // 사용자가 되돌아가기 버튼이외에 핸드폰 back버튼을 눌렀을때 GameSelectActivity로 넘어감
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), GameSelectActivity.class);
        back.putExtra("id", user_id);
        back.putExtra("user_name",user_name);
        startActivity(back);
    }
}