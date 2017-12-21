package com.example.kosta.maphack;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.example.kosta.maphack.adapter.MyFavorAdapter;
import com.example.kosta.maphack.model.Favor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

import static java.lang.String.valueOf;

/**
 * Created by kosta on 2017-12-11.
 */

public class MyTravelModifyActivity extends Activity {


    //회원 정보 수정 activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytravelmodify);




    }



}