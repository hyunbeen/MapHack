package com.example.kosta.maphack;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kosta on 2017-12-06.
 */

public class TabHomeActivity extends Activity {

    DBHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

    Button btnlogout;

    String id;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("다시시작해랏", "ㅁㅈㄷㄹ");
        helper = new DBHelper(this);

        try{
            db = helper.getWritableDatabase();
        }
        catch (SQLiteException e){
            db = helper.getReadableDatabase();
        }

        cursor = db.rawQuery("SELECT * FROM login;",null);

        if(cursor.moveToNext() == false){

            Intent intent = new Intent(this, LoginActivity.class);

            this.startActivity(intent);
        }else{

            id = cursor.getString(1);
            Log.d("id", id);
        }

    }


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//                Log.d("다시시작해랏123123", "ㅁㅈㄷㄹ123");
//        helper = new DBHelper(this);
//
//        try{
//            db = helper.getWritableDatabase();
//        }
//        catch (SQLiteException e){
//            db = helper.getReadableDatabase();
//        }
//
//        cursor = db.rawQuery("SELECT * FROM login;",null);
//
//        if(cursor.moveToNext() == false){
//
//            Intent intent = new Intent(this, LoginActivity.class);
//
//            this.startActivity(intent);
//        }else{
//            id = cursor.getString(1);
//            Log.d("id", id);
//        }
//
//    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabhome);

//        helper = new DBHelper(this);
//
//        try{
//            db = helper.getWritableDatabase();
//        }
//        catch (SQLiteException e){
//            db = helper.getReadableDatabase();
//        }
//
//        cursor = db.rawQuery("SELECT * FROM login;",null);
//
//        if(cursor.moveToNext() == false){
//
//            Intent intent = new Intent(this, LoginActivity.class);
//
//            this.startActivity(intent);
//        }else{
//            id = cursor.getString(1);
//            Log.d("id", id);
//        }

        btnlogout = (Button)findViewById(R.id.btnlogout);

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.execSQL("DELETE FROM login WHERE id='" + id + "';");
                Toast.makeText(getApplicationContext(), "로그아웃완료", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(TabHomeActivity.this, MainActivity.class);

                TabHomeActivity.this.startActivity(intent);
            }
        });

    }
}
