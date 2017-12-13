package com.example.kosta.maphack;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kosta.maphack.adapter.MapSearchAdapter;
import com.example.kosta.maphack.model.MapSearch;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kosta on 2017-12-06.
 */

public class TabMapActivity extends Activity implements OnMapReadyCallback{
    GoogleMap googleMap;
    GoogleMap googleMap2;
    MapFragment mapFragment;
    MapFragment mapFragment2;


    double mLatitude;
    double mLongitude;

    String responseData;
    String homeresponse;

    String mapx[];
    String mapy[];
    String title[];
    String detail[];
    String firstimage[];
    String sigungucode[];
    String contenttypeid[];


    String homemapx[];
    String homemapy[];
    String hometitle[];
    String homedetail[];
    String homefirstimage[];
    String homesigungucode[];
    String homecontenttypeid[];

    String foodmapx[];
    String foodmapy[];
    String foodtitle[];
    String fooddetail[];
    String foodfirstimage[];
    String foodsigungucode[];
    String foodcontenttypeid[];

    Button tourbtn, homebtn, foodbtn, searchbtn;
    Spinner spinnersi, spinnergu, spinnercate;
    EditText editdetail;

    ListView listView;
    List<MapSearch> list;
    MapSearchAdapter mapSearchAdapter;

    String searchgu;
    String searchcate;
    MarkerOptions searchmarker[];
    int index;

    MarkerOptions markerarray[];
    MarkerOptions homemarker[];
    MarkerOptions foodmarker[];
    MarkerOptions mymarker;

    LocationManager locationManager;

    @Override
    protected void onPause() {
        super.onPause();
        //
        Log.d("멈춰랏", "ㅁㅈㄷㄹ");
        locationManager.removeUpdates(locationListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("다시시작해랏", "ㅁㅈㄷㄹ");
        requestMyLocation();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabmap);

        Intent intent = getIntent();
         String mLat = intent.getExtras().getString("mLat");
         String mLng = intent.getExtras().getString("mLng");
         Log.d("mlat", mLat);
         Log.d("mlng", mLng);
         mLatitude = Double.parseDouble(mLat);
         mLongitude = Double.parseDouble(mLng);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //GPS가 켜져있는지 체크
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //GPS 설정화면으로 이동
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent1.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent1);
        }

        //마시멜로 이상이면 권한 요청하기
        if(Build.VERSION.SDK_INT >= 23){
            //권한이 없는 경우
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Log.d("권한", "없나용");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.ACCESS_FINE_LOCATION} , 1);
            }
            //권한이 있는 경우
            else{
                Log.d("권한", "있나용");
                requestMyLocation();
                Log.d("권한", "호출됐나요");
            }
        }
        //마시멜로 아래
        else{
            requestMyLocation();
            Log.d("권한", "호출됐나요2");
        }


        tourbtn = (Button)findViewById(R.id.tourbtn);

        tourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapSearchAdapter.clear();
                if(tourbtn.getText().equals("OFF")){
                    if(foodbtn.getText().equals("ON") && homebtn.getText().equals("ON")){

                        googleMap.clear();
                        googleMap.addMarker(mymarker);

                    }else if(foodbtn.getText().equals("OFF") && homebtn.getText().equals("ON")){
                        googleMap.clear();
                        googleMap.addMarker(mymarker);
                        for (int i=0; i<foodmarker.length; i++){

                            foodmarker[i].visible(true);
                            googleMap.addMarker(foodmarker[i]);
                        }
                    }else if(foodbtn.getText().equals("ON") && homebtn.getText().equals("OFF")){
                        googleMap.clear();
                        googleMap.addMarker(mymarker);
                        for (int i=0; i<homemarker.length; i++){

                            homemarker[i].visible(true);
                            googleMap.addMarker(homemarker[i]);
                        }
                    }else if(foodbtn.getText().equals("OFF") && homebtn.getText().equals("OFF")){
                        googleMap.clear();
                        googleMap.addMarker(mymarker);
                        for (int i=0; i<foodmarker.length; i++){

                            foodmarker[i].visible(true);
                            googleMap.addMarker(foodmarker[i]);
                        }
                        for (int i=0; i<homemarker.length; i++){

                            homemarker[i].visible(true);
                            googleMap.addMarker(homemarker[i]);
                        }
                    }
                    tourbtn.setText("ON");
                }else{
                    for (int i=0; i<markerarray.length; i++){

                        markerarray[i].visible(true);
                        googleMap.addMarker(markerarray[i]);
                    }
                    googleMap.addMarker(mymarker);
                    tourbtn.setText("OFF");
                }

            }
        });

        homebtn = (Button)findViewById(R.id.homebtn);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapSearchAdapter.clear();
                if(homebtn.getText().equals("OFF")){
                    if (foodbtn.getText().equals("ON") && tourbtn.getText().equals("ON")){
                        googleMap.clear();
                        googleMap.addMarker(mymarker);
                    }else if(foodbtn.getText().equals("OFF") && tourbtn.getText().equals("ON")){
                        googleMap.clear();
                        googleMap.addMarker(mymarker);
                        for (int i=0; i<foodmarker.length; i++){

                            foodmarker[i].visible(true);
                            googleMap.addMarker(foodmarker[i]);
                        }
                    }else if(foodbtn.getText().equals("ON") && tourbtn.getText().equals("OFF")){
                        googleMap.clear();
                        googleMap.addMarker(mymarker);
                        for (int i=0; i<markerarray.length; i++){

                            markerarray[i].visible(true);
                            googleMap.addMarker(markerarray[i]);
                        }
                    }else if(foodbtn.getText().equals("OFF") && tourbtn.getText().equals("OFF")){
                        googleMap.clear();
                        googleMap.addMarker(mymarker);
                        for (int i=0; i<foodmarker.length; i++){

                            foodmarker[i].visible(true);
                            googleMap.addMarker(foodmarker[i]);
                        }
                        for (int i=0; i<markerarray.length; i++){

                            markerarray[i].visible(true);
                            googleMap.addMarker(markerarray[i]);
                        }
                    }
                    homebtn.setText("ON");
                }else{
                    for (int i=0; i<homemarker.length; i++){

                        homemarker[i].visible(true);
                        googleMap.addMarker(homemarker[i]);
                    }
                    googleMap.addMarker(mymarker);
                    homebtn.setText("OFF");
                }
            }
        });

        foodbtn = (Button)findViewById(R.id.foodbtn);

        foodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapSearchAdapter.clear();
                if(foodbtn.getText().equals("OFF")){
                    if(tourbtn.getText().equals("ON") && homebtn.getText().equals("ON")){

                        googleMap.clear();
                        googleMap.addMarker(mymarker);
                    }else if(tourbtn.getText().equals("OFF") && homebtn.getText().equals("ON")){
                        googleMap.clear();
                        googleMap.addMarker(mymarker);
                        for (int i=0; i<markerarray.length; i++){

                            markerarray[i].visible(true);
                            googleMap.addMarker(markerarray[i]);
                        }
                    }else if(tourbtn.getText().equals("ON") && homebtn.getText().equals("OFF")){
                        googleMap.clear();
                        googleMap.addMarker(mymarker);
                        for (int i=0; i<homemarker.length; i++){

                            homemarker[i].visible(true);
                            googleMap.addMarker(homemarker[i]);
                        }
                    }else if(tourbtn.getText().equals("OFF") && homebtn.getText().equals("OFF")){
                        googleMap.clear();
                        googleMap.addMarker(mymarker);
                        for (int i=0; i<markerarray.length; i++){

                            markerarray[i].visible(true);
                            googleMap.addMarker(markerarray[i]);
                        }
                        for (int i=0; i<homemarker.length; i++){

                            homemarker[i].visible(true);
                            googleMap.addMarker(homemarker[i]);
                        }
                    }
                    foodbtn.setText("ON");
                }else{
                    for (int i=0; i<foodmarker.length; i++){

                        foodmarker[i].visible(true);
                        googleMap.addMarker(foodmarker[i]);
                    }
                    googleMap.addMarker(mymarker);
                    foodbtn.setText("OFF");
                }
            }
        });


        spinnersi = (Spinner)findViewById(R.id.selectsi);
        spinnergu = (Spinner)findViewById(R.id.selectgu);
        spinnercate = (Spinner)findViewById(R.id.selectcate);

        editdetail = (EditText)findViewById(R.id.editdetail);

        listView = (ListView)findViewById(R.id.searchlist);

        list = new ArrayList<MapSearch>();
        mapSearchAdapter = new MapSearchAdapter(this, R.layout.layout_mapsearch, list);

        listView.setAdapter(mapSearchAdapter);

        searchbtn = (Button)findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String si = spinnersi.getSelectedItem().toString();
                String gu = spinnergu.getSelectedItem().toString();
                String cate = spinnercate.getSelectedItem().toString();
                String search = ""+editdetail.getText();

                tourbtn.setText("ON");
                foodbtn.setText("ON");
                homebtn.setText("ON");

                addData(si, gu, cate, search);
            }
        });




        TourTask tourTask = new TourTask();

        Map<String, String> params = new HashMap<String, String>();
        params.put("title", "메모입니다.");
        params.put("memo", "메모를 입력했습니다.");

        tourTask.execute(params);

        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);

    }
    //권한 요청후 응답 콜백
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //ACCESS_COARSE_LOCATION 권한
        if(requestCode==1){
            //권한받음
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.d("권한", "호출됐나요23");
                requestMyLocation();
            }
            //권한못받음
            else{
                Toast.makeText(this, "권한없음", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    //나의 위치 요청
    public void requestMyLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        //요청
        Log.d("권한", "호출됐123123나요");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        Log.d("권한12344", "호출완료한거니");
    }

    //위치정보 구하기 리스너
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(ContextCompat.checkSelfPermission(TabMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(TabMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Log.d("리턴하니", "몰라요");
                return;
            }
            //나의 위치를 한번만 가져오기 위해
            //
            Log.d("권한", "들어오나요");
            //위도 경도
            mLatitude = location.getLatitude();   //위도
            mLongitude = location.getLongitude(); //경도


                Log.d("long", ""+mLongitude);
                Log.d("lat", ""+mLatitude);
                googleMap.clear();


                if(tourbtn.getText().equals("OFF")){
                    for(int i=0; i< markerarray.length; i++){
                        markerarray[i].visible(true);
                        googleMap.addMarker(markerarray[i]);
                    }
                }
                if(homebtn.getText().equals("OFF")){
                    for(int i=0; i<homemarker.length; i++){
                        homemarker[i].visible(true);
                        googleMap.addMarker(homemarker[i]);
                    }
                }
                if(foodbtn.getText().equals("OFF")){
                    for(int i=0; i<foodmarker.length; i++){
                        foodmarker[i].visible(true);
                        googleMap.addMarker(foodmarker[i]);
                    }
                }

                if(tourbtn.getText().equals("ON") && homebtn.getText().equals("ON") && foodbtn.getText().equals("ON")){
                    if(mapSearchAdapter.getCount() > 0){
                        String si = spinnersi.getSelectedItem().toString();
                        String gu = spinnergu.getSelectedItem().toString();
                        String cate = spinnercate.getSelectedItem().toString();
                        String search = ""+editdetail.getText();

                        addData(si, gu, cate, search);
                    }
                }

            LatLng mylocation = new LatLng(mLatitude, mLongitude);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mylocation);
            markerOptions.title("내위치");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markerOptions.visible(true);

            googleMap.addMarker(markerOptions);
            mymarker = markerOptions;

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { Log.d("gps", "onStatusChanged"); }

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider)  {}
    };


    private void addData(String si, String gu, String cate, String search) {
        //adapter.add(new Flower("제목", "내용", R.drawable.flower01, false));


        mapSearchAdapter.clear();

        if(gu.equals("강남구")){
            searchgu = "1";
        }else if(gu.equals("강동구")){
            searchgu = "2";
        }else if(gu.equals("강북구")){
            searchgu = "3";
        }
        else if(gu.equals("강서구")){
            searchgu = "4";
        }
        else if(gu.equals("관악구")){
            searchgu = "5";
        }
        else if(gu.equals("광진구")){
            searchgu = "6";
        }
        else if(gu.equals("구로구")){
            searchgu = "7";
        }
        else if(gu.equals("금천구")){
            searchgu = "8";
        }
        else if(gu.equals("노원구")){
            searchgu = "9";
        }
        else if(gu.equals("도봉구")){
            searchgu = "10";
        }
        else if(gu.equals("동대문구")){
            searchgu = "11";
        }
        else if(gu.equals("동작구")){
            searchgu = "12";
        }else if(gu.equals("마포구")){
            searchgu = "13";
        }else if(gu.equals("서대문구")){
            searchgu = "14";
        }else if(gu.equals("서초구")){
            searchgu = "15";
        }else if(gu.equals("성동구")){
            searchgu = "16";
        }else if(gu.equals("성북구")){
            searchgu = "17";
        }else if(gu.equals("송파구")){
            searchgu = "18";
        }else if(gu.equals("양천구")){
            searchgu = "19";
        }else if(gu.equals("영등포구")){
            searchgu = "20";
        }else if(gu.equals("용산구")){
            searchgu = "21";
        }else if(gu.equals("은평구")){
            searchgu = "22";
        }else if(gu.equals("종로구")){
            searchgu = "23";
        }else if(gu.equals("중구")){
            searchgu = "24";
        }else if(gu.equals("중랑구")){
            searchgu = "25";
        }else if(gu.equals("전체")){
            searchgu = "전체";
        }


        if(cate.equals("관광지")){
            searchcate = "12";
        }else if(cate.equals("숙박")){
            searchcate = "32";
        }else if(cate.equals("음식점")){
            searchcate = "39";
        }else if(cate.equals("전체")){
            searchcate = "전체";
        }

        if (searchcate.equals("12")){
            googleMap.clear();
            googleMap.addMarker(mymarker);
            for(int i=0; i< markerarray.length; i++){
               String detailsearch = title[i];
                double x[];
                double y[];
                x = new double[mapx.length];
                y = new double[mapx.length];
                x[i] = Double.valueOf(mapx[i]);
                y[i] = Double.valueOf(mapy[i]);
               if(sigungucode[i].equals(searchgu) && detailsearch.indexOf(search, 0) >= 0){


                   LatLng location = new LatLng(y[i], x[i]);

                   MarkerOptions markerOptions = new MarkerOptions();
                   markerOptions.position(location);
                   markerOptions.title(title[i]);
                   markerOptions.snippet(detail[i]);
                   markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                   markerOptions.visible(true);

                   googleMap.addMarker(markerOptions);
                   mapSearchAdapter.add(new MapSearch(title[i], detail[i], firstimage[i]));
               }
            }

        }else if (searchcate.equals("39")){
            googleMap.clear();
            googleMap.addMarker(mymarker);
            for(int i=0; i< foodmarker.length; i++){
                String detailsearch = foodtitle[i];
                double x[];
                double y[];
                x = new double[foodmapx.length];
                y = new double[foodmapx.length];
                x[i] = Double.valueOf(foodmapx[i]);
                y[i] = Double.valueOf(foodmapy[i]);
                if(foodsigungucode[i].equals(searchgu) && detailsearch.indexOf(search, 0) >= 0){


                    LatLng location = new LatLng(y[i], x[i]);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location);
                    markerOptions.title(foodtitle[i]);
                    markerOptions.snippet(fooddetail[i]);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    markerOptions.visible(true);

                    googleMap.addMarker(markerOptions);
                    mapSearchAdapter.add(new MapSearch(foodtitle[i], fooddetail[i], foodfirstimage[i]));
                }
            }

        }else if (searchcate.equals("32")){
            googleMap.clear();
            googleMap.addMarker(mymarker);
            for(int i=0; i< homemarker.length; i++){
                String detailsearch = hometitle[i];
                double x[];
                double y[];
                x = new double[homemapx.length];
                y = new double[homemapx.length];
                x[i] = Double.valueOf(homemapx[i]);
                y[i] = Double.valueOf(homemapy[i]);
                if(homesigungucode[i].equals(searchgu) && detailsearch.indexOf(search, 0) >= 0){


                    LatLng location = new LatLng(y[i], x[i]);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location);
                    markerOptions.title(hometitle[i]);
                    markerOptions.snippet(homedetail[i]);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    markerOptions.visible(true);

                    googleMap.addMarker(markerOptions);
                    mapSearchAdapter.add(new MapSearch(hometitle[i], homedetail[i], homefirstimage[i]));
                }
            }

        }else if (searchcate.equals("전체")){
            googleMap.clear();
            googleMap.addMarker(mymarker);
            for(int i=0; i< homemarker.length; i++){
                String detailsearch = hometitle[i];
                double x[];
                double y[];
                x = new double[homemapx.length];
                y = new double[homemapx.length];
                x[i] = Double.valueOf(homemapx[i]);
                y[i] = Double.valueOf(homemapy[i]);
                if(homesigungucode[i].equals(searchgu) && detailsearch.indexOf(search, 0) >= 0){


                    LatLng location = new LatLng(y[i], x[i]);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location);
                    markerOptions.title(hometitle[i]);
                    markerOptions.snippet(homedetail[i]);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    markerOptions.visible(true);

                    googleMap.addMarker(markerOptions);
                    mapSearchAdapter.add(new MapSearch(hometitle[i], homedetail[i], homefirstimage[i]));
                }
            }
            for(int i=0; i< foodmarker.length; i++){
                String detailsearch = foodtitle[i];
                double x[];
                double y[];
                x = new double[foodmapx.length];
                y = new double[foodmapx.length];
                x[i] = Double.valueOf(foodmapx[i]);
                y[i] = Double.valueOf(foodmapy[i]);
                if(foodsigungucode[i].equals(searchgu) && detailsearch.indexOf(search, 0) >= 0){


                    LatLng location = new LatLng(y[i], x[i]);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location);
                    markerOptions.title(foodtitle[i]);
                    markerOptions.snippet(fooddetail[i]);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    markerOptions.visible(true);

                    googleMap.addMarker(markerOptions);
                    mapSearchAdapter.add(new MapSearch(foodtitle[i], fooddetail[i], foodfirstimage[i]));
                }
            }
            for(int i=0; i< markerarray.length; i++){
                String detailsearch = title[i];
                double x[];
                double y[];
                x = new double[mapx.length];
                y = new double[mapx.length];
                x[i] = Double.valueOf(mapx[i]);
                y[i] = Double.valueOf(mapy[i]);
                if(sigungucode[i].equals(searchgu) && detailsearch.indexOf(search, 0) >= 0){


                    LatLng location = new LatLng(y[i], x[i]);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location);
                    markerOptions.title(title[i]);
                    markerOptions.snippet(detail[i]);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    markerOptions.visible(true);

                    googleMap.addMarker(markerOptions);
                    mapSearchAdapter.add(new MapSearch(title[i], detail[i], firstimage[i]));
                }
            }

        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        googleMap2 = map;

        LatLng SEOUL = new LatLng(37.56, 126.97);

        double x[];
        double y[];
        x = new double[mapx.length];
        y = new double[mapx.length];

        double homex[];
        double homey[];
        homex = new double[homemapx.length];
        homey = new double[homemapx.length];

        double foodx[];
        double foody[];
        foodx = new double[foodmapx.length];
        foody = new double[foodmapx.length];

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
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            markerOptions.visible(false);

            map.addMarker(markerOptions);

            homemarker[i] = markerOptions;
        }

        foodmarker = new MarkerOptions[foodx.length];

        for(int i =0; i < foodmapx.length; i++){

            foodx[i] = Double.valueOf(foodmapx[i]);
            foody[i] = Double.valueOf(foodmapy[i]);

            LatLng location = new LatLng(foody[i], foodx[i]);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title(foodtitle[i]);
            markerOptions.snippet(fooddetail[i]);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            markerOptions.visible(false);
            map.addMarker(markerOptions);

            foodmarker[i] = markerOptions;
        }

        for(int i =0; i < 1; i++){

            Log.d("aweflong", ""+mLongitude);
            Log.d("aweflat", ""+mLatitude);
            LatLng location = new LatLng(mLatitude, mLongitude);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title("내위치");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markerOptions.visible(true);

            map.addMarker(markerOptions);
            mymarker = markerOptions;

        }

        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
    public class TourTask extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

// Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.0.100:8080/MapHack/android.mh");

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
                String list2 = null;
                String detaillist2 = null;

//                JSONArray jsonArray = new JSONArray(responseData);
                JSONObject object = new JSONObject(responseData);
                    list = object.getString("list");
                    detaillist = object.getString("detaillist");
                    list1 = object.getString("list1");
                    detaillist1 = object.getString("detaillist1");
                    list2 = object.getString("list2");
                    detaillist2 = object.getString("detaillist2");



                JSONArray array = new JSONArray(list);
                JSONArray array1 = new JSONArray(detaillist);
                JSONArray array2 = new JSONArray(list1);
                JSONArray array3 = new JSONArray(detaillist1);
                JSONArray array4 = new JSONArray(list2);
                JSONArray array5 = new JSONArray(detaillist2);


                mapx = new String[array.length()];
                mapy = new String[array.length()];
                title = new String[array.length()];
                detail = new String[array.length()];
                firstimage = new String[array.length()];
                sigungucode = new String[array.length()];
                contenttypeid = new String[array.length()];

                homemapx = new String[array2.length()];
                homemapy = new String[array2.length()];
                hometitle = new String[array2.length()];
                homedetail = new String[array2.length()];
                homefirstimage = new String[array2.length()];
                homesigungucode = new String[array2.length()];
                homecontenttypeid = new String[array2.length()];

                foodmapx = new String[array4.length()];
                foodmapy = new String[array4.length()];
                foodtitle = new String[array4.length()];
                fooddetail = new String[array4.length()];
                foodfirstimage = new String[array4.length()];
                foodsigungucode = new String[array4.length()];
                foodcontenttypeid = new String[array4.length()];

                for(int i=0; i < array.length(); i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    JSONObject jsonObject1 = array1.getJSONObject(i);
                    mapx[i] = jsonObject.getString("mapx");
                    mapy[i] = jsonObject.getString("mapy");
                    if(jsonObject.isNull("firstimage") == true){
                        firstimage[i] = "http://api.visitkorea.or.kr/static/images/common/noImage.gif";
                    }else{
                        firstimage[i] = jsonObject.getString("firstimage");
                    }
                    title[i] = jsonObject.getString("title");
                    detail[i] = jsonObject1.getString("overview");
                    sigungucode[i] = jsonObject.getString("sigungucode");
                    contenttypeid[i] = jsonObject.getString("contenttypeid");
                }

                for(int i=0; i < array2.length(); i++){
                    JSONObject jsonObject = array2.getJSONObject(i);
                    JSONObject jsonObject1 = array3.getJSONObject(i);
                    homemapx[i] = jsonObject.getString("mapx");
                    homemapy[i] = jsonObject.getString("mapy");
                    if(jsonObject.isNull("firstimage") == true){
                        homefirstimage[i] = "http://api.visitkorea.or.kr/static/images/common/noImage.gif";
                    }else{
                        homefirstimage[i] = jsonObject.getString("firstimage");
                    }
                    hometitle[i] = jsonObject.getString("title");
                    homedetail[i] = jsonObject1.getString("overview");
                    homesigungucode[i] = jsonObject.getString("sigungucode");
                    homecontenttypeid[i] = jsonObject.getString("contenttypeid");
                    Log.d("homei값", ""+i);
                    Log.d("homefirstimage", homefirstimage[i]);
                }

                for(int i=0; i < array4.length(); i++){
                    JSONObject jsonObject = array4.getJSONObject(i);
                    JSONObject jsonObject1 = array5.getJSONObject(i);
                    foodmapx[i] = jsonObject.getString("mapx");
                    foodmapy[i] = jsonObject.getString("mapy");
                    if(jsonObject.isNull("firstimage") == true){
                        foodfirstimage[i] = "http://api.visitkorea.or.kr/static/images/common/noImage.gif";
                    }else{
                        foodfirstimage[i] = jsonObject.getString("firstimage");
                    }
                    foodtitle[i] = jsonObject.getString("title");
                    fooddetail[i] = jsonObject1.getString("overview");
                    foodsigungucode[i] = jsonObject.getString("sigungucode");
                    foodcontenttypeid[i] = jsonObject.getString("contenttypeid");

                }

                mapFragment.getMapAsync(TabMapActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}

