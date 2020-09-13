package com.example.cse_competition2020.record;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse_competition2020.R;
import com.example.cse_competition2020.db.DBHelper3;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;

public class Game3RecordActivity extends AppCompatActivity {
    public static String user_id;
    public static String new_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3_record);

        Intent intent = getIntent(); //StartActivity에서 id가 넘어옴
        user_id = intent.getExtras().getString("id");

        LineChart lineChart1 = findViewById(R.id.chart3_1); //기쁨
        LineChart lineChart2 = findViewById(R.id.chart3_2); //슬픔
        LineChart lineChart3 = findViewById(R.id.chart3_3); //놀람
        LineChart lineChart4 = findViewById(R.id.chart3_4); //화남

        //기쁨그래프 설정
        ArrayList<Entry> entries1 = new ArrayList<>();
        DBHelper3 helper1 = new DBHelper3(getApplicationContext());
        final SQLiteDatabase db1 = helper1.getWritableDatabase();
        String sql1 = "SELECT * FROM T3;";
        Cursor cursor1 = db1.rawQuery(sql1, null);
        if (cursor1.getCount() > 0) {
            int tmp = 0;
            while (cursor1.moveToNext()) {
                if (cursor1.getString(0).equals(user_id+"Happy")) {
                    entries1.add(new Entry((float) cursor1.getFloat(2), tmp));
                    tmp++;
                }
            }
        }
        LineDataSet dataset1 = new LineDataSet(entries1, ": 확률(%)");
        cursor1 = db1.rawQuery(sql1, null);
        ArrayList<String> labels1 = new ArrayList<String>();
        if (cursor1.getCount() > 0) {
            while (cursor1.moveToNext()) {
                if (cursor1.getString(0).equals(user_id+"Happy")) {
                    labels1.add("");
                }
            }
        }
        dataset1.setValueTextColor(38938);
        LineData data1 = new LineData(labels1, dataset1);
        dataset1.setLineWidth(2);
        dataset1.setColors(Collections.singletonList(Color.BLUE));
        data1.setValueTextSize(20);
        XAxis xAxis1 = lineChart1.getXAxis();
        xAxis1.setTextColor(Color.BLACK);
        xAxis1.setTextSize(10);
        YAxis yLAxis1 = lineChart1.getAxisLeft();
        yLAxis1.setTextColor(Color.BLACK);
        yLAxis1.setTextSize(20);
        YAxis yRAxis1 = lineChart1.getAxisRight();
        yRAxis1.setDrawLabels(false);
        yRAxis1.setDrawAxisLine(false);
        yRAxis1.setDrawGridLines(false);
        CustomMarker3View mv1 = new CustomMarker3View(getApplicationContext(), R.layout.activity_custom_marker3_view);
        lineChart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_user_id = user_id + "Happy";
            }
        });
        lineChart1.setMarkerView(mv1);
        lineChart1.getAxisLeft().setAxisMaxValue(120);
        lineChart1.getAxisLeft().setDrawTopYLabelEntry(false);
        lineChart1.setData(data1);
        lineChart1.animateY(2000, Easing.EasingOption.EaseInCubic);
        //여기까지

        //슬픔그래프 설정
        ArrayList<Entry> entries2 = new ArrayList<>();
        DBHelper3 helper2 = new DBHelper3(getApplicationContext());
        final SQLiteDatabase db2 = helper2.getWritableDatabase();
        String sql2 = "SELECT * FROM T3;";
        Cursor cursor2 = db2.rawQuery(sql2, null);
        if (cursor2.getCount() > 0) {
            int tmp = 0;
            while (cursor2.moveToNext()) {
                if (cursor2.getString(0).equals(user_id+"Sad")) {
                    entries2.add(new Entry((float) cursor2.getDouble(2), tmp));
                    tmp++;
                }
            }
        }
        LineDataSet dataset2 = new LineDataSet(entries2, ": 확률(%)");
        cursor2 = db2.rawQuery(sql2, null);
        ArrayList<String> labels2 = new ArrayList<String>();
        if (cursor2.getCount() > 0) {
            while (cursor2.moveToNext()) {
                if (cursor2.getString(0).equals(user_id+"Sad")) {
                    labels2.add("");
                }
            }
        }
        dataset2.setValueTextColor(38938);
        LineData data2 = new LineData(labels2, dataset2);
        dataset2.setLineWidth(2);
        dataset2.setColors(Collections.singletonList(Color.BLUE));
        data2.setValueTextSize(20);
        XAxis xAxis2 = lineChart2.getXAxis();
        xAxis2.setTextColor(Color.BLACK);
        xAxis2.setTextSize(10);
        YAxis yLAxis2 = lineChart2.getAxisLeft();
        yLAxis2.setTextColor(Color.BLACK);
        yLAxis2.setTextSize(20);
        YAxis yRAxis2 = lineChart2.getAxisRight();
        yRAxis2.setDrawLabels(false);
        yRAxis2.setDrawAxisLine(false);
        yRAxis2.setDrawGridLines(false);
        CustomMarker3View mv2 = new CustomMarker3View(getApplicationContext(), R.layout.activity_custom_marker3_view);
        lineChart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_user_id = user_id + "Sad";
            }
        });
        lineChart2.setMarkerView(mv2);
        lineChart2.getAxisLeft().setAxisMaxValue(120);
        lineChart2.getAxisLeft().setDrawTopYLabelEntry(false);
        lineChart2.setData(data2);
        lineChart2.animateY(2000, Easing.EasingOption.EaseInCubic);
        //여기까지

        //놀람 그래프 설정
        ArrayList<Entry> entries3 = new ArrayList<>();
        DBHelper3 helper3 = new DBHelper3(getApplicationContext());
        final SQLiteDatabase db3 = helper3.getWritableDatabase();
        String sql3 = "SELECT * FROM T3;";
        Cursor cursor3 = db3.rawQuery(sql3, null);
        if (cursor3.getCount() > 0) {
            int tmp = 0;
            while (cursor3.moveToNext()) {
                if (cursor3.getString(0).equals(user_id+"Surprise")) {
                    entries3.add(new Entry((float) cursor3.getDouble(2), tmp));
                    tmp++;
                }
            }
        }
        LineDataSet dataset3 = new LineDataSet(entries3, ": 확률(%)");
        cursor3 = db3.rawQuery(sql3, null);
        ArrayList<String> labels3 = new ArrayList<String>();
        if (cursor3.getCount() > 0) {
            while (cursor3.moveToNext()) {
                if (cursor3.getString(0).equals(user_id+"Surprise")) {
                    labels3.add("");
                }
            }
        }
        dataset3.setValueTextColor(38938);
        LineData data3 = new LineData(labels3, dataset3);
        dataset3.setLineWidth(2);
        dataset3.setColors(Collections.singletonList(Color.BLUE));
        data3.setValueTextSize(20);
        XAxis xAxis3 = lineChart3.getXAxis();
        xAxis3.setTextColor(Color.BLACK);
        xAxis3.setTextSize(10);
        YAxis yLAxis3 = lineChart3.getAxisLeft();
        yLAxis3.setTextColor(Color.BLACK);
        yLAxis3.setTextSize(20);
        YAxis yRAxis3 = lineChart3.getAxisRight();
        yRAxis3.setDrawLabels(false);
        yRAxis3.setDrawAxisLine(false);
        yRAxis3.setDrawGridLines(false);
        CustomMarker3View mv3 = new CustomMarker3View(getApplicationContext(), R.layout.activity_custom_marker3_view);
        lineChart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_user_id = user_id + "Surprise";
            }
        });
        lineChart3.setMarkerView(mv3);
        lineChart3.getAxisLeft().setAxisMaxValue(120);
        lineChart3.getAxisLeft().setDrawTopYLabelEntry(false);
        lineChart3.setData(data3);
        lineChart3.animateY(2000, Easing.EasingOption.EaseInCubic);
        //여기까지

        //화남 그래프 설정
        ArrayList<Entry> entries4 = new ArrayList<>();
        DBHelper3 helper4 = new DBHelper3(getApplicationContext());
        final SQLiteDatabase db4 = helper4.getWritableDatabase();
        String sql4 = "SELECT * FROM T3;";
        Cursor cursor4 = db4.rawQuery(sql4, null);
        if (cursor4.getCount() > 0) {
            int tmp = 0;
            while (cursor4.moveToNext()) {
                if (cursor4.getString(0).equals(user_id+"Angry")) {
                    entries4.add(new Entry((float) cursor4.getDouble(2), tmp));
                    tmp++;
                }
            }
        }
        LineDataSet dataset4 = new LineDataSet(entries4, ": 확률(%)");
        cursor4 = db4.rawQuery(sql4, null);
        ArrayList<String> labels4 = new ArrayList<String>();
        if (cursor4.getCount() > 0) {
            while (cursor4.moveToNext()) {
                if (cursor4.getString(0).equals(user_id+"Angry")) {
                    labels4.add("");
                }
            }
        }
        dataset4.setValueTextColor(38938);
        LineData data4 = new LineData(labels4, dataset4);
        dataset4.setLineWidth(2);
        dataset4.setColors(Collections.singletonList(Color.BLUE));
        data4.setValueTextSize(20);
        XAxis xAxis4 = lineChart4.getXAxis();
        xAxis4.setTextColor(Color.BLACK);
        xAxis4.setTextSize(10);
        YAxis yLAxis4 = lineChart4.getAxisLeft();
        yLAxis4.setTextColor(Color.BLACK);
        yLAxis4.setTextSize(20);
        YAxis yRAxis4 = lineChart4.getAxisRight();
        yRAxis4.setDrawLabels(false);
        yRAxis4.setDrawAxisLine(false);
        yRAxis4.setDrawGridLines(false);
        CustomMarker3View mv4 = new CustomMarker3View(getApplicationContext(), R.layout.activity_custom_marker3_view);
        lineChart4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_user_id = user_id + "Angry";
            }
        });
        lineChart4.setMarkerView(mv4);
        lineChart4.getAxisLeft().setAxisMaxValue(120);
        lineChart4.getAxisLeft().setDrawTopYLabelEntry(false);
        lineChart4.setData(data4);
        lineChart4.animateY(2000, Easing.EasingOption.EaseInCubic);
        //여기까지
    }
    public void onClick(View V){ //버튼 클릭에 대한 이벤트 처리
        switch(V.getId()){
            case R.id.game3_back:
                Intent select = new Intent(V.getContext(), RecordActivity.class);
                select.putExtra("id", user_id);
                startActivity(select);
                break;
        }
    }
    //핸드폰 back버튼을 누르면 RecordActivity로 이동
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), RecordActivity.class);
        back.putExtra("id", user_id);
        startActivity(back);
    }
}