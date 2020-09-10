package com.example.cse_competition2020.game1;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.cse_competition2020.GameResultActivity;
import com.example.cse_competition2020.R;
import com.example.cse_competition2020.StartActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.CAMERA;
import static org.opencv.imgproc.Imgproc.HoughCircles;
import static org.opencv.imgproc.Imgproc.filter2D;
import static org.opencv.imgproc.Imgproc.rectangle;
import static org.opencv.imgproc.Imgproc.circle;
import static org.opencv.imgproc.Imgproc.resize;

public class EyeGameStartActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "opencv";
    private Mat matInput;
    private Mat matResult;
    private Mat gray, thresh;
    private Mat eye,face,groundFace;
    String user_id;
    String gameResult="";
    private CameraBridgeViewBase mOpenCvCameraView;
    private Point left=null,right=null;
    private Rect first_eye=new Rect(),second_eye=new Rect();
    TimerTask limit,score;
    CascadeClassifier cas_face,cas_eye;
    static int counter;

    public native void ConvertRGBtoGray(long matAddrInput, long matAddrResult);


    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_eye_game_start);
        Matrix matrix = new Matrix();
        matrix.setScale(-1,1);

        if(openXML()<0) { //인식에 필요한 XML 파일을 open
            android.os.Process.killProcess(android.os.Process.myPid()); //파일 open에 실패한 경우 앱 강제종료
        }

        Intent intent = getIntent();
        long addr = intent.getLongExtra("subface", 0); //입력받은 사용자 사진을 띄움
        user_id = intent.getExtras().getString("id"); //user id를 받아서 저장
        int eye_1[] = intent.getIntArrayExtra("eye_1");
        int eye_2[] = intent.getIntArrayExtra("eye_2");
        groundFace=new Mat(addr);
        first_eye.x=eye_1[0];
        first_eye.y=eye_1[1];
        first_eye.width=eye_1[2];
        first_eye.height=eye_1[3];
        second_eye.x=eye_2[0];
        second_eye.y=eye_2[1];
        second_eye.width=eye_2[2];
        second_eye.height=eye_2[3];
        drawEye(); //입력받은 얼굴의 눈 부분을 표시
        count5sec(); //5초 count

        //게임 시작을 위한 카메라 세팅
        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(1); // front-camera(1),  back-camera(0)
    }

    private void drawEye(){ //입력받은 사진에 눈을 표시
        Rect rc_tmp = new Rect();
        rc_tmp.x = Math.min(Math.abs(first_eye.x), Math.abs(second_eye.x));
        rc_tmp.y = Math.min(Math.abs(first_eye.y), Math.abs(second_eye.y));
        //rc_tmp.width=rc_tmp.x + Math.max(eyes.toList().get(0).width,eyes.toList().get(1).width);
        rc_tmp.width = Math.max(first_eye.x + first_eye.width, second_eye.x + second_eye.width) - rc_tmp.x;
        rc_tmp.height = rc_tmp.y - Math.min(first_eye.y + first_eye.height, second_eye.y - second_eye.height);
        rectangle(groundFace, rc_tmp, new Scalar(255, 255, 255, 0.5), 2);
    }

    private int openXML(){ //인식에 필요한 XML을 open
        cas_face = new CascadeClassifier();
        cas_eye = new CascadeClassifier();
        String path = getExternalFilesDir(null).toString();
        if (cas_face.empty()) { //얼굴 탐지를 위한 파일 load
            cas_face.load(path+"/haarcascade_frontalface_alt.xml");
            Log.d("파일 열기 시도",getExternalFilesDir(null).toString());
            if (cas_face.empty()){
                Log.d("파일 열기 실패",getExternalFilesDir(null).toString());
                return -1; //비정상 종료
            }
        }
        if (cas_eye.empty()) { //눈 탐지를 위한 파일 load
            cas_eye.load(path+"/haarcascade_eye.xml");
            Log.d("파일 열기 시도",getExternalFilesDir(null).toString());
            if (cas_eye.empty()) {
                Log.d("파일 열기 실패",getExternalFilesDir(null).toString());
                return -1; //비정상 종료
            }
        }
        return 1; //정상적으로 open이 완료되면 1을 리턴
    }

    private void count5sec(){ //5초를 count
        counter=0;
        limit=new TimerTask() {
            @Override
            public void run() {
                Log.e(String.valueOf(counter)+" ","초 경과");
                if(++counter==6){
                    limit.cancel();
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EyeGameStartActivity.this, "게임이 종료되었습니다!",Toast.LENGTH_LONG).show();
                        }
                    }, 0);
                    //게임 결과 넘기고 gameresult activity 실행
                    gameResult="From EyeGameStartActivity!!";
                    Intent result = new Intent(getApplicationContext(), GameResultActivity.class);
                    result.putExtra("id",user_id);
                    result.putExtra("gameResult", gameResult);
                    startActivity(result);
                }

            }
        };
        Timer timer=new Timer();
        timer.schedule(limit,0,1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    public void onDestroy() {
        super.onDestroy();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Log.d("온 카메라 프레임","  진입!!!!");
        matInput = inputFrame.rgba();
        Core.flip(matInput.t(),matInput,-1); //반전
        //Core.flip(matInput,matInput,1); //좌우반전
        //Core.flip(matInput,matInput,0); //좌우반전
        if ( matResult == null )
            matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

        ConvertRGBtoGray(matInput.getNativeObjAddr(), matResult.getNativeObjAddr());
        gray = new Mat();
        thresh =new Mat();
        Log.d("동공 찾기","  시작!!!");
        detectPupil();
        Log.d("동공 찾기","  끝!!!");

        Log.d("matinput height : "+matInput.height(),"width : "+matInput.width());
        Log.d("matInput : ",matInput+"");
        resize(groundFace,groundFace,new Size(matInput.width(),matInput.height()));
        return groundFace; //여기서 리턴을 자르고 직사각형 그린 mat 변수로 하면 됨
    }


    private void detectPupil() {
        MatOfRect faces = new MatOfRect();
        MatOfRect eyes = new MatOfRect();
        Rect face_rc,eye_rc;
        int i,tmp,max=0,maxindex=0;

        Imgproc.cvtColor(matInput, gray, Imgproc.COLOR_BGRA2GRAY); //원본 프레임을 회색조로 변경
        Imgproc.equalizeHist(gray,gray);
        Imgproc.threshold(gray, thresh,70,255,Imgproc.THRESH_BINARY);
        Imgproc.erode(thresh, thresh, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));
        Imgproc.dilate(thresh, thresh, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4, 4)));
        //Imgproc.medianBlur(thresh,thresh,11);


        cas_face.detectMultiScale(gray, faces, 1.3, 1, 0, new Size(40, 40)); //회색조로부터 얼굴 부분을 찾음
        for (i = 0; i < faces.total(); i++) {
            face_rc = faces.toList().get(i); //인식된 얼굴의 정보를 저장
            tmp=face_rc.height*face_rc.width;
            if(tmp>max){ //가장 큰 얼굴 값을 저장
                max=tmp;
                maxindex=i;
            }
        }

        if(i==0)return; //찾은 얼굴이 없으면 return
        face_rc=faces.toList().get(maxindex); //가장 큰 얼굴 부분을 저장
        rectangle(matInput, face_rc, new Scalar(255, 255, 0, 0.8), 2); //찾은 얼굴 부분을 사각형으로 표시
        face_rc.height/=2;
        face=gray.submat(face_rc); //흑백으로 바꾼 찾은 얼굴 영역을 face Mat 변수에 저장
        cas_eye.detectMultiScale(face, eyes, 1.3, 2, 0, new Size(face_rc.width/5, face_rc.width/5)); //회색조의 얼굴 부분에서 눈 부분을 찾음
        for(i=0;i<eyes.total();i++){
            eye_rc=eyes.toList().get(i); //구한 눈의 정보를 저장
            eye_rc.x+=face_rc.x; //원래 좌표에 맞게 조정
            eye_rc.y+=face_rc.y;
            int tmp_y=eye_rc.y,tmp_height=eye_rc.height,tmp_width=eye_rc.width;
            eye_rc.y+=0.35*eye_rc.height;
            eye_rc.height*=0.4;
            if(eye_rc.x<(face_rc.x+face_rc.width)/2){ //오른쪽 눈의 경우
                eye_rc.x+=0.2*eye_rc.width;
                eye_rc.width*=0.6;
            }
            else{
                eye_rc.width*=0.6;
                eye_rc.x+=0.2*tmp_width;
            }
            rectangle(matInput, eye_rc, new Scalar(255, 0, 0, 0.8), 2); //찾은 얼굴 부분을 사각형으로 표시

            /*
            eye=gray.submat(eye_rc); //회색조에서 눈 부분만 자름
            Imgproc.equalizeHist(eye,eye);
            Imgproc.threshold(eye, eye,20,255,Imgproc.THRESH_BINARY);
            Imgproc.erode(eye, eye, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));
            Imgproc.dilate(eye, eye, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4, 4)));
            Imgproc.medianBlur(eye,eye,13);
*/
            eye=thresh.submat(eye_rc);
            ///***
            /*Mat pupil=new Mat();
            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Imgproc.findContours(eye, contours, pupil, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,new Point(eye_rc.x,eye_rc.y)); //흑백으로부터 찾은 눈 부분의 윤곽을 contours에 저장
            int maxindex1=0,max1=0;
            for(int k=0;k<contours.size();k++){
                *//*if(contours.get(k).height()*contours.get(k).width()>max1){
                    max1=contours.get(k).height()*contours.get(k).width();
                    maxindex1=k;
                }*//*
                Imgproc.drawContours(matInput,contours,k,new Scalar(0,255,255),2);
            }*/
            //Imgproc.drawContours(matInput,contours,maxindex1,new Scalar(0,255,255),2);
            ///***

            //////****

            int maxRadius=0,maxRadiusindex=0;
            Mat circles=new Mat(); //허프 변환을 통해 원을 찾아서 그리기
            Imgproc.HoughCircles(eye,circles,Imgproc.CV_HOUGH_GRADIENT,1,100,100,8,eye_rc.height/4,eye_rc.height/2);
            //Imgproc.HoughCircles(eye,circles,Imgproc.CV_HOUGH_GRADIENT,1,100,100,11,0,eye_rc.width);
            if (circles.cols() > 0) {
                for (int x=0; x < circles.cols(); x++ ) {
                    double circleVec[]=circles.get(0,x);
                    if(circleVec==null)continue;
                    if((int)circleVec[2]>maxRadius){
                        maxRadius=(int)circleVec[2];
                        maxRadiusindex=x;
                    }
                        /*double circleVec[] = circles.get(0, x);
                        if (circleVec == null)
                            break;
                        Point center = new Point((int) circleVec[0]+eye_rc.x, (int) circleVec[1]+eye_rc.y); //찾은 동공의 좌표
                        int radius = (int) circleVec[2];
                        if(center.y<tmp_y+tmp_height*0.4||center.y>tmp_y+tmp_height*0.7)continue; //과도하게 벗어난 경우 continue
                        if(center.x+radius>=eye_rc.x+eye_rc.width||center.x-radius<=eye_rc.x
                        ||center.y-radius<=eye_rc.y||center.y+radius>=eye_rc.y+eye_rc.height)continue;

                        if(center.x>(int)(eye_rc.x+eye_rc.x+eye_rc.width)/2) { //찾은 동공이 어느쪽 눈인지 판단
                            right = center;
                            Log.d("오른쪽눈 : ",right.toString());
                        }
                        else {
                            left = center;
                            Log.d("왼눈 : ",left.toString());
                        }
                        Imgproc.circle(matInput, center, radius, new Scalar(255, 0, 255), 3);*/

                }
                Point center = new Point((int) circles.get(0,maxRadiusindex)[0]+eye_rc.x, (int) circles.get(0,maxRadiusindex)[1]+eye_rc.y); //찾은 동공의 좌표
                if(center.x>(int)((eye_rc.x+eye_rc.x+eye_rc.width)/2)) { //찾은 동공이 어느쪽 눈인지 판단
                    right = center;
                    Log.d("오른쪽눈 : ",right.toString());
                }
                else {
                    left = center;
                    Log.d("왼눈 : ",left.toString());
                }
                Imgproc.circle(matInput, center, 1, new Scalar(255, 255, 255), 10);
            }
            right=null;
            left=null;
            circles.release();
            //////***
        }
        //gray.release();
        //thresh.release();
        // eye.release();
    }
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    //여기서부턴 퍼미션 관련 메소드
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;


    protected void onCameraPermissionGranted() {
        List<? extends CameraBridgeViewBase> cameraViews = getCameraViewList();
        if (cameraViews == null) {
            return;
        }
        for (CameraBridgeViewBase cameraBridgeViewBase: cameraViews) {
            if (cameraBridgeViewBase != null) {
                cameraBridgeViewBase.setCameraPermissionGranted();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean havePermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                havePermission = false;
            }
        }
        if (havePermission) {
            onCameraPermissionGranted();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onCameraPermissionGranted();
        }else{
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder( EyeGameStartActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){
                requestPermissions(new String[]{CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.create().show();
    }
    @Override
    public void onBackPressed() { //back 버튼 눌러도 이전으로 돌아갈 수 없음
    }
}