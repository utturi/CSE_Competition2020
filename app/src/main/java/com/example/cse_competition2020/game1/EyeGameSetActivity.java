package com.example.cse_competition2020.game1;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse_competition2020.ChangeActivity;
import com.example.cse_competition2020.GameSelectActivity;
import com.example.cse_competition2020.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class EyeGameSetActivity extends AppCompatActivity {
    private static final String TAG = "EyeGameSetActivity";
    public static final int GALLERY = 1;
    String user_id;
    boolean isReady = false;
    private ImageView image, imageGuide; // 사진 선택 및 사진 등록 메뉴얼
    private Uri uri;
    private String path;
    public Mat matInput, matOutput, subFace;
    protected MatOfRect eyes = new MatOfRect();
    private int[] eye_1 = new int[4];
    private int[] eye_2 = new int[4];
    private CascadeClassifier cas_face = new CascadeClassifier();
    private CascadeClassifier cas_eye = new CascadeClassifier();

    // OpenCV 네이티브 라이브러리와 C++코드로 빌드된 라이브러리를 읽음(CMakeList.txt add_library and target 참조)
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java4");
    }

    // C++ JNI 함수와 연결할 메소드(native-lib.cpp 참조)
    public native void ConvertRGBtoGray(long matAddrInput, long matAddrResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_game_set);
        Intent intent = getIntent();
        user_id = intent.getExtras().getString("id"); //user id를 받아서 저장
        image = (ImageView) findViewById(R.id.imageViewInput);
        imageGuide = (ImageView) findViewById(R.id.imageGuide);

        // 사진 등록 이미지
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallery();
            }
        });

        // 사진 등록 메뉴얼(느낌표)
        imageGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(EyeGameSetActivity.this);
                dlg.setTitle("사진 등록 방법"); // 제목
                // 메시지(설명란)
                dlg.setMessage("-밑 이미지를 누르고 원하는 사진을 고르세요.\n" +
                        "-적절한 사진인 경우, 바로 게임이 시작됩니다.\n" +
                        "-사진이 적절하지 않으면 다시 선택해주세요!");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.show();
            }
        });
    }

    private int checkImage() {
        if (matInput == null)
            return -2;
        matOutput = new Mat(matInput.rows(), matInput.cols(), matInput.type());
        ConvertRGBtoGray(matInput.getNativeObjAddr(), matOutput.getNativeObjAddr());
        if (detectFace() == -1)
            return -1;

        return 1;
    }

    // 버튼 이벤트 처리
    public void onClick(final View V) {
        switch (V.getId()) {
            case R.id.game1guide_button: //게임1의 <게임 설명> 버튼에 대한 이벤트 처리
                //다이얼로그 띄어서 간단 설명
                AlertDialog.Builder dlg = new AlertDialog.Builder(EyeGameSetActivity.this);
                dlg.setTitle("게임 설명"); //제목
                //메시지(설명란)
                dlg.setMessage("-이 게임은 5초간 눈을 마주치는 게임입니다.\n" +
                        "-5초에서 얼굴을 바라보는 시간을 측정해서 알려드립니다!\n" +
                        "-오른쪽 상단의 버튼을 눌러서 사진 등록 방법을 볼 수 있습니다.\n" +
                        "-아이가 집중할 수 있도록 적절한 사진을 선택하세요!");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.show();
                break;
            case R.id.game1start_button: //게임1의 <게임 시작> 버튼에 대한 이벤트 처리
                if (checkImage() == -1) {
                    Toast.makeText(getApplicationContext(), "사진을 다시 선택해주세요!", Toast.LENGTH_LONG).show();
                    break;
                } else if (checkImage() == -2) {
                    Toast.makeText(getApplicationContext(), "사진을 선택하지 않았습니다!", Toast.LENGTH_LONG).show();
                    break;
                } else { //인텐트에 얼굴과 눈의 정보를 입력하고 다음 엑티비티 실행
                    long subface = subFace.getNativeObjAddr();
                    Intent game1 = new Intent(V.getContext(), ChangeActivity.class);
                    game1.putExtra("id", user_id);
                    game1.putExtra("subface", subface);
                    game1.putExtra("eye_1", eye_1);
                    game1.putExtra("eye_2", eye_2);
                    game1.putExtra("gameNum", 1);
                    startActivity(game1);
                    break;
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isReady = true;
    }

    // 로컬 갤러리로 접근하는 함수
    private void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    // 갤러리에서 가져온 이미지를 Mat객체로 변환하는 함수
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY) {
            if (data == null) {
                return;
            }
            if (data.getData() != null) {
                try {
                    uri = data.getData();
                    path = getPath(uri);
                    image.setImageURI(uri);

                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), uri);
                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                    // bitmap = temp;
                    image.setImageBitmap(bitmap);
                    matInput = new Mat();
                    matOutput = new Mat();

                    // ARGB_8888(4바이트) 값으로 비트맵 복사
                    Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    // 복사한 bmp32 이미지를 opencv에 맞게 Mat형식으로 변환
                    Utils.bitmapToMat(bmp32, matInput);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 최종적으로 인식된 Mat객체를 bitmap으로 변환(얼굴인식된 부분만 리턴)
    private void resultImage() {
        Bitmap bitmapOutput = Bitmap.createBitmap(subFace.cols(), subFace.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(subFace, bitmapOutput);
        image.setImageBitmap(bitmapOutput);
    }

    // 얼굴 인식 & 눈 인식 함수
    private int detectFace() {
        if (openXML() == -1) return -1; //인식에 필요한 XML을 open
        Rect face_rc;
        Mat gray = new Mat();
        Imgproc.cvtColor(matInput, gray, Imgproc.COLOR_BGRA2GRAY); //인식을 위해 회색조로 변경

        // 얼굴 인식
        MatOfRect faces = new MatOfRect();
        cas_face.detectMultiScale(gray, faces, 1.3, 1, 0, new Size(40, 40));
        Log.d(TAG, "얼굴 개수 리턴값 : " + faces.total());
        if (faces.total() == 1) { //얼굴이 한 개 인식된 경우
            face_rc = faces.toList().get(0);
            subFace = matInput.submat(face_rc); //얼굴 부분만 추출해서 subFace에 저장
            Log.d(TAG, "ㅅ브페이스 : " + subFace);
            face_rc.height /= 2; //원할한 눈 인식을 위해 높이를 변경
            //눈 인식
            cas_eye.detectMultiScale(gray.submat(face_rc), eyes, 1.3, 5, 0, new Size(20, 20));
            Log.d(TAG, "눈 개수 리턴값 : " + eyes.total());
            if (eyes.total() == 2) { //눈 개수가 2개 인식된 경우 사각형을 그리기 위한 정보 저장
                //첫 번째 눈의 정보를 저장
                eye_1[0] = eyes.toList().get(0).x;
                eye_1[1] = eyes.toList().get(0).y;
                eye_1[2] = eyes.toList().get(0).width;
                eye_1[3] = eyes.toList().get(0).height;
                //두 번째 눈의 정보를 저장
                eye_2[0] = eyes.toList().get(1).x;
                eye_2[1] = eyes.toList().get(1).y;
                eye_2[2] = eyes.toList().get(1).width;
                eye_2[3] = eyes.toList().get(1).height;
                return 1;
            } else return -1;
        } else return -1;
    }

    // 갤러리에서 가져온 사진을 절대경로로 바꾸는 메소드
    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        return cursor.getString(column_index);
    }

    private int openXML() { //인식에 필요한 XML을 open
        cas_face = new CascadeClassifier();
        cas_eye = new CascadeClassifier();
        String path = getExternalFilesDir(null).toString();
        if (cas_face.empty()) { //얼굴 탐지를 위한 파일 load
            cas_face.load(path + "/haarcascade_frontalface_alt.xml");
            Log.d("파일 열기 시도", getExternalFilesDir(null).toString());
            if (cas_face.empty()) {
                Log.d("파일 열기 실패", getExternalFilesDir(null).toString());
                return -1; //비정상 종료
            }
        }
        if (cas_eye.empty()) { //눈 탐지를 위한 파일 load
            cas_eye.load(path + "/haarcascade_eye.xml");
            Log.d("파일 열기 시도", getExternalFilesDir(null).toString());
            if (cas_eye.empty()) {
                Log.d("파일 열기 실패", getExternalFilesDir(null).toString());
                return -1; //비정상 종료
            }
        }
        return 1; //정상적으로 open이 완료되면 1을 리턴
    }

    @Override
    public void onBackPressed() { //백버튼을 누르면 게임 선택 엑티비티르 돌아감
        Intent back = new Intent(getApplicationContext(), GameSelectActivity.class);
        back.putExtra("id", user_id); //user_id를 다시 게임 선택 엑티비티로 넘겨줌
        startActivity(back);
    }
}