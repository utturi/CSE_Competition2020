package com.example.cse_competition2020.game2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cse_competition2020.GameSelectActivity;
import com.example.cse_competition2020.R;

public class Game2ResultActivity extends AppCompatActivity {
    int check_num = 0; //몇개 정답인지 갯수 파악
    String []result; //아이가 말한 것을 저장한 배열, 수신된 값
    String []select; //랜덤으로 나온 그림에 대한 정답, 수신된 값
    String yes = ""; //정답 -> '+'로 문자열 연결했음
    String no= ""; //오답
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2_result);
        //Toast.makeText(getApplicationContext(), "This is Game2ResultActivity", Toast.LENGTH_LONG).show();
        Intent intent = getIntent(); //gmae2Activity에서 결과값 수신
        result = intent.getExtras().getStringArray("result"); //아이가 말한 것을 저장한 배열
        //Toast.makeText(getApplicationContext(), result[0] + " " + result[1] + " " + result[2] + " " + result[3] + " " + result[4], Toast.LENGTH_LONG).show();
        select = intent.getExtras().getStringArray("select"); //랜덤으로 나온 그림에 대한 정답
        //Toast.makeText(getApplicationContext(), select[0] + " " + select[1] + " " + select[2] + " " + select[3] + " " + select[4], Toast.LENGTH_LONG).show();

        //정답과 비교해서 어느게 맞았고 어느게 틀렷는지 체크
        for(int i=0; i<5; i++){
            if(result[i] != null && result[i].equals(select[i])) {
                yes += select[i];
                yes += " ";
                check_num++;
            }
            else {
                no += select[i]; //틀릴 경우 0을 저장
                no += " ";
            }
        }
    }

    public void onClick(View V){ //버튼 클릭에 대한 이벤트 처리
        switch(V.getId()){
            case R.id.gmae2_result_explain: //<결과보기> 버튼 눌렀을 경우
                //다이얼로그를 띄어서 결과를 알려줌
                AlertDialog.Builder result_dlg = new AlertDialog.Builder(Game2ResultActivity.this);
                result_dlg.setTitle("게임 결과"); //제목
                result_dlg.setMessage("총 5문제 중에서 " + check_num + "개를 맞추었습니다.\n맞은 문제 : " + yes + "\n틀린 문제 : " + no); //메시지
                result_dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                result_dlg.show();
                break;
            case R.id.gmae2_back_button: //되돌아가기 버튼 -> GameSelectActivity로 넘어감
                Intent record = new Intent(V.getContext(), GameSelectActivity.class);
                startActivity(record);
                break;
        }
    }

    @Override //사용자가 되돌아가기 버튼이외에 핸드폰 back버튼을 눌렀을때 GameSelectActivity로 넘어감
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), GameSelectActivity.class);
        startActivity(back);
    }
}