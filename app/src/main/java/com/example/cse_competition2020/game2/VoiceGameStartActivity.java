package com.example.cse_competition2020.game2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse_competition2020.GameResultActivity;
import com.example.cse_competition2020.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VoiceGameStartActivity extends AppCompatActivity {
    public static final int data[]={ //이미지 20개
            R.drawable.banana, R.drawable.bus, R.drawable.candy, R.drawable.car, R.drawable.cheese,
            R.drawable.chick, R.drawable.clock, R.drawable.cucumber, R.drawable.dog, R.drawable.elephant,
            R.drawable.frog, R.drawable.glasses, R.drawable.grape, R.drawable.lion, R.drawable.pants,
            R.drawable.rabbit, R.drawable.squirrel, R.drawable.tiger, R.drawable.tomato, R.drawable.umbrella
    };
    public static final String name[]={ //이미지에 대한 답
            "바나나", "버스", "사탕", "자동차", "치즈",
            "병아리", "시계", "오이", "강아지", "코끼리",
            "개구리", "안경", "포도", "사자", "바지",
            "토끼", "다람쥐", "호랑이", "토마토", "우산"
    };

    String user_id;
    String user_name; //이름+년도
    String []result = new String[5]; //결과에 대한 값을 Game2ResultActivity에 전달
    String []select = new String[5]; //랜덤으로 선택된 값이 무엇인지 -> Game2ResultActivity에 전달, result배열과 비교할 때 사용
    String yes = ""; //정답 목록
    String no= ""; //오답 목록
    String gameResult=""; //결과 엑티비티에 보낼 메세지
    int check_num=0; //정답 개수
    int select_num = 0; //select배열의 어느 위치인지 나타내는 변수

    //여기까지 음성인식에 대한 변수
    private SpeechRecognizer mRecognizer;
    Intent SttIntent;

    int []used_data = new int[21]; //중복되는 이미지 안나오게하기위해 체크 배열 선언
    int index = (int)(Math.random()*20+1); //랜덤 값 생성 1~20
    int count = 1; //문제를 풀때마다 증가, 1/5에서 1부분을 나타냄(현재 까지의 문제 수)
    private ImageView change_image; //이미지
    private TextView change_count; //현재 5개중 몇개 까지 진행하였는지
    private TextView change_answer; //이미지에 대한 정답
    int tmp; //랜덤 값 잠시 저장하는 임시 변수

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_game_start);
        //Toast.makeText(getApplicationContext(), "This is Game2Activity", Toast.LENGTH_LONG).show();
        Intent intent = getIntent(); //StartActivity에서 id가 넘어옴
        user_id = intent.getExtras().getString("id");
        user_name = intent.getExtras().getString("user_name");

        //음성권한을 허용 안했으면 앱을 재실행 해야함.
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(),"음성 권한이 필요합니다!\n 앱을 재실행 해주세요!",Toast.LENGTH_SHORT).show();
            return;
        }

        //여기부터
        SttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR"); //한국어 사용
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);
        //여기까지 음성인식 설정

        change_count = findViewById(R.id.game2count); //실행화면 상단에 1/5로 5게임중 얼마나 진행했는지 알려주는 text
        change_answer = findViewById(R.id.game2answer); //이미지에 대한 정답 text
        change_image = (ImageView)findViewById(R.id.game2iamge_view); //이미지
        change_answer.setText("< " + count + " / 5 >"); //현재 진행된 문제수 text 업데이트
        count++; //문제 수 증가
        change_answer.bringToFront(); //text를 최상 위(위치)로 올리기위한 함수, 그림보다 위에 있어야함
        change_image.setImageResource(data[index-1]); //랜덤 그림 업데이트
        change_answer.setText(name[index-1]); //랜덤 그림에 대한 답 업데이트
        select[select_num++] = name[index-1]; //나타나는 그림에 대한 정답을 넣어둠 -> 나중에 정답 체크할때 사용
        used_data[index] = 99; //해당 이미지는 사용을 했으므로 체크를함
        mRecognizer.startListening(SttIntent); //음성인식 사용할라면 이줄 그대로 쓰면됨
    }

    //음성인식 데이터 설정 값
    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {
            //Toast.makeText(game2Activity.this, "지금부터 말을 해주세요.....", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            //Toast.makeText(game2Activity.this, "다음을 누르면 다음문제로 넘어갑니다...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEndOfSpeech() { //아이가 말을하여 값을 받으면 다음버튼을 누르도록 토스트로 알려줌
            //Toast.makeText(Game2Activity.this, "다음을 누르면 다음문제로 넘어갑니다...", Toast.LENGTH_SHORT).show();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onError(int i) {
            if (count == 6) { //5문제를 진행 후 <다음>버튼을 누르면 2초뒤에 결과창으로 넘어감
                Toast.makeText(getApplicationContext(), "잠시후 결과 창으로 넘어갑니다...", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() { //2초정도 기다린뒤에 결과창으로 넘어가게 하기위해서
                    @Override
                    public void run() { //2초뒤 실행됨
                        //정답을 체크하고 DB에 결과를 넣은 뒤, 메세지를 세팅하고 결과 엑티비티 호출
                        resultProcess();
                        Intent last = new Intent(getApplicationContext(), GameResultActivity.class);
                        gameResult="총 5문제 중에서 "+check_num+"개를 맞추었습니다.\n"+
                                "맞은 문제 : "+yes+"\n"+
                                "틀린 문제 : "+no+"\n";
                        last.putExtra("gameResult", gameResult);
                        last.putExtra("id",user_id);
                        last.putExtra("user_name",user_name);
                        startActivity(last);
                    }
                }, 1000);
            } else {
                while (true) { //겹치지 않는 랜덤값 설정
                    tmp = (int) (Math.random() * 20 + 1);
                    if (used_data[tmp] != 99) {
                        index = tmp - 1;
                        used_data[tmp] = 99;
                        break;
                    }
                }
                //업데이트됨 그림, 정답, 진행 된 문제수를 출력
                change_image.setImageResource(data[index]);
                change_count.setText("< " + count + " / 5 >");
                change_answer.setText(name[index]);
                select[select_num++] = name[index]; //나타나는 그림에 대한 정답을 넣어둠 -> 나중에 정답 체크할때 사용
                count++;
                mRecognizer.startListening(SttIntent); //버튼을 클릭할때마다 음성인식 실행 -> 정답 받기
            }
            //Toast.makeText(Game2Activity.this, "천천히 다시 말해 주세요...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) { //아이가 말한 값을 result배열에 넣는 작업
            List<String> result_arr = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String[] arry = result_arr.toArray(new String[result_arr.size()]);
            //Toast.makeText(game2Activity.this, arry[0], Toast.LENGTH_SHORT).show();
            result[count-2] = arry[0]; //음성인식으로 아이가 말한 것들을 정답란에 넣음 -> 판단은 Game2ResultActivity에서 판단
            if (count == 6) { //5문제를 진행 후 <다음>버튼을 누르면 2초뒤에 결과창으로 넘어감
                Toast.makeText(getApplicationContext(), "잠시후 결과 창으로 넘어갑니다...", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() { //2초정도 기다린뒤에 결과창으로 넘어가게 하기위해서
                    @Override
                    public void run() { //2초뒤 실행됨
                        //정답을 체크하고 DB에 결과를 넣은 뒤, 메세지를 세팅하고 결과 엑티비티 호출
                        resultProcess();
                        Intent last = new Intent(getApplicationContext(), GameResultActivity.class);
                        gameResult="총 5문제 중에서 "+check_num+"개를 맞추었습니다.\n"+
                                "맞은 문제 : "+yes+"\n"+
                                "틀린 문제 : "+no+"\n";
                        last.putExtra("gameResult", gameResult);
                        last.putExtra("id",user_id);
                        startActivity(last);
                    }
                }, 1000);
            } else {
                while (true) { //겹치지 않는 랜덤값 설정
                    tmp = (int) (Math.random() * 20 + 1);
                    if (used_data[tmp] != 99) {
                        index = tmp - 1;
                        used_data[tmp] = 99;
                        break;
                    }
                }
                //업데이트됨 그림, 정답, 진행 된 문제수를 출력
                change_image.setImageResource(data[index]);
                change_count.setText("< " + count + " / 5 >");
                change_answer.setText(name[index]);
                select[select_num++] = name[index]; //나타나는 그림에 대한 정답을 넣어둠 -> 나중에 정답 체크할때 사용
                count++;
                mRecognizer.startListening(SttIntent); //버튼을 클릭할때마다 음성인식 실행 -> 정답 받기
            }
        }

        private void resultProcess(){ //게임 결과를 처리하는 함수
            for(int i=0; i<5; i++){ //정답, 오답, 정답 개수를 처리
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

            //db에 결과를 저장
            com.example.cse_competition2020.db.DBHelper1 helper = new com.example.cse_competition2020.db.DBHelper1(getApplicationContext());
            SQLiteDatabase db = helper.getWritableDatabase();
            Date currentTime = Calendar.getInstance().getTime();
            String date_text = new SimpleDateFormat("yy.MM.dd", Locale.getDefault()).format(currentTime);
            String sql = String.format("INSERT INTO T1 VALUES ('" + user_id + "','" + date_text + "'," + check_num + ");");
            //id, 날짜, 점수를 T1에 저장
            db.execSQL(sql);
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

    @Override
    public void onBackPressed() { //back 버튼 눌러도 이전으로 돌아갈 수 없음
    }
}