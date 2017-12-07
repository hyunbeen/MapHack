package com.example.kosta.maphack;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kosta.maphack.adapter.AfterAdapter;
import com.example.kosta.maphack.model.After;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kosta on 2017-12-06.
 */

public class TabListActivity extends Activity {
    ViewPager viewPager;

    ListView lv;

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


       lv=(ListView)findViewById(R.id.listview);

        list = new ArrayList<After>();

        adapter = new AfterAdapter(this, R.layout.activity_afteritem, list);
        //footer
        View footerView = getLayoutInflater().inflate(R.layout.activity_tablistfooter,null);
        lv.addFooterView(footerView);
        btnNext = (Button)footerView.findViewById(R.id.btnNext);

        lv.setAdapter(adapter);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        webView = (WebView)findViewById(R.id.webView);
        btnBack = (Button)findViewById(R.id.btnBack);
        lv.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"헬로 길게", Toast.LENGTH_LONG).show();


                After item = adapter.getItem(i);
                webView.setTag(item.getAft_html());
                lv.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

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

    /*private void addData() {
        //adapter.add(new Flower("제목", "내용", R.drawable.flower01, false));
        adapter.add(new Flower("고구마꽃",
                "행운", R.drawable.flower01, false));
        adapter.add(new Flower("국화꽃",
                "성실, 청초, 고상함 [빨강,자홍색]사랑, [흰색]순결 [황색]질투", R.drawable.flower02, false));
        adapter.add(new Flower("대나무꽃",
                "지조, 인내, 절개, 절정", R.drawable.flower03, false));
        adapter.add(new Flower("동자꽃",
                "기다림", R.drawable.flower04, false));
        adapter.add(new Flower("백합꽃",
                "[빨간색] 열정, 깨끗함, [주황색] 명랑한 사랑, [노란색] 유쾌, [분홍색] 사랑의 맹세, [흰색] 순수한 사랑, 순결",
                R.drawable.flower05, false));
        adapter.add(new Flower("소나무꽃",
                "불로장수, 불로장생, 용감", R.drawable.flower06, false));
        adapter.add(new Flower("솔체꽃",
                "이루어 질 수없는 사랑", R.drawable.flower07, false));
        adapter.add(new Flower("양귀비꽃",
                "[빨강]위로, [흰색]망각", R.drawable.flower08, false));
        adapter.add(new Flower("은방울꽃",
                "섬세함", R.drawable.flower09, false));
        adapter.add(new Flower("장미",
                "[노랑색]질투, 완벽한 성취, [흰색] 순결, 순진, 매력, [빨강색] 욕망, 열정, 기쁨, [파랑색] 기적, [핑크색] 맹세, 행복한 사랑",
                R.drawable.flower10, false));
        adapter.add(new Flower("찔레꽃",
                "고독, 신중한 사랑, 가족에 대한 그리움", R.drawable.flower11, false));
        adapter.add(new Flower("투구꽃",
                "밤의 열림", R.drawable.flower12, false));
        adapter.add(new Flower("해바라기",
                "애모, 아름다운 빛, 그리움, 기다림, 숭배", R.drawable.flower13, false));
    }*/
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
            Log.d("JSON_REUSLT", s);
        }
    }

}
