package com.example.kosta.maphack;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;


import com.example.kosta.maphack.adapter.AfterAdapter;
import com.example.kosta.maphack.model.After;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

import static java.lang.String.valueOf;

/**
 * Created by kosta on 2017-12-06.
 */

public class TabListActivity extends Activity{
    List<JSONObject> travelList = new ArrayList();
    ViewPager viewPager;

    GridViewWithHeaderAndFooter lv;

    List<After> list;
    AfterAdapter adapter;
    Button btnNext;

    LinearLayout linearLayout;
    WebView webView;
    Button btnBack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablist);

        //서버 요청 보내기
        NetworkTask networkTask = new NetworkTask();

        Map<String, String> params = new HashMap<String, String>();
        //파라미터 json 형태로 보내기
        params.put("title", "여행 계획 리스트 불러오기");
        params.put("memo", "4개만 불러온다");

        networkTask.execute(params);

        //아이디 설정
        list = new ArrayList<After>();
        lv=(GridViewWithHeaderAndFooter) findViewById(R.id.listview);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        webView = (WebView) findViewById(R.id.webView);



        btnBack = (Button)findViewById(R.id.btnBack);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        Timer timer = new Timer();

        //슬라이더 시간 설정
        timer.scheduleAtFixedRate(new MyTimerTask(),10000,10000);





        //어댑터 설정
        adapter = new AfterAdapter(this, R.layout.activity_afteritem, list);


        //리스트 뷰에 어댑터 설정
        lv.setAdapter(adapter);

        lv.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);


        //상세 보기 이벤트
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                After item = adapter.getItem(i);
                //web의 상세보기를 가져온다
                webView.loadUrl("http://192.168.0.188:8080/MapHack/after.jsp?id="+item.getAft_id());

            }

        });





        //뒤로가기 이벤트
        //여행후기 리스트를 다시 보여준다
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lv.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }
        });


    }


    //슬라이더 클래스
    //슬라이더의 설정을 관리한다
    public class MyTimerTask extends TimerTask{
        //view page 넘어가기 이벤트
        @Override
        public void run() {
            TabListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    }else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    }else{
                        viewPager.setCurrentItem(0);
                    }
                }
            });

        }
    }


    //여행후기 리스트를 더하는 이벤트
    private void addData() {
        JSONArray jrr = new JSONArray();
        JSONObject jobj = new JSONObject();
        String mainimg = ""; //메인 이미지
        String title = ""; //메인 타이틀
        String id = ""; //기본키 아이디
        try {
            for(int i=0;i<travelList.size();i++){
                jrr = travelList.get(i).getJSONArray("imageList");
                //추가한 이미지가 있을시 첫번째 이미지가 메인 이미지

                if(jrr.length()==0){
                    mainimg = "";
                }else{
                    jobj = (JSONObject) jrr.get(0);
                    mainimg = valueOf(jobj.get("fileName"));
                }


                title = valueOf(travelList.get(i).get("title")); //타이틀 설정
                id = valueOf(travelList.get(i).get("_id"));//아이디 설정
                adapter.add(new After(mainimg,title,id));//어댑터 추가
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    public class NetworkTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

// Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.0.188:8080/MapHack/tabList.mh");

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
        //응답을 json 형태로 분리
        @Override
        protected void onPostExecute(String s) {
            try {

                //String 형태에서 json 형태로 가져오기
                JSONObject obj = new JSONObject(s);

                //결과값 가져오기
                JSONArray jar   = obj.getJSONArray("result");

                //결과값을 adapter에 내용추가를 위해 사용할 리스트에 추가하기
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