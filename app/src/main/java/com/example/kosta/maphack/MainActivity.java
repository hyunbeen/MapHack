package com.example.kosta.maphack;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
    TabHost mTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); /*맵핵 프로젝트 시작*/
        setContentView(R.layout.activity_main);

        Intent intent;
        TabHost tabHost = getTabHost();

        intent = new Intent().setClass(this, TabHomeActivity.class);

        TabHost.TabSpec tabSpecTab1 = tabHost.newTabSpec("TAB1").setIndicator("",getResources().getDrawable(R.drawable.home));
        tabSpecTab1.setContent(intent);
        tabHost.addTab(tabSpecTab1);

        intent = new Intent().setClass(this, TabMapActivity.class);
        TabHost.TabSpec tabSpecTab2 = tabHost.newTabSpec("TAB2").setIndicator("",getResources().getDrawable(R.drawable.map));
        tabSpecTab2.setContent(intent);
        tabHost.addTab(tabSpecTab2);

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

        tabHost.setCurrentTab(2);

    }

}
