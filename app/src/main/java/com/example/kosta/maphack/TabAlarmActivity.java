package com.example.kosta.maphack;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.kosta.maphack.adapter.MapSearchAdapter;
import com.example.kosta.maphack.model.MapSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kosta on 2017-12-06.
 */

public class TabAlarmActivity extends Activity {
    ListView listView;
    List<MapSearch> list;
    MapSearchAdapter mapSearchAdapter;

    DBHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabalarm);


        listView = (ListView)findViewById(R.id.alarmlist);

        list = new ArrayList<MapSearch>();
        mapSearchAdapter = new MapSearchAdapter(this, R.layout.layout_mapsearch, list);

        listView.setAdapter(mapSearchAdapter);


    }
}
