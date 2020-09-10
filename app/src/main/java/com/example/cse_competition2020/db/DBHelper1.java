package com.example.cse_competition2020.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//id, 날짜, 점수를 저장하는 테이블
public class DBHelper1 extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public DBHelper1(Context context) {
        super(context, "t1.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE T1 (id TEXT, date TEXT, score INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS T1");
        onCreate(db);
    }
}