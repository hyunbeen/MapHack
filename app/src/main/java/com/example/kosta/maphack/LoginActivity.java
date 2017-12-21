package com.example.kosta.maphack;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kosta on 2017-12-11.
 */

public class LoginActivity extends Activity {
    DBHelper helper;
    SQLiteDatabase db;
    Button btnlogin, btnmemberadd, btnpassword, btnenter;
    EditText editText, editText1;
    String id;

    String responseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toast.makeText(getApplicationContext(), "로그인 후 이용가능합니다.", Toast.LENGTH_SHORT).show();


        //안드로이드 내부디비를 사용하기 위함
        helper = new DBHelper(this);

        try{
            db = helper.getWritableDatabase();
        }
        catch (SQLiteException e){
            db = helper.getReadableDatabase();
        }


        editText = (EditText)findViewById(R.id.EditID);
        editText1 = (EditText)findViewById(R.id.EditPW);
        btnlogin = (Button)findViewById(R.id.btnlogin);
        btnmemberadd= (Button)findViewById(R.id.btnmemberadd);
        btnpassword = (Button)findViewById(R.id.btnpassword);
        btnenter = (Button)findViewById(R.id.btnenter);

        //로그인 버튼을 클릭했을 시
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TourTask tourTask = new TourTask();

                //서버에 아이디와 패스워드 값을 보내 확인
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", editText.getText().toString());
                params.put("pw", editText1.getText().toString());

                tourTask.execute(params);
            }
        });
        //로그인없이 둘러보기 버튼 클릭 시 메인화면 호출
        btnenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                LoginActivity.this.startActivity(intent);
            }
        });


    }
    //서버에 로그인 아이디, 패스워드 일치 확인요청
    public class TourTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

// Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.0.188:8080/MapHack/androidloginconfirm.mh");

// Parameter 를 전송한다.

            http.addAllParameters(maps[0]);


//Http 요청 전송
            HttpClient post = http.create();
            post.request();

// 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

// 응답 본문 가져오기
            String body = post.getBody();

            return body;
        }

        //확인 값 받음
        @Override
        protected void onPostExecute(String s) {
            responseData = s;
            Log.d("뭐냐", s);

            id = editText.getText().toString();

            int confirm = Integer.parseInt(responseData);

            //confirm이 0보다 크면 아이디와 비밀번호 일치함, LoginActivity 종료
            if(confirm > 0){
                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                //내부디비에 아이디 저장
                db.execSQL("INSERT INTO login VALUES (null,'"+id+"');");
                finish();
            }else{
                //일치 하지않을 시 메시지 호출
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
            }


        }
    }

}
