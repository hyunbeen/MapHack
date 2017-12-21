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
import com.example.kosta.maphack.adapter.TravelAdapter;

import com.example.kosta.maphack.model.After;
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

public class MyTravelReviewActivity extends Activity {
    List<JSONObject> travelList = new ArrayList();
    String id = "";

    GridViewWithHeaderAndFooter lv;

    List<After> list;
    MyAfterAdapter adapter;
    Button btnNext;

    LinearLayout linearLayout;
    WebView webView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytravelreview);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        //서버 요청 보내기
        MyTravelReviewActivity.NetworkTask networkTask = new  MyTravelReviewActivity.NetworkTask();

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);


        networkTask.execute(params);







        lv=(GridViewWithHeaderAndFooter) findViewById(R.id.listviewAfter);

        list = new ArrayList();

        adapter = new MyAfterAdapter(this, R.layout.activity_myafteritem, list);



        lv.setAdapter(adapter);

        linearLayout = (LinearLayout)findViewById(R.id.linearlayoutafterdetail);
        webView = (WebView)findViewById(R.id.webViewafter);

        lv.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);


        //리스트 상세보기 이벤트
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                After item = adapter.getItem(i);
                //상세보기 웹에서 가져오기
                webView.loadUrl("http://192.168.0.188:8080/MapHack/after.jsp?id="+item.getAft_id());

            }
        });








    }

    //adapter 데이터 추가하기
    private void addData() {
        JSONArray jrr = new JSONArray();
        JSONObject jobj = new JSONObject();
        String mainimg = "";
        String title = "";
        String id = "";
        try {
            //json list 에서 데이터 나눠서 넣기
            for(int i=0;i<travelList.size();i++){

                jrr = travelList.get(i).getJSONArray("imageList");
                if(jrr.length()==0){
                    mainimg = "";
                }else{
                    jobj = (JSONObject) jrr.get(0);
                    mainimg = valueOf(jobj.get("fileName"));
                }


                title = valueOf(travelList.get(i).get("title"));
                id = valueOf(travelList.get(i).get("_id"));
                adapter.add(new After(mainimg,title,id));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }






    }

    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

// Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.0.188:8080/MapHack/androidMyAfterList.mh");

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

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.d("나의여행후기",s);
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