package com.example.cse_competition2020;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GameResultActivity extends AppCompatActivity {
    String user_id;
    String gameResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        Intent intent = getIntent(); //각 게임에서 결과값 수신
        user_id = intent.getExtras().getString("id");
        gameResult = intent.getExtras().getString("gameResult");
        /*result = intent.getExtras().getStringArray("result"); //아이가 말한 것을 저장한 배열
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
        }*/
    }

    int check=-1;

    public void onClick(View V){ //버튼 클릭에 대한 이벤트 처리
        switch(V.getId()){
            case R.id.gmae_result_explain: //<결과보기> 버튼 눌렀을 경우
                //다이얼로그를 띄어서 결과를 알려줌
                AlertDialog.Builder result_dlg = new AlertDialog.Builder(GameResultActivity.this);
                result_dlg.setTitle("게임 결과"); //제목
                result_dlg.setMessage(gameResult); //메시지
                result_dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        check=100;
                        /*com.example.cse_competition2020.db.DBHelper1 helper = new com.example.cse_competition2020.db.DBHelper1(getApplicationContext());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        Date currentTime = Calendar.getInstance().getTime();
                        String date_text = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentTime);
                        String sql = String.format("INSERT INTO T1 VALUES ('" + user_id + "','" + date_text + "'," + check_num + ");");
                        //id, 날짜, 점수를 T1에 저장
                        db.execSQL(sql);
                        //Toast.makeText(Game2ResultActivity.this,  sql, Toast.LENGTH_SHORT).show();*/
                    }
                });
                result_dlg.show();
                break;
            case R.id.gmae_back_button: //되돌아가기 버튼 -> GameSelectActivity로 넘어감
                /*if(check == -1){ //결과보기 버튼 안누르고 바로 되돌아가기 버튼을 누를경우
                    com.example.cse_competition2020.db.DBHelper1 helper = new com.example.cse_competition2020.db.DBHelper1(getApplicationContext());
                    SQLiteDatabase db = helper.getWritableDatabase();
                    Date currentTime = Calendar.getInstance().getTime();
                    String date_text = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentTime);
                    String sql = String.format("INSERT INTO T1 VALUES ('" + user_id + "','" + date_text + "'," + check_num + ");");
                    //id, 날짜, 점수를 T1에 저장
                    db.execSQL(sql);
                    //Toast.makeText(Game2ResultActivity.this,  sql, Toast.LENGTH_SHORT).show();
                }*/
                Intent record = new Intent(V.getContext(), GameSelectActivity.class);
                record.putExtra("id",user_id);
                startActivity(record);
                break;
        }
    }

    //사용자가 되돌아가기 버튼이외에 핸드폰 back버튼을 눌렀을때 GameSelectActivity로 넘어감
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), GameSelectActivity.class);
        back.putExtra("id",user_id);
        startActivity(back);
    }
}