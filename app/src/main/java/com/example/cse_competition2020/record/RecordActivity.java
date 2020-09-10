package com.example.cse_competition2020.record;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse_competition2020.MainActivity;
import com.example.cse_competition2020.R;
import com.example.cse_competition2020.db.DBHelper1;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent intent = getIntent(); //StartActivity에서 id가 넘어옴
        user_id = intent.getExtras().getString("id");

        LineChart lineChart = (LineChart) findViewById(R.id.chart1);

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
        dataset.setColors(Collections.singletonList(Color.MAGENTA));
        data.setValueTextSize(20);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(10);
        //xAxis.setAxisLineWidth(10);
        //xAxis.setPosition(XAxis.XAxisPosition.TOP);
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.WHITE);
        yLAxis.setTextSize(20);
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        CustomMarkerView mv = new CustomMarkerView(getApplicationContext(), R.layout.activity_custom_marker_view);
        lineChart.setMarkerView(mv);
        //lineChart.setExtraLeftOffset(8f);
        lineChart.getAxisLeft().setAxisMaxValue(6);
        lineChart.getAxisLeft().setDrawTopYLabelEntry(false);
        lineChart.setData(data);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
    }

    public void onClick(View V){ //버튼 클릭에 대한 이벤트 처리
        switch(V.getId()){
            case R.id.record_back_button:
                Intent select = new Intent(V.getContext(),MainActivity.class);
                select.putExtra("id", user_id);
                startActivity(select);
                break;
        }
    }
    //핸드폰 back버튼을 누르면 StartActivity로 이동
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        back.putExtra("id", user_id);
        startActivity(back);
    }
}