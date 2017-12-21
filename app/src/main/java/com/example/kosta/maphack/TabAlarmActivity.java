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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by kosta on 2017-12-06.
 */


//예정된 알림 보여주는 화면
public class TabAlarmActivity extends Activity {
    ListView listView;
    List<AlarmList> list;
    AlarmListAdapter alarmListAdapter;

    List<String> alarm = new ArrayList<>();
    List<String> alarmtitle = new ArrayList<>();
    List<String> alarmdetailcontent = new ArrayList<>();
    List<String> alarmdetailimage = new ArrayList<>();
    List<Integer> alarmdetailcount = new ArrayList<>();


    DBHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

    String id ;
    String responseData;

    String alarmlist;
    String detaillist[];
    String totaltitle[];
    String totalimage[];

    int count;
    int countlist[];

    int stYear[];
    int stMonth[];
    int stDay[];
    int stHour[];
    int stMinute[];

    String inputtitle[];

    //로그인 체크를 하기위해 알람탭 클릭 시 요청
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

        //내부디비에 아이디가 있는지 확인
        cursor = db.rawQuery("SELECT * FROM login;",null);

        //등록된 아이디가 없다면 로그인액티비티 호출
        if(cursor.moveToNext() == false){

            Intent intent = new Intent(this, LoginActivity.class);

            this.startActivity(intent);
        }else{
            //등록된 아이디가 없다면

            //내부디비에 저장된 아이디값 가져오기
            id = cursor.getString(1);
            Log.d("id", id);


            //저장된 배열 초기화(값이 쌓이기 때문)
            alarm.clear();
            alarmtitle.clear();


            //알람 예정 리스트 출력을 위해 서버에서 값 받아오기
            TourTask tourTask = new TourTask();

            Map<String, String> params = new HashMap<String, String>();
            //서버로 id값을 보냄
            params.put("id", id);

            tourTask.execute(params);

        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabalarm);



    }

    //서버로 가서 값 가져오기
    public class TourTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

// Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.0.188:8080/MapHack/androidalarm.mh");

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
            //요청값을 담음
            responseData = s;


            String alarmtime[];
            String title[];

            //JSON형태로 가져온 것들을 String배열에 담음
            try {
                JSONObject object = new JSONObject(responseData);
                alarmlist = object.getString("travelmaplist");
                Log.d("list", alarmlist);

                JSONArray array = new JSONArray(alarmlist);

                detaillist = new String[array.length()];
                totaltitle = new String[array.length()];
                totalimage = new String[array.length()];

                for(int i=0; i<array.length(); i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    detaillist[i] = jsonObject.getString("dayList");
                    totaltitle[i] = jsonObject.getString("map_title");
                    totalimage[i] = jsonObject.getString("map_fname");
                    Log.d("dayList", detaillist[i]);
                    Log.d("map_title", totaltitle[i]);
                }

                //이미지를 호출하기위한 url을 만들기 위함
                for(int i=0; i<totalimage.length; i++){
                    totalimage[i] = "http://192.168.0.188:8080"+totalimage[i];
                    Log.d("totalimage", totalimage[i]);
                }
                countlist = new int[detaillist.length];


                //현재시간을 가져오기 위함
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");

                String datetime1 = sdf1.format(calendar.getTime());


                //현재시간을 기점으로 후에 울릴 알람만을 보여주기 위함
                for (int i=0; i<detaillist.length; i++){
                    count = 0;

                    JSONArray array1 = new JSONArray(detaillist[i]);
                    for(int j=0; j<array1.length(); j++){
                        JSONObject jsonObject = array1.getJSONObject(j);
                        jsonObject.getString("markList");
                        JSONArray jsonArray = jsonObject.getJSONArray("markList");
                        for(int k=0; k<jsonArray.length(); k++){

                            JSONObject jsonObject1 = jsonArray.getJSONObject(k);

                            String y;
                            String dm;
                            String d;
                            String h;
                            String m;
                            String datetime2= "0";
                            StringTokenizer stringTokenizer = new StringTokenizer(jsonObject1.getString("time"), "-");
                                if(stringTokenizer.hasMoreTokens()){
                                    y = stringTokenizer.nextToken();
                                    dm = stringTokenizer.nextToken();
                                    d = stringTokenizer.nextToken();
                                    h = stringTokenizer.nextToken();
                                    m = stringTokenizer.nextToken();
                                    datetime2 = "";
                                    datetime2 = y+dm+d+h+m;
                                }


                            //int형의 범위를 벗어나므로 long형 사용

                            if (Long.parseLong(datetime1) < Long.parseLong(datetime2)){

                                alarm.add(jsonObject1.getString("time"));
                                alarmtitle.add(jsonObject1.getString("title"));
                                alarmdetailcontent.add(jsonObject1.getString("content"));
                                alarmdetailimage.add(jsonObject1.getString("firstimage"));
                                count++;

                            }

                        }
                    }
                    Log.d("count", ""+count);
                    countlist[i] = count;
                    Log.d("countlist", ""+countlist[i]);

                }

                listView = (ListView)findViewById(R.id.alarmlist);

                list = new ArrayList<AlarmList>();
                alarmListAdapter = new AlarmListAdapter(TabAlarmActivity.this, R.layout.layout_alarmlist, list);

                listView.setAdapter(alarmListAdapter);

                alarmdetailcount.clear();

                //한 일정에 관해 등록된 일정이 몇개인지 보여줌
                for (int i=0; i<totaltitle.length; i++){
                    if(countlist[i] != 0){
                        alarmListAdapter.add(new AlarmList(totaltitle[i], "등록된 알림 "+countlist[i]+" 개", totalimage[i]));
                        alarmdetailcount.add(countlist[i]);

                    }

                }
                //리스트뷰에 항목을 클릭 시 해당 항목에 알림 갯수에 대한 세부알림일정을 보여줌
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        Log.d("position", ""+position);
                        for(int i=0; i<alarmdetailcount.size(); i++){
                            Log.d("countlist123123", ""+alarmdetailcount.get(i));
                        }

                        Intent intent = new Intent(TabAlarmActivity.this, DetailAlarmActivity.class);
                        intent.putExtra("position", position);
                        intent.putIntegerArrayListExtra("count", (ArrayList<Integer>) alarmdetailcount);
                        intent.putStringArrayListExtra("alarm", (ArrayList<String>) alarm);
                        intent.putStringArrayListExtra("alarmtitle", (ArrayList<String>) alarmtitle);
                        intent.putStringArrayListExtra("alarmdetailcontent", (ArrayList<String>) alarmdetailcontent);
                        intent.putStringArrayListExtra("alarmdetailimage", (ArrayList<String>) alarmdetailimage);

                        TabAlarmActivity.this.startActivity(intent);

                    }
                });


                stYear = new int[alarm.size()];
                stMonth = new int[alarm.size()];
                stDay = new int[alarm.size()];
                stHour = new int[alarm.size()];
                stMinute = new int[alarm.size()];


                //알람을 등록하기 위해 받은 년,월,일,시,분을 나눠서 배열에 저장
                for(int i=0; i< alarm.size(); i++){
                    StringTokenizer stringTokenizer = new StringTokenizer(alarm.get(i), "-");
                    if(stringTokenizer.hasMoreTokens()){
                        stYear[i] = Integer.parseInt(stringTokenizer.nextToken());
                        stMonth[i] = Integer.parseInt(stringTokenizer.nextToken());
                        stDay[i] = Integer.parseInt(stringTokenizer.nextToken());
                        stHour[i] = Integer.parseInt(stringTokenizer.nextToken());
                        stMinute[i] = Integer.parseInt(stringTokenizer.nextToken());
                    }

                }

                inputtitle = new String[alarm.size()];

                //알람 갯수만큼 AlarmHATT 호출
                new AlarmHATT(getApplicationContext()).Alarm(alarm.size());

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    //알람을 보내는 함수
    public class AlarmHATT {
        private Context context;

        public AlarmHATT(Context context) {

            this.context = context;
        }
        int last = 0;
        int first = 0;

        public void Alarm(int count) {
            // Log.d("븐", ""+a);

            PendingIntent[] sender = new PendingIntent[count];

            //세부알람에 대한 일정타이틀을 넣어줌
            for(int i=0; i<countlist.length; i++){
                last += countlist[i];
                for(int j=first; j<last; j++){
                    inputtitle[j] = totaltitle[i];
                }
                first += countlist[i];
            }
            //알람 갯수만큼 알람시간 설정
            for(int i=0; i < count; i++){
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(TabAlarmActivity.this, BroadcastD.class);
                intent.putExtra("title", inputtitle[i]);
                intent.putExtra("content", "일정 '"+alarmtitle.get(i)+"' 알람입니다.");
                sender[i] = PendingIntent.getBroadcast(TabAlarmActivity.this, i, intent, 0);

                Calendar calendar = Calendar.getInstance();
                //알람시간 calendar에 set해주기
                calendar.set(stYear[i], stMonth[i]-1, stDay[i], stHour[i], stMinute[i]);
                //알람 예약
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender[i]);
            }
        }
    }
}
