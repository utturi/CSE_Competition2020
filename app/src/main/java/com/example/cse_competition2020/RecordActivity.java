package com.example.cse_competition2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class RecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //Toast.makeText(getApplicationContext(), "This is RecordActivity", Toast.LENGTH_LONG).show();
    }

    //핸드폰 back버튼을 누르면 StartActivity로 이동
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(back);
    }
}