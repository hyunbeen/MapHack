package com.example.kosta.maphack;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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

public class TabListActivity extends Activity {
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
        params.put("title", "메모입니다.");
        params.put("memo", "메모를 입력했습니다.");

        networkTask.execute(params);




        viewPager = (ViewPager)findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new MyTimerTask(),10000,10000);


        lv=(GridViewWithHeaderAndFooter) findViewById(R.id.listview);

        list = new ArrayList<After>();

        adapter = new AfterAdapter(this, R.layout.activity_afteritem, list);



        lv.setAdapter(adapter);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        webView = (WebView)findViewById(R.id.webView);
        btnBack = (Button)findViewById(R.id.btnBack);
        lv.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                After item = adapter.getItem(i);

                lv.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                webView.setHorizontalScrollBarEnabled(false);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl("http://192.168.0.121:8080/MapHack/afterDetailed.mh?id="+item.getAft_id());

            }
        });






        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lv.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }
        });


    }
    public class MyTimerTask extends TimerTask{

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

    private void addData() {
        JSONArray jrr = new JSONArray();
        JSONObject jobj = new JSONObject();
        String mainimg = "";
        String title = "";
        String id = "";
        try {
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
                Log.d("TRAVEL_TITLE",title);
                Log.d("TRAVEL_MAINIMG",mainimg);
                Log.d("TRAVEL_id",id);
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
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.0.121:8080/MapHack/tabList.mh");

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

                Log.d("초기시작",s);
                JSONObject obj = new JSONObject(s);

                JSONArray jar   = obj.getJSONArray("result");
                Log.d("길이", String.valueOf(jar.length()));
                for(int i=0;i<jar.length();i++){
                    Log.d("들어감","들어감");
                    JSONObject objdetail = jar.getJSONObject(i);
                    travelList.add(objdetail);

                }
                addData();
            } catch (JSONException e) {

            }


        }
    }


}