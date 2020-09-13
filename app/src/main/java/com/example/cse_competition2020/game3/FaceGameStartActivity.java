package com.example.cse_competition2020.game3;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse_competition2020.GameResultActivity;
import com.example.cse_competition2020.R;
import com.example.cse_competition2020.db.DBHelper3;
import com.example.cse_competition2020.ect.Classifier;
import com.example.cse_competition2020.ect.ImageClassifier;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FaceGameStartActivity extends AppCompatActivity {
    private static final String TAG = "FaceGameStartActivity";
    private static final String MODEL_PATH = "model.tflite"; // 학습한 데이터 파일
    private static final boolean QUANT = true;
    private static final String LABEL_PATH = "labels.txt"; // 학습한 데이터 파일들의 목록
    private static final int INPUT_SIZE = 224; // 이미지 크기
    private CameraView cameraKitView;
    Classifier classifier;
    //Bitmap bitmap; // 사진 정보
    String gameResult = ""; // 게임 결과에 대한 정보
    private ImageView imageView;
    String what_game;
    private Executor executor = Executors.newSingleThreadExecutor();

    String user_id;
    private String game3_name;
    private int count; // 자동으로 사진 찍는데 필요한 시간 변수
    private TimerTask sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_game_start);

        // FaceGameSelectActivity에서 user_id & gameNum & game3_name 이 넘어옴
        Intent intent = getIntent();
        user_id = intent.getExtras().getString("id");
        game3_name = intent.getExtras().getString("game3_name");

        cameraKitView = findViewById(R.id.cameraView);
        imageView = (ImageView)findViewById(R.id.imageView);

        cameraKitView.toggleFacing(); // 전면 카메라로 전환

        // game3에서 표정선택에 따른 이미지가 등록
        switch(game3_name) {
            case "happy":
                imageView.setImageResource(R.drawable.happy);
                what_game = "Happy";
                break;
            case "sad":
                imageView.setImageResource(R.drawable.sad);
                what_game = "Sad";
                break;
            case "surprise":
                imageView.setImageResource(R.drawable.surprised);
                what_game = "Surprise";
                break;
            case "angry":
                imageView.setImageResource(R.drawable.angry);
                what_game = "Angry";
                break;
        }

        // 카메라 뷰 부분
        cameraKitView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
            }

            @Override
            public void onError(CameraKitError cameraKitError) {
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
                final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
                gameResult = results.toString(); // 나온 결과 값을 String화 시킴

                //db저장
                DBHelper3 helper = new DBHelper3(getApplicationContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                Date currentTime = Calendar.getInstance().getTime();
                String date_text = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentTime);
                String new_user_id = user_id + what_game;
                if(gameResult.contains(what_game)){ //게임 결과에 내가 선택한 게임에 대한 확률이 나올 경우
                    String tmp = gameResult.substring(gameResult.indexOf(what_game));
                    String[] array = tmp.split(" ");
                    String sql = String.format("INSERT INTO T3 VALUES ('" + new_user_id + "','" + date_text + "'," + Float.valueOf(array[1].substring(1, array[1].length() -3)) + ");");
                    Log.d(TAG, "확률111 : "+ Float.valueOf(array[1].substring(1, array[1].length() -3)));
                    db.execSQL(sql);
                }
                else{
                    String sql = String.format("INSERT INTO T3 VALUES ('" + new_user_id + "','" + date_text + "'," + 0 + ");");
                    db.execSQL(sql);
                }
                //id, 날짜, 점수를 T1에 저장
                Log.d(TAG, "김의현" + gameResult);
                gameResult = gameResult.substring(1);
                Intent intent = new Intent(getApplicationContext(), GameResultActivity.class);
                intent.putExtra("id", user_id);
                intent.putExtra("gameResult", gameResult);
                startActivity(intent);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {
            }
        });

        count(); // 3초후 자동 촬영
        initTensorFlowAndLoadModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.start();
    }

    @Override
    protected void onPause() {
        cameraKitView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }

    private void count() {
        count = 3;
        sec = new TimerTask() {
            @Override
            public void run() {
                // count가 0초가 될 경우
                if (count == 0) {
                    cameraKitView.captureImage(); // 사진 캡쳐
                    sec.cancel();
                } else {
                    count--;
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(sec, 0, 1000);
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = ImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE,
                            QUANT);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }
}