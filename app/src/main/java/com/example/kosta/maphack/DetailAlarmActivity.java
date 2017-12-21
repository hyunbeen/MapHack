package com.example.kosta.maphack;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.kosta.maphack.adapter.DetailAlarmListAdapter;
import com.example.kosta.maphack.model.DetailAlarmList;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;



//알람 세부내용을 확인하는 액티비티
public class DetailAlarmActivity extends Activity {

    Button button;
    ListView listView;
    List<DetailAlarmList> list;
    DetailAlarmListAdapter detailAlarmListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailalarm);

        button = (Button)findViewById(R.id.buttonback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.detailalarmlist);

        list = new ArrayList<DetailAlarmList>();

        detailAlarmListAdapter = new DetailAlarmListAdapter(DetailAlarmActivity.this, R.layout.layout_alarmdetail, list);

        listView.setAdapter(detailAlarmListAdapter);


        //TabAlarmActivity에서 값을 넘겨받음
        Intent intent = getIntent();
        int position = intent.getExtras().getInt("position");
        ArrayList<Integer> count = intent.getExtras().getIntegerArrayList("count");
        ArrayList<String> alarm = intent.getExtras().getStringArrayList("alarm");
        ArrayList<String> alarmtitle = intent.getExtras().getStringArrayList("alarmtitle");
        ArrayList<String> alarmdetailcontent = intent.getExtras().getStringArrayList("alarmdetailcontent");
        ArrayList<String> alarmdetailimage = intent.getExtras().getStringArrayList("alarmdetailimage");

        detailAlarmListAdapter.clear();

        //각 일정의 세부알람내용을 보여주기 위함
        int end = 0;
        for(int i=0; i <= position; i++){
            end += count.get(i);
        }
        int start =0;
        for(int i=0; i<=position-1; i++){
            start += count.get(i);
        }

        start = end-count.get(position);

        Log.d("start", ""+start);
        Log.d("end", ""+end);
        String y;
        String dm;
        String d;
        String h;
        String m;
        for(int i =start; i<end; i++){
            StringTokenizer stringTokenizer = new StringTokenizer(alarm.get(i), "-");
            y = stringTokenizer.nextToken();
            dm = stringTokenizer.nextToken();
            d = stringTokenizer.nextToken();
            h = stringTokenizer.nextToken();
            m = stringTokenizer.nextToken();
            String datetime2 = y+"."+dm+"."+d+" "+h+":"+m+" 알람예정";


            //리스트에 추가
            detailAlarmListAdapter.add(new DetailAlarmList(alarmtitle.get(i), alarmdetailcontent.get(i), datetime2, alarmdetailimage.get(i), true));

        }





    }

}
