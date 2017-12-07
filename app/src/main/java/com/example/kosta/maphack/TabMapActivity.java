package com.example.kosta.maphack;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kosta on 2017-12-06.
 */

public class TabMapActivity extends Activity implements OnMapReadyCallback{
    GoogleMap googleMap;
    MapFragment mapFragment;
    MapFragment mapFragment2;

    String responseData;
    String homeresponse;

    String mapx[];
    String mapy[];
    String title[];
    String detail[];
    String firstimage[];

    String homemapx[];
    String homemapy[];
    String hometitle[];
    String homedetail[];
    String homefirstimage[];

    Button tourbtn, homebtn, foodbtn;

    MarkerOptions markerarray[];
    MarkerOptions homemarker[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabmap);

        tourbtn = (Button)findViewById(R.id.tourbtn);

        tourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tourbtn.getText().equals("노답1")){
                    for (int i=0; i<markerarray.length; i++){

                        googleMap.clear();
                    }
                    tourbtn.setText("노답");
                }else{
                    for (int i=0; i<markerarray.length; i++){

                        markerarray[i].visible(true);
                        googleMap.addMarker(markerarray[i]);
                    }
                    tourbtn.setText("노답1");
                }

            }
        });

        homebtn = (Button)findViewById(R.id.homebtn);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homebtn.getText().equals("노답1")){
                    for (int i=0; i<homemarker.length; i++){

                        googleMap.clear();
                    }
                    homebtn.setText("노답");
                }else{
                    for (int i=0; i<homemarker.length; i++){

                        homemarker[i].visible(true);
                        googleMap.addMarker(homemarker[i]);
                    }
                    homebtn.setText("노답1");
                }
            }
        });


        TourTask tourTask = new TourTask();

        Map<String, String> params = new HashMap<String, String>();
        params.put("title", "메모입니다.");
        params.put("memo", "메모를 입력했습니다.");

        tourTask.execute(params);

        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);


    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;

        LatLng SEOUL = new LatLng(37.56, 126.97);

        double x[];
        double y[];
        x = new double[mapx.length];
        y = new double[mapx.length];

        double homex[];
        double homey[];
        homex = new double[homemapx.length];
        homey = new double[homemapx.length];

        markerarray = new MarkerOptions[mapx.length];

        for(int i =0; i < mapx.length; i++){

            x[i] = Double.valueOf(mapx[i]);
            y[i] = Double.valueOf(mapy[i]);

            LatLng location = new LatLng(y[i], x[i]);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title(title[i]);
            markerOptions.snippet(detail[i]);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            markerOptions.visible(false);

            map.addMarker(markerOptions);

            markerarray[i] = markerOptions;
        }

        homemarker = new MarkerOptions[homex.length];

        for(int i =0; i < homemapx.length; i++){

            homex[i] = Double.valueOf(homemapx[i]);
            homey[i] = Double.valueOf(homemapy[i]);

            LatLng location = new LatLng(homey[i], homex[i]);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title(hometitle[i]);
            markerOptions.snippet(homedetail[i]);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            markerOptions.visible(false);

            map.addMarker(markerOptions);

            homemarker[i] = markerOptions;
        }

        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
    public class TourTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

// Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.0.125:8080/MapHack/android.mh");

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

            try {
                String list = null;
                String detaillist = null;
                String list1 = null;
                String detaillist1 = null;

//                JSONArray jsonArray = new JSONArray(responseData);
                JSONObject object = new JSONObject(responseData);
                    list = object.getString("list");
                    detaillist = object.getString("detaillist");
                    list1 = object.getString("list1");
                    detaillist1 = object.getString("detaillist1");

                    Log.d("awefawefawef", list1);
                    Log.d("ffjfjfjfjfj", detaillist1);
                JSONArray array = new JSONArray(list);
                JSONArray array1 = new JSONArray(detaillist);
                JSONArray array2 = new JSONArray(list1);
                JSONArray array3 = new JSONArray(detaillist1);


                mapx = new String[array.length()];
                mapy = new String[array.length()];
                title = new String[array.length()];
                detail = new String[array.length()];
                firstimage = new String[array.length()];

                homemapx = new String[array2.length()];
                homemapy = new String[array2.length()];
                hometitle = new String[array2.length()];
                homedetail = new String[array2.length()];
                homefirstimage = new String[array2.length()];

                for(int i=0; i < array.length(); i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    JSONObject jsonObject1 = array1.getJSONObject(i);
                    mapx[i] = jsonObject.getString("mapx");
                    mapy[i] = jsonObject.getString("mapy");
//                    firstimage[i] = jsonObject.getString("firstimage");
                    title[i] = jsonObject.getString("title");
                    detail[i] = jsonObject1.getString("overview");
                }

                for(int i=0; i < array2.length(); i++){
                    JSONObject jsonObject = array2.getJSONObject(i);
                    JSONObject jsonObject1 = array3.getJSONObject(i);
                    homemapx[i] = jsonObject.getString("mapx");
                    homemapy[i] = jsonObject.getString("mapy");
//                    firstimage[i] = jsonObject.getString("firstimage");
                    hometitle[i] = jsonObject.getString("title");
                    homedetail[i] = jsonObject1.getString("overview");
                }

                mapFragment.getMapAsync(TabMapActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

