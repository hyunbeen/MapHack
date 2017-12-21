package com.example.kosta.maphack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kosta on 2017-12-11.
 */

//핸드폰 내부디비를 생성하는 함수
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Maphack.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //테이블 생성
        db.execSQL("CREATE TABLE login (_id INTEGER PRIMARY KEY AUTOINCREMENT, id TEXT);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS login");
        onCreate(db);
    }
}