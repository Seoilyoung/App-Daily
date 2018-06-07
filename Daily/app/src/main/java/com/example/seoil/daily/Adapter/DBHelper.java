package com.example.seoil.daily.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public DBHelper(Context context) {
        super(context, "dailyDB.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String dailySQL = "create table tb_daily" + "(_id integer primary key autoincrement," + "content," + "finish integer)";
        sqLiteDatabase.execSQL(dailySQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i1 == DATABASE_VERSION){
            sqLiteDatabase.execSQL("drop table tb_daily");
            onCreate(sqLiteDatabase);
        }
    }
}