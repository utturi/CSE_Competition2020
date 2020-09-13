package com.example.cse_competition2020.record;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;

import com.example.cse_competition2020.MainActivity;
import com.example.cse_competition2020.R;
import com.example.cse_competition2020.db.DBHelper1;
import com.example.cse_competition2020.db.DBHelper2;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;

public class RecordActivity extends AppCompatActivity {
    public static String user_id;
    public static int game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent intent = getIntent(); //StartActivity에서 id가 넘어옴
        user_id = intent.getExtras().getString("id");


        LineChart lineChart = (LineChart) findViewById(R.id.chart1); //2번 또박또박 말하기 게임
        LineChart lineChart1 = findViewById(R.id.chart0); //1번 눈 마주치기 게임
        //LineChart lineChart2 = findViewById(R.id.chart2); //3번 표정 따라하기 게임


        //게임1데이터설정 ... 여기부터 눈마주치기
        ArrayList<Entry> entries0 = new ArrayList<>();
        DBHelper2 helper0 = new DBHelper2(getApplicationContext());
        final SQLiteDatabase db0 = helper0.getWritableDatabase();
        String sql0 = "SELECT * FROM T2;";
        Cursor cursor0 = db0.rawQuery(sql0, null);
        if (cursor0.getCount() > 0) {
            int tmp = 0;
            while (cursor0.moveToNext()) {
                if (cursor0.getString(0).equals(user_id)) {
                    entries0.add(new Entry((float)cursor0.getDouble(2), tmp));
                    tmp++;
                }
            }
        }

        LineDataSet dataset0 = new LineDataSet(entries0, ": 점수");
        cursor0 = db0.rawQuery(sql0, null);
        ArrayList<String> labels0 = new ArrayList<String>();
        if (cursor0.getCount() > 0) {
            while (cursor0.moveToNext()) {
                if (cursor0.getString(0).equals(user_id)) {
                    labels0.add("");
                }
            }
        }

        dataset0.setValueTextColor(38938);
        LineData data0 = new LineData(labels0, dataset0);
        dataset0.setLineWidth(2);
        dataset0.setColors(Collections.singletonList(getResources().getColor(R.color.colorAccent)));
        data0.setValueTextSize(20);
        XAxis xAxis0 = lineChart1.getXAxis();
        xAxis0.setTextColor(Color.BLACK);
        xAxis0.setTextSize(10);
        YAxis yLAxis0 = lineChart1.getAxisLeft();
        yLAxis0.setTextColor(Color.BLACK);
        yLAxis0.setTextSize(10);
        YAxis yRAxis0 = lineChart1.getAxisRight();
        yRAxis0.setDrawLabels(false);
        yRAxis0.setDrawAxisLine(false);
        yRAxis0.setDrawGridLines(false);
        CustomMarkerView mv0 = new CustomMarkerView(getApplicationContext(), R.layout.activity_custom_marker_view);
        lineChart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game=1;
            }
        });
        lineChart1.setMarkerView(mv0);
        lineChart1.getAxisLeft().setAxisMaxValue(6);
        lineChart1.getAxisLeft().setDrawTopYLabelEntry(false);
        lineChart1.setData(data0);
        lineChart1.animateY(2000, Easing.EasingOption.EaseInCubic);
        //여기까지

        //게임2데이터설정 ... 여기부터 또박또박말하기
        ArrayList<Entry> entries = new ArrayList<>();
        DBHelper1 helper = new DBHelper1(getApplicationContext());
        final SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT * FROM T1;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            int tmp = 0;
            while (cursor.moveToNext()) {
                if (cursor.getString(0).equals(user_id)) {
                    entries.add(new Entry((int)cursor.getInt(2), tmp));
                    tmp++;
                }
            }
        }

        LineDataSet dataset = new LineDataSet(entries, ": 점수");
        cursor = db.rawQuery(sql, null);
        ArrayList<String> labels = new ArrayList<String>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(0).equals(user_id)) {
                    labels.add("");
                }
            }
        }

        dataset.setValueTextColor(38938);
        LineData data = new LineData(labels, dataset);
        dataset.setLineWidth(2);
        dataset.setColors(Collections.singletonList(getResources().getColor(R.color.colorAccent)));
        data.setValueTextSize(20);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10);
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setTextSize(10);
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        CustomMarkerView mv = new CustomMarkerView(getApplicationContext(), R.layout.activity_custom_marker_view);
        lineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game=2;
            }
        });
        lineChart.setMarkerView(mv);
        lineChart.getAxisLeft().setAxisMaxValue(6);
        lineChart.getAxisLeft().setDrawTopYLabelEntry(false);
        lineChart.setData(data);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        //여기까지
    }

    public void onClick(View V){ //버튼 클릭에 대한 이벤트 처리
        switch(V.getId()){
            case R.id.back_button:
                Intent select = new Intent(V.getContext(),MainActivity.class);
                select.putExtra("id", user_id);
                startActivity(select);
                break;
            case R.id.game3_record_button: //게임 3에 대한 결과 엑티비티로 넘어감
                Intent game3_record = new Intent(V.getContext(),Game3RecordActivity.class);
                game3_record.putExtra("id", user_id);
                startActivity(game3_record);
                break;
        }
    }

    // Back 버튼을 누르면 StartActivity로 이동
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        back.putExtra("id", user_id);
        startActivity(back);
    }
}