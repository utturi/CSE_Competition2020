package com.example.cse_competition2020.record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cse_competition2020.R;
import com.example.cse_competition2020.db.DBHelper1;
import com.example.cse_competition2020.db.DBHelper2;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    public CustomMarkerView (Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)

    //그래프안에 점 클릭시 2020-01-11 날짜 나타나게하는 함수
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        RecordActivity a = new RecordActivity();
        SQLiteDatabase db = null;
        String sql = null;
        if(a.game == 1){
            DBHelper2 helper = new DBHelper2(getContext());
            db = helper.getWritableDatabase();
            sql = "SELECT * FROM T2;";
        }
        else if(a.game == 2){
            DBHelper1 helper = new DBHelper1(getContext());
            db = helper.getWritableDatabase();
            sql = "SELECT * FROM T1;";
        }
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            int tmp = 0;
            while (cursor.moveToNext()) {
                if (cursor.getString(0).equals(a.user_id)) {
                    if(tmp == e.getXIndex()) {
                        tvContent.setText("" + cursor.getString(1));
                        break;
                    }
                    tmp++;
                }
            }
        }
    }

    //점 클릭시 x좌표
    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    //점 클릭시 y좌표
    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}