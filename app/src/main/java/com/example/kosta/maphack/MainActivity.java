package com.example.kosta.maphack;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//탭리스트 생성
public class MainActivity extends TabActivity {
    TabHost mTab;

    LocationManager manager;
    List<String> enabledProviders;
    float bestAccuracy;
    String mLat, mLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); /*맵핵 프로젝트 시작*/
        setContentView(R.layout.activity_main);



        manager=(LocationManager)getSystemService(LOCATION_SERVICE);
        //위치 정보를 허용하기위함
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //위치 정보를 허용 / 거부를 선택하는 알림창을 띄움
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }else {
            getProviders();
            getLocation();
        }

        Intent intent;
        TabHost tabHost = getTabHost();

        intent = new Intent().setClass(this, TabHomeActivity.class);

        TabHost.TabSpec tabSpecTab1 = tabHost.newTabSpec("TAB1").setIndicator("",getResources().getDrawable(R.drawable.account));
        tabSpecTab1.setContent(intent);
        tabHost.addTab(tabSpecTab1);

        intent = new Intent().setClass(this, TabMapActivity.class);
        TabHost.TabSpec tabSpecTab2 = tabHost.newTabSpec("TAB2").setIndicator("",getResources().getDrawable(R.drawable.map));
        tabSpecTab2.setContent(intent);
        tabHost.addTab(tabSpecTab2);
        //TabMapActivity에 현재위치 좌표값 전송
        intent.putExtra("mLat",mLat);
        intent.putExtra("mLng",mLng);

        intent = new Intent().setClass(this, TabListActivity.class);
        TabHost.TabSpec tabSpecTab3 = tabHost.newTabSpec("TAB3").setIndicator("",getResources().getDrawable(R.drawable.view));
        tabSpecTab3.setContent(intent);
        tabHost.addTab(tabSpecTab3);

        intent = new Intent().setClass(this, TabAlarmActivity.class);
        TabHost.TabSpec tabSpecTab4 = tabHost.newTabSpec("TAB4").setIndicator("",getResources().getDrawable(R.drawable.alarm));
        tabSpecTab4.setContent(intent);
        tabHost.addTab(tabSpecTab4);

        intent = new Intent().setClass(this, TabSettingActivity.class);
        TabHost.TabSpec tabSpecTab5 = tabHost.newTabSpec("TAB5").setIndicator("",getResources().getDrawable(R.drawable.setting));
        tabSpecTab5.setContent(intent);
        tabHost.addTab(tabSpecTab5);

        //실행했을 시 시작화면이 TabListActivity를 보여줌
        tabHost.setCurrentTab(2);

    }
    //위치 허용을 선택
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getProviders();
                getLocation();
            } else {
                showToast("no permission...");
            }
        }
    }
    //위치 허용이 되지않았을 때
    private void showToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    private void getProviders(){
        //add~~~~~~~~~~~~~~
        String result="App Providers:";
        List<String> providers=manager.getAllProviders();
        for(String provider: providers){
            result += provider +",";
        }

        result="Enabled Providers : ";
        enabledProviders=manager.getProviders(true);
        for(String provider : enabledProviders){
            result += provider+",";
        }
    }
    //현재 나의 위치 가져오기
    private void getLocation(){
        //add~~~~~~~~~~~~~
        for(String provider : enabledProviders){
            Location location=null;
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                location=manager.getLastKnownLocation(provider);
            }else {
                showToast("no permission");
            }
            if(location != null){
                float accuracy=location.getAccuracy();
                if(bestAccuracy==0){
                    bestAccuracy=accuracy;
                    setLocationInfo(provider, location);
                }else {
                    if(accuracy<bestAccuracy){
                        bestAccuracy=accuracy;
                        setLocationInfo(provider, location);
                    }
                }
            }
        }
    }
    //전역 변수에 x, y좌표 값 넣기
    private void setLocationInfo(String provider, Location location){
        //add~~~~~~~~~~~~~~~~
        if(location != null){

            mLat = String.valueOf(location.getLatitude());
            mLng = String.valueOf(location.getLongitude());

        }else {
            showToast("location null");
        }
    }

}
