package com.example.kosta.maphack;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kosta on 2017-12-06.
 */

public class TabHomeActivity extends Activity {

    DBHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

    ListView listView;
    TextView textView;

    String id;

    //TabHomeActivity 탭을 눌렀을 시 로그인 확인
    @Override
    protected void onResume() {
        super.onResume();
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

            listView = (ListView)findViewById(R.id.homelist);

            //데이터를 저장하게 되는 리스트
            List<String> list = new ArrayList<>();

            //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, list);

            //리스트뷰의 어댑터를 지정해준다.
            listView.setAdapter(adapter);

            //리스트뷰에 내용추가
            list.add("\n여행짜기 목록\n");
            list.add("\n여행후기 목록\n");
            list.add("\n즐겨찾기 목록\n");
            list.add("\n회원정보 수정\n");
            list.add("\n로그아웃\n");


            textView = (TextView)findViewById(R.id.idtext);
            textView.setText("'"+id+"'님 환영합니다♥");

            //각 리스트뷰 항목 클릭 시
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                    if(position == 0){
                        Intent intent = new Intent(TabHomeActivity.this, MyTravelActivity.class);
                        intent.putExtra("id", id);
                        TabHomeActivity.this.startActivity(intent);
                    }else if(position == 1){
                        Intent intent = new Intent(TabHomeActivity.this, MyTravelReviewActivity.class);
                        intent.putExtra("id", id);
                        TabHomeActivity.this.startActivity(intent);
                    }else if(position == 2){
                        Intent intent = new Intent(TabHomeActivity.this, MyTravelFavoriteActivity.class);
                        intent.putExtra("id", id);
                        TabHomeActivity.this.startActivity(intent);
                    }else if(position == 3){
                        Intent intent = new Intent(TabHomeActivity.this, MyTravelModifyActivity.class);
                        intent.putExtra("id", id);
                        TabHomeActivity.this.startActivity(intent);
                    }else if(position == 4){
                        //로그아웃을 눌렀을 시 내부 디비에 로그인 정보를 삭제한다.
                        db.execSQL("DELETE FROM login WHERE id='" + id + "';");
                        Toast.makeText(getApplicationContext(), "로그아웃완료", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(TabHomeActivity.this, MainActivity.class);

                        TabHomeActivity.this.startActivity(intent);
                    }
                }
            });

        }

    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabhome);



    }
}
