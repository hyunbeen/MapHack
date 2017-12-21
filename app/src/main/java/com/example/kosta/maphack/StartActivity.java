package com.example.kosta.maphack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


//스플래쉬액티비티 화면 호출

public class StartActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //3초의 화면시간
        try{
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        //3초후 메인화면 출력
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
