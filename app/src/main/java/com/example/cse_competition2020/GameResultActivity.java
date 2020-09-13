package com.example.cse_competition2020;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GameResultActivity extends AppCompatActivity {
    String user_id;
    String user_name; //이름+년도
    private TextView top_text;
    String gameResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        Intent intent = getIntent(); //각 게임에서 결과값 수신
        user_id = intent.getExtras().getString("id");
        user_name = intent.getExtras().getString("user_name");
        top_text = (TextView)findViewById(R.id.user_name);
        top_text.setText(user_name);

        gameResult = intent.getExtras().getString("gameResult");

        if(gameResult.contains("%")){ //내용안에 %, 확률에 대한 정보가 들어가면 gameResult를 바꿈
            String []array = gameResult.split(" ");
            gameResult = ""; //초기화
            for(int i=0; i<array.length; i++){
                if(i % 2 == 0){
                    array[i] = array[i].substring(1);
                    if(array[i].equals("Happy")){
                        gameResult += "기쁨 : ";
                    }
                    else if(array[i].equals("Sad")){
                        gameResult += "슬픔 : ";
                    }
                    else if(array[i].equals("Surprise")){
                        gameResult += "놀람 : ";
                    }
                    else{
                        gameResult += "화남 : ";
                    }
                }
                else{
                    array[i] = array[i].substring(1, array[i].length()-3);
                    gameResult += array[i];
                    gameResult += "%\n";
                }
            }
        }
    }

    int check=-1;

    public void onClick(View V){ //버튼 클릭에 대한 이벤트 처리
        switch(V.getId()){
            case R.id.game_result_explain: //<결과보기> 버튼 눌렀을 경우
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
            case R.id.game_back_button: //되돌아가기 버튼 -> GameSelectActivity로 넘어감
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
                record.putExtra("user_name",user_name);
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
        back.putExtra("user_name",user_name);
        startActivity(back);
    }
}