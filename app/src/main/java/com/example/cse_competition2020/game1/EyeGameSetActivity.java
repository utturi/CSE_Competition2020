package com.example.cse_competition2020.game1;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cse_competition2020.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import static org.opencv.imgproc.Imgproc.rectangle;

public class EyeGameSetActivity extends AppCompatActivity {
    private static final String TAG = "EyeGameSetActivity";
    public static final int GALLERY = 1;
    boolean isReady = false;
    private ImageView image, imageGuide; // 사진 선택 및 사진 등록 메뉴얼
    private Uri uri;
    private String path;
    protected Mat matInput, matOutput, subFace;
    protected MatOfRect eyes = new MatOfRect();

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

        image = (ImageView)findViewById(R.id.imageViewInput);
        imageGuide = (ImageView)findViewById(R.id.imageGuide);

        // 사진 등록 이미지
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallery();

                /*// 등록된 사진이 제대로 인식되지 않았을 경우
                if(checkImage() == -1) {
                    startActivity(new Intent(getApplication(), EyeGameSetActivity.class));
                    //Intent eye = new Intent(V.getContext(), EyeGameSetActivity.class);
                    //startActivity(eye);
                }*/
            }
        });

        // 사진 등록 메뉴얼(느낌표)
        imageGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(EyeGameSetActivity.this);
                dlg.setTitle("사진 등록 방법"); // 제목
                // 메시지(설명란)
                dlg.setMessage("");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.show();
            }
        });

        /*if (!hasPermissions(PERMISSIONS)) { //퍼미션 허가를 했었는지 여부를 확인
            requestNecessaryPermissions(PERMISSIONS);//퍼미션 허가안되어 있다면 사용자에게 요청
        }*/

        /*// 등록된 사진이 제대로 인식되지 않았을 경우
        if(checkImage() == -1) {
            Toast.makeText(getApplicationContext(), "사진을 다시 선택해주세요!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplication(), EyeGameSetActivity.class));
        }*/
    }

    private int checkImage() {
        matOutput = new Mat(matInput.rows(), matInput.cols(), matInput.type());
        ConvertRGBtoGray(matInput.getNativeObjAddr(), matOutput.getNativeObjAddr());
        if(detectFace() == -1)
            return -1;

        //resultImage();

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
                dlg.setMessage("");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.show();
                break;
            case R.id.game1start_button: //게임1의 <게임 시작> 버튼에 대한 이벤트 처리
                if(checkImage() == -1) {
                    Toast.makeText(getApplicationContext(), "사진을 다시 선택해주세요!", Toast.LENGTH_LONG).show();
                    break;
                }
                else {
                    Intent game1 = new Intent(V.getContext(), EyeGameStartActivity.class);
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
        CascadeClassifier cascade_1 = new CascadeClassifier();
        CascadeClassifier cascade_2 = new CascadeClassifier();
        if (cascade_1.empty()) {
            String path = getExternalFilesDir(null).toString();
            cascade_1.load(path + "/haarcascade_frontalface_alt.xml"); // 얼굴 인식 오픈소스
            cascade_2.load(path + "/haarcascade_eye.xml"); // 눈 인식 오픈소스
            //Log.d("인식 오픈소스 실행 중",getExternalFilesDir(null).toString());
        }

        Mat gray = new Mat();
        Imgproc.cvtColor(matInput, gray, Imgproc.COLOR_BGRA2GRAY);

        // 얼굴 인식
        MatOfRect faces = new MatOfRect();
        cascade_1.detectMultiScale(gray, faces, 1.3, 3, 0, new Size(40, 40));

        // 눈 인식
        //MatOfRect eyes = new MatOfRect();
        cascade_2.detectMultiScale(gray, eyes, 1.3, 7

                , 0, new Size(25, 25));

        Log.d(TAG, "얼굴 개수 리턴값 : " + faces.total());
        Log.d(TAG, "눈 개수 리턴값 : " + eyes.total());

        // 인식된 얼굴의 개수가 1개가 아니거나 눈의 개수가 2개가 아닐 경우 -> 사진 다시 등록해야 함
        if(faces.total() == 1 && eyes.total() == 2)
            return 1;
        else
            return -1;

            /*Log.d(TAG, "반환값 : " + eyes.total());
            for (int i = 0; i < eyes.total(); i++) {
                Rect rc = eyes.toList().get(i);
                rectangle(matInput, rc, new Scalar(255, 255, 255, 1), 1);
            }
            Log.d(TAG, "반환값 1: " + eyes.toList().get(0));
            Log.d(TAG, "반환값 2: " + eyes.toList().get(1));*/

        /*for (int i = 0; i < faces.total(); i++) {
            Rect rc = faces.toList().get(i);
            rectangle(matInput, rc, new Scalar(255, 255, 255, 0.5), 1);
            subFace = matInput.submat(rc); // 얼굴 인식한 부분만 추출
        }

        // 눈 주위에 직사각형을 그려주는 부분
        if (eyes.total() == 2) {
            Rect rc_tmp = new Rect();
            rc_tmp.x = Math.min(Math.abs(eyes.toList().get(0).x), Math.abs(eyes.toList().get(1).x));
            rc_tmp.y = Math.min(Math.abs(eyes.toList().get(0).y), Math.abs(eyes.toList().get(1).y));
            //rc_tmp.width=rc_tmp.x + Math.max(eyes.toList().get(0).width,eyes.toList().get(1).width);
            rc_tmp.width = Math.max(eyes.toList().get(0).x + eyes.toList().get(0).width, eyes.toList().get(1).x + eyes.toList().get(1).width) - rc_tmp.x;
            rc_tmp.height = rc_tmp.y - Math.min(eyes.toList().get(0).y + eyes.toList().get(0).height, eyes.toList().get(1).y - eyes.toList().get(1).height);
            rectangle(matInput, rc_tmp, new Scalar(0, 0, 0, 0.5), 1);
        }*/
    }

    // 갤러리에서 가져온 사진을 절대경로로 바꾸는 메소드
    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        return cursor.getString(column_index);
    }

    static final int PERMISSION_REQUEST_CODE = 1;
    String[] PERMISSIONS  = {"android.permission.WRITE_EXTERNAL_STORAGE"};

    private boolean hasPermissions(String[] permissions) {
        int ret = 0;
        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions){
            ret = checkCallingOrSelfPermission(perms);
            if (!(ret == PackageManager.PERMISSION_GRANTED)){
                //퍼미션 허가 안된 경우
                return false;
            }

        }
        //모든 퍼미션이 허가된 경우
        return true;
    }

    private void requestNecessaryPermissions(String[] permissions) {
        //마시멜로( API 23 )이상에서 런타임 퍼미션(Runtime Permission) 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }
}