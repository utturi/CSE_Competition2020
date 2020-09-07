package com.example.cse_competition2020.game3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.cse_competition2020.R;

public class FaceGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_game);
        Toast.makeText(getApplicationContext(), "This is FaceGameActivity", Toast.LENGTH_LONG).show();

    }
}