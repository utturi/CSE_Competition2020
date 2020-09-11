package com.example.cse_competition2020.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//눈 마주치기 게임(1번 게임)에 대한 데이터베이스
public class DBHelper2 extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public DBHelper2(Context context) {
        super(context, "t2.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE T2 (id TEXT, date TEXT, score DOUBLE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS T2");
        onCreate(db);
    }
}