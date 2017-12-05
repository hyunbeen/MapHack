package com.example.kosta.maphack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by kosta on 2017-12-05.
 */

public class StartActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        try{
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
