package com.example.cse_competition2020.record;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.example.cse_competition2020.R;
import com.example.cse_competition2020.db.DBHelper3;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class CustomMarker3View extends MarkerView {

    private TextView tvContent3;
    public CustomMarker3View(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent3 = (TextView) findViewById(R.id.tvContent3);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)

    //그래프안에 점 클릭시 2020-01-11 날짜 나타나게하는 함수
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Game3RecordActivity a = new Game3RecordActivity();

        DBHelper3 helper = new DBHelper3(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT * FROM T3;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            int tmp = 0;
            while (cursor.moveToNext()) {
                 if (cursor.getString(0).equals(a.new_user_id)) {
                    if(tmp == e.getXIndex()) {
                        tvContent3.setText("" + cursor.getString(1));
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