package com.example.cse_competition2020.record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cse_competition2020.R;
import com.example.cse_competition2020.db.DBHelper1;
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
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        RecordActivity a = new RecordActivity();
        DBHelper1 helper = new DBHelper1(getContext());
        final SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT * FROM T1;";
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

    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}