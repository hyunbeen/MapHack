package com.example.kosta.maphack;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.kosta.maphack.adapter.TravelAdapter;

import com.example.kosta.maphack.model.Travel;

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

public class MyTravelActivity extends Activity {
    List<JSONObject> travelList = new ArrayList();
    String id = "";

    GridViewWithHeaderAndFooter lv;

    List<Travel> list;
    TravelAdapter adapter;
    Button btnNext;

    LinearLayout linearLayout;
    WebView webView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytravel);

        Intent intent = getIntent();
        //로그인 아이디 받아오기
        id = intent.getExtras().getString("id");
        //서버 요청 보내기
        MyTravelActivity.NetworkTask networkTask = new  MyTravelActivity.NetworkTask();

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);

        //요청 데이터 보내기
        networkTask.execute(params);







        lv=(GridViewWithHeaderAndFooter) findViewById(R.id.listviewTravel);

        list = new ArrayList<Travel>();
        //어댑터 설정
        adapter = new TravelAdapter(this, R.layout.activity_travelitem, list);


        //list에 어댑터 설정
        lv.setAdapter(adapter);

        linearLayout = (LinearLayout)findViewById(R.id.linearlayouttraveldetail);
        webView = (WebView)findViewById(R.id.webViewtravel);

        lv.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);


        //listview 클릭시 상세보기 이벤트
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //상세보기 웹에서 가져오기
                Travel item = adapter.getItem(i);
                webView.loadUrl("http://192.168.0.188:8080/MapHack/map.jsp?id="+item.getTravel_id());

            }
        });









    }

    //adapter에 데이터 추가
    private void addData() {
        JSONArray jrr = new JSONArray();
        JSONObject jobj = new JSONObject();
        String mainimg = "";
        String title = "";
        String id = "";
        try {
            for(int i=0;i<travelList.size();i++){


                //json list에서 값 설정하기
                mainimg = valueOf(travelList.get(i).get("map_fname"));
                title = valueOf(travelList.get(i).get("map_title"));
                id = valueOf(travelList.get(i).get("_id"));
                adapter.add(new Travel(mainimg,title,id));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

// Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.0.188:8080/MapHack/mapList.mh");

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
        //요청데이터 json 형태로 바꾸기
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