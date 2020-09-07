package com.example.cse_competition2020;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
/*
2020.8.18 영훈
버튼 누르면 onClick 실행돼서 각각 다이얼로그 띄우게 했음.
-add 버튼은 누르면 이름, 생일 저장 후에 MainActivity로 이동
 (DB에 입력된 이름, 생일 저장 필요)
-load 버튼은 누르면 일단은 예시용 데이터로 선택 dialog 띄운 후에 불러오기 누르면 MainActivity로 이동
 (DB로부터 저장된 정보 불러와서 선택 dialog에 띄우기 필요)

 2020.8.19 영훈
 마이크, 카메라 퍼미션 추가
 */
public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    String name; //입력받은 이름이 저장됨
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        int CameraPermission=ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int VoicePermission=ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if(CameraPermission==PackageManager.PERMISSION_GRANTED&&VoicePermission==PackageManager.PERMISSION_GRANTED);
        else
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO},0);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //권한 체크 응답 함수
        if (grantResults.length > 0) { //권한 허가
            if(grantResults[0] == PackageManager.PERMISSION_DENIED||grantResults[1] == PackageManager.PERMISSION_DENIED){
                finish();
            }
        }
    }
    @Override
    public void onClick(final View V){ //버튼 이벤트 처리
        switch (V.getId()){
            case R.id.add_button: //추가 버튼에 대한 이벤트 처리
                AlertDialog.Builder ad = new AlertDialog.Builder(StartActivity.this);
                ad.setMessage("아이 이름을 입력하세요.");
                final EditText e1 = new EditText(StartActivity.this);
                ad.setView(e1);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() { //확인 버튼을 누른 경우
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name = e1.getText().toString(); //입력한 아이 이름이 저장됨
                        Toast.makeText(getApplicationContext(), "아이 생일을 선택하세요.", Toast.LENGTH_SHORT).show(); //아이의 생일을 입력받아서 개월 수를 저장
                        final Calendar cal = Calendar.getInstance();
                        DatePickerDialog dialog2 = new DatePickerDialog(StartActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                                String msg = String.format("%s의 생일은 %d 년 %d 월 %d 일",name, year, month+1, date); //선택한 날짜 확인
                                Toast.makeText(StartActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(V.getContext(),MainActivity.class); //MainActivity 실행
                                startActivity(intent);
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                        dialog2.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                        dialog2.show();
                        dialog.dismiss();
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() { //취소 버튼을 누른 경우
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                break;
            case R.id.load_button: //불러오기 버튼에 대한 이벤트 처리
                final String[] data=new String[]{"이영훈","임대호","김의현"};
                final int[] selectedItem={0};
                AlertDialog.Builder dialog=new AlertDialog.Builder(StartActivity.this);
                dialog  .setTitle("불러올 아이를 고르세요")
                        .setSingleChoiceItems(data, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedItem[0]=which;
                            }
                        })
                        .setPositiveButton("불러오기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(StartActivity.this
                                        , data[selectedItem[0]]
                                        , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(V.getContext(),MainActivity.class); //MainActivity 실행
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                dialog.create();
                dialog.show();
                break;
        }
    }

}