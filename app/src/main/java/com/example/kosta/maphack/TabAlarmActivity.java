package com.example.kosta.maphack;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kosta.maphack.adapter.AlarmListAdapter;
import com.example.kosta.maphack.adapter.MapSearchAdapter;
import com.example.kosta.maphack.model.AlarmList;
import com.example.kosta.maphack.model.MapSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by kosta on 2017-12-06.
 */

public class TabAlarmActivity extends Activity {
    ListView listView;
    List<AlarmList> list;
    AlarmListAdapter alarmListAdapter;

    List<String> alarm = new ArrayList<>();

    DBHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

    String id ;
    String responseData;

    String alarmlist;
    String detaillist[];
    String totaltitle[];

    int count;
    int countlist[];

    String stYear[];
    String stMonth[];
    String stDay[];
    String stHour[];
    String stMinute[];

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

            alarm.clear();

            TourTask tourTask = new TourTask();

            Map<String, String> params = new HashMap<String, String>();
            Log.d("id123123", id);
            params.put("id", id);

            tourTask.execute(params);

        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabalarm);



    }
    public class AlarmHATT {
        private Context context;

        public AlarmHATT(Context context) {

            this.context = context;
        }

        public void Alarm(int count) {
            // Log.d("븐", ""+a);
            PendingIntent[] sender = new PendingIntent[count];

            for(int i=0; i < count; i++){
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(TabAlarmActivity.this, BroadcastD.class);
                sender[i] = PendingIntent.getBroadcast(TabAlarmActivity.this, i, intent, 0);
                Calendar calendar = Calendar.getInstance();
                //알람시간 calendar에 set해주기

                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 17, i+36, 0);
                Log.d("calendar", calendar.get(Calendar.MONTH)+ ";"+calendar.get(Calendar.DATE));
                //알람 예약
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender[i]);
            }


        }
    }
    public class TourTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

// Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.0.100:8080/MapHack/androidalarm.mh");

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
            responseData = s;

            Log.d("와랏", s);



            String alarmtime[];
            String title[];

            try {
                JSONObject object = new JSONObject(responseData);
                alarmlist = object.getString("travelmaplist");
                Log.d("list", alarmlist);

                JSONArray array = new JSONArray(alarmlist);

                detaillist = new String[array.length()];
                totaltitle = new String[array.length()];

                for(int i=0; i<array.length(); i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    detaillist[i] = jsonObject.getString("dayList");
                    totaltitle[i] = jsonObject.getString("map_title");
                    Log.d("dayList", detaillist[i]);
                    Log.d("map_title", totaltitle[i]);
                }

                countlist = new int[detaillist.length];

                for (int i=0; i<detaillist.length; i++){
                    count = 0;
                    Log.d("길이", ""+detaillist.length);
                    JSONArray array1 = new JSONArray(detaillist[i]);
                    for(int j=0; j<array1.length(); j++){
                        JSONObject jsonObject = array1.getJSONObject(j);
                        jsonObject.getString("markList");
                        JSONArray jsonArray = jsonObject.getJSONArray("markList");
                        for(int k=0; k<jsonArray.length(); k++){

                            JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                            Log.d("알람", i+" "+k+" "+jsonObject1.getString("time"));
                            alarm.add(jsonObject1.getString("time"));
                            count++;
                        }
                    }
                    Log.d("count", ""+count);
                    countlist[i] = count;
                    Log.d("countlist", ""+countlist[i]);
                }
                for(int i=0; i<alarm.size(); i++){
                    Log.d("알람", alarm.get(i));
                }


                listView = (ListView)findViewById(R.id.alarmlist);

                list = new ArrayList<AlarmList>();
                alarmListAdapter = new AlarmListAdapter(TabAlarmActivity.this, R.layout.layout_alarmlist, list);

                listView.setAdapter(alarmListAdapter);



                for (int i=0; i<totaltitle.length; i++){
                    alarmListAdapter.add(new AlarmList(totaltitle[i], "등록된 알림 "+countlist[i]+" 개", R.drawable.alarm));
                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        Log.d("position", ""+position);
                    }
                });


                stYear = new String[alarm.size()];
                stMonth = new String[alarm.size()];
                stDay = new String[alarm.size()];
                stHour = new String[alarm.size()];
                stMinute = new String[alarm.size()];

                for(int i=0; i< alarm.size(); i++){
                    StringTokenizer stringTokenizer = new StringTokenizer(alarm.get(i), "-");
                    if(stringTokenizer.hasMoreTokens()){
                        stYear[i] = stringTokenizer.nextToken();
                        stMonth[i] = stringTokenizer.nextToken();
                        stDay[i] = stringTokenizer.nextToken();
                        stHour[i] = stringTokenizer.nextToken();
                        stMinute[i] = stringTokenizer.nextToken();
                    }
                    Log.d("날짜", stYear[i] + stMonth[i] + stDay[i]);
                }
                //new AlarmHATT(getApplicationContext()).Alarm(alarm.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
