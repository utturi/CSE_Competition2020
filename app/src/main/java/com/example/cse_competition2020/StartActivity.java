package com.example.cse_competition2020;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cse_competition2020.db.DBHelper;
import com.example.cse_competition2020.db.DBHelper1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/*
2020.8.18 영훈
버튼 누르면 onClick 실행돼서 각각 다이얼로그 띄우게 했음
-add 버튼은 누르면 이름, 생일 저장 후에 MainActivity로 이동
 (DB에 입력된 이름, 생일 저장 필요)
-load 버튼은 누르면 일단은 예시용 데이터로 선택 dialog 띄운 후에 불러오기 누르면 MainActivity로 이동
 (DB로부터 저장된 정보 불러와서 선택 dialog에 띄우기 필요)

2020.8.19 영훈
마이크, 카메라 퍼미션 추가

2020.09.12 대호
- Back 버튼 2번 누르면 종료
 */

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    String name; //입력받은 이름이 저장됨
    private long backKeyPressedTime = 0; // 시간을 저장하는 변수
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        int CameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int VoicePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int ReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int WritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (CameraPermission == PackageManager.PERMISSION_GRANTED
                && VoicePermission == PackageManager.PERMISSION_GRANTED
                && ReadPermission == PackageManager.PERMISSION_GRANTED
                && WritePermission == PackageManager.PERMISSION_GRANTED)
            ;
        else
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //권한 체크 응답 함수
        if (grantResults.length > 0) { //권한 허가
            if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED
                    || grantResults[2] == PackageManager.PERMISSION_DENIED || grantResults[3] == PackageManager.PERMISSION_DENIED) {
                finish();
            }
        }
    }

    @Override
    public void onClick(final View V) { //버튼 이벤트 처리
        switch (V.getId()) {
            case R.id.add_button: //추가 버튼에 대한 이벤트 처리
                AlertDialog.Builder ad = new AlertDialog.Builder(StartActivity.this);
                ad.setMessage("아이의 이름을 입력하세요");
                final EditText e1 = new EditText(StartActivity.this);
                ad.setView(e1);
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() { //확인 버튼을 누른 경우
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name = e1.getText().toString(); //입력한 아이 이름이 저장됨
                        Toast.makeText(getApplicationContext(), "아이의 생년월일을 선택하세요", Toast.LENGTH_SHORT).show(); //아이의 생일을 입력받아서 개월 수를 저장
                        final Calendar cal = Calendar.getInstance();
                        DatePickerDialog dialog2 = new DatePickerDialog(StartActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                                //Toast.makeText(StartActivity.this, "년도" + year, Toast.LENGTH_SHORT).show();
                                //입력한 아이랑 나이를 table에 저장
                                com.example.cse_competition2020.db.DBHelper helper =
                                        new com.example.cse_competition2020.db.DBHelper(getApplicationContext());
                                SQLiteDatabase db = helper.getWritableDatabase();
                                String sql;
                                // UUID uuid = UUID.randomUUID();
                                //겹치지않는 아이디 값 설정
                                String convertPw = UUID.randomUUID().toString().replace("-", "");
                                String age;
                                if (month < 10) {
                                    if (date < 10) {
                                        age = String.format((year % 100) + ".0" + (month + 1) + ".0" + date);
                                    } else {
                                        age = String.format((year % 100) + ".0" + (month + 1) + "." + date);
                                    }
                                } else {
                                    if (date < 10) {
                                        age = String.format((year % 100) + "." + (month + 1) + ".0" + date);
                                    } else {
                                        age = String.format((year % 100) + "." + (month + 1) + "." + date);
                                    }
                                }
                                sql = String.format("INSERT INTO T0 VALUES ('" + convertPw + "','" + name + "','" + age + "');");
                                db.execSQL(sql);
                                db.close();
                                Intent start_intent = new Intent(getApplicationContext(), MainActivity.class); //MainActivity 실행
                                start_intent.putExtra("id", convertPw); //해당 고유번호(id)를 가진 상태로 시작 화면으로 전환
                                start_intent.putExtra("user_name", name + "(" + age + ")");
                                startActivity(start_intent);
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
            case R.id.remove_button: //삭제 버튼 처리
            case R.id.load_button: //불러오기 버튼에 대한 이벤트 처리
                final List<String> list = new ArrayList<>();
                final List<String> id_list = new ArrayList<>();
                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                DBHelper helper = new DBHelper(getApplicationContext());
                final SQLiteDatabase db = helper.getWritableDatabase();
                DBHelper1 helper1 = new DBHelper1(getApplicationContext());
                final SQLiteDatabase db1 = helper1.getWritableDatabase();
                String sql = "SELECT * FROM T0;";
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        id_list.add(String.format(cursor.getString(0)));
                        list.add(String.format("%s" + "(%s)", cursor.getString(1), cursor.getString(2)));
                    }
                }
                //cursor.close();
                //불러올 데이터가 없다면
                if (list.isEmpty()) {
                    Toast.makeText(StartActivity.this, "불러올 수 있는 아이가 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    final CharSequence[] data = list.toArray(new CharSequence[list.size()]);
                    final int[] check = new int[1];
                    builder.setTitle("아이 목록")
                            .setSingleChoiceItems(data, 0,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            check[0] = i;
                                            //Toast.makeText(StartActivity.this, ""+check[0], Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (V.getId() == R.id.load_button) {
                                        Intent start_intent = new Intent(getApplicationContext(), MainActivity.class); //MainActivity 실행
                                        start_intent.putExtra("id", id_list.get(check[0])); //해당 고유번호(id)를 가진 상태로 시작 화면으로 전환
                                        start_intent.putExtra("user_name", list.get(check[0]));
                                        startActivity(start_intent);
                                    } else if (V.getId() == R.id.remove_button) { //삭제 버튼일경우
                                        db.execSQL("DELETE FROM T0 WHERE id = '" + id_list.get(check[0]) + "'");
                                        db1.execSQL("DELETE FROM T1 WHERE id = '" + id_list.get(check[0]) + "'");
                                        //Toast.makeText(StartActivity.this, ""+sel_id + "이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                //db.close();
                break;
        }
    }

    // Back Button 눌렀을 경우
    @Override
    public void onBackPressed() {
        // 현재 시간이 backKeyPressedTime + 2000보다 크면 backKeyPressedTime에 현재시간을 저장하고 알림창을 띄움
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        // 현재 시간이 backKeyPressedTime + 2000보다 작으면 앱 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            //this.finish();
            ActivityCompat.finishAffinity(this);
            System.exit(0);
            toast.cancel();
        }
    }

    // 알림창 띄우는 함수
    public void showGuide() {
        toast = Toast.makeText(this, "\'뒤로\' 버튼을 한 번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT);
        toast.show();
    }
}