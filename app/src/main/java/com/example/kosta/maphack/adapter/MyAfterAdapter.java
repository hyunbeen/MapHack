package com.example.kosta.maphack.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kosta.maphack.R;
import com.example.kosta.maphack.model.After;

import java.util.List;


/**
 * Created by kosta on 2017-10-25.
 */

public class MyAfterAdapter extends ArrayAdapter<After> {
    Activity activity; //화면을 구성하는 객체를 참조하는 클래스(객체)
    int resource; //화면을 구성하는 xml 파일을 찾는 아이디값
    CheckBox checkBox;

    public MyAfterAdapter(@NonNull final Context context, @LayoutRes int resource, @NonNull List<After> objects) {
        super(context, resource, objects);
        this.resource = resource;
        activity = (Activity)context;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //모델값을 뷰단의 아이템으로 하나씩 연결하는 역할
        View itemView = convertView;
        if(itemView==null){
            //한줄 아이템 xml 파일을 읽어서 메모리 객체과정 => inflation
            itemView =  this.activity.getLayoutInflater().inflate(this.resource, null);
        }
        final After item1 = getItem(position);

        if(item1 != null){
            WebView myAfterImg1 = (WebView)itemView.findViewById(R.id.imgmyafter);

            final TextView myAfterTitle1 = (TextView)itemView.findViewById(R.id.MyTitleAfter1);

            String imgurl = "";
            //no image 설정
            if(item1.getAft_image().equals("")){
                imgurl = "http://api.visitkorea.or.kr/static/images/common/noImage.gif";

            }else{
                imgurl = "http://192.168.0.104:8080/MapHack/upload2/"+item1.getAft_image();

            }

            //list item 설정
            myAfterImg1.loadDataWithBaseURL(null, creHtmlBody(imgurl), "text/html", "utf-8", null);
            myAfterImg1.setHorizontalScrollBarEnabled(false);
            myAfterImg1.setWebViewClient(new WebViewClient());
            myAfterImg1.setClickable(false);
            myAfterImg1.setFocusable(false);
            myAfterTitle1.setText(item1.getAft_title());





        }
       /* return super.getView(position, convertView, parent);*/
        return itemView;
    }
    //webview 이미지 설정
    public  String creHtmlBody(String imagUrl){
        StringBuffer sb = new StringBuffer("<HTML>");
        sb.append("<HEAD>");
        sb.append("</HEAD>");
        sb.append("<BODY style='margin:0; padding:0; text-align:center;'>");    //중앙정렬
        sb.append("<img width='100%' height='100%' src=\"" + imagUrl+"\">"); //가득차게 나옴
        sb.append("</BODY>");
        sb.append("</HTML>");
        return sb.toString();
    }
}