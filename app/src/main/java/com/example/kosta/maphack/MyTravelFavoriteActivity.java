package com.example.kosta.maphack;

import android.app.Activity;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.LinearLayout;


import com.example.kosta.maphack.adapter.MyAfterAdapter;
import com.example.kosta.maphack.adapter.MyFavorAdapter;
import com.example.kosta.maphack.adapter.TravelAdapter;

import com.example.kosta.maphack.model.After;
import com.example.kosta.maphack.model.Favor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import in.srain.cube.views.GridViewWithHeaderAndFooter;

import static java.lang.String.valueOf;

/**
 * Created by kosta on 2017-12-11.
 */

public class MyTravelFavoriteActivity extends Activity {
    List<JSONObject> travelList = new ArrayList();
    String id = "";

    GridViewWithHeaderAndFooter lv;

    List<Favor> list;
    MyFavorAdapter adapter;


    LinearLayout linearLayout;
    WebView webView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytravelfavorite);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        //서버 요청 보내기
        MyTravelFavoriteActivity.NetworkTask networkTask = new  MyTravelFavoriteActivity.NetworkTask();

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);


        networkTask.execute(params);







        lv=(GridViewWithHeaderAndFooter) findViewById(R.id.listviewFavor);

        list = new ArrayList();
        //어댑터 설정하기
        adapter = new MyFavorAdapter(this, R.layout.activity_myfavoritem, list);


        //리스트뷰 설정하기
        lv.setAdapter(adapter);



        lv.setVisibility(View.VISIBLE);














    }

    //어댑터에 데이터 추가하기
    private void addData() {
        JSONArray jrr = new JSONArray();
        JSONObject jobj = new JSONObject();
        String mainimg = "";
        String title = "";
        String id = "";
        Log.d("size", String.valueOf(travelList.size()));
        //json 리스트의 값을 분류하여 어댑터 데어터넣기
        try {

            for(int i=0;i<travelList.size();i++){




                title = valueOf(travelList.get(i).get("title"));
                id = "";
                mainimg = valueOf(travelList.get(i).get("firstimage"));
                adapter.add(new Favor(mainimg,title,id));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }






    }

    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

// Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.0.188:8080/MapHack/androidMyFavorList.mh");

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
        //응답 스트링을 json 형태로 바꾸기
        @Override
        protected void onPostExecute(String s) {
            try {

                JSONObject obj = new JSONObject(s);

                JSONArray jar   = obj.getJSONArray("result");

                for(int i=0;i<jar.length();i++){

                    JSONObject objdetail = jar.getJSONObject(i);
                    travelList.add(objdetail);

                }
                addData();


            } catch (JSONException e) {

            }


        }
    }


}