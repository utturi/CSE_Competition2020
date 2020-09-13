package com.example.cse_competition2020.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//game3 표정따라하기 db table
public class DBHelper3 extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public DBHelper3(Context context) {
        super(context, "t3.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE T3 (id TEXT, date TEXT, per DOUBLE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS T3");
        onCreate(db);
    }
}