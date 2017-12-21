package com.example.kosta.maphack.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kosta.maphack.R;
import com.example.kosta.maphack.model.After;
import com.example.kosta.maphack.model.Favor;

import java.util.List;


/**
 * Created by kosta on 2017-10-25.
 */

public class MyFavorAdapter extends ArrayAdapter<Favor> {
    Activity activity; //화면을 구성하는 객체를 참조하는 클래스(객체)
    int resource; //화면을 구성하는 xml 파일을 찾는 아이디값
    CheckBox checkBox;

    public MyFavorAdapter(@NonNull final Context context, @LayoutRes int resource, @NonNull List<Favor> objects) {
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
        final Favor item1 = getItem(position);

        if(item1 != null){
            WebView myFavorImg1 = (WebView)itemView.findViewById(R.id.imgmyfavor);

            final TextView myFavorTitle1 = (TextView)itemView.findViewById(R.id.MyTitleFavor1);

            String imgurl = "";
            if(item1.getFavor_image().equals("null")){
                imgurl = "http://api.visitkorea.or.kr/static/images/common/noImage.gif";
            }else{
                imgurl = item1.getFavor_image();
            }

            //list item 설정
            myFavorImg1.loadDataWithBaseURL(null, creHtmlBody(imgurl), "text/html", "utf-8", null);
            myFavorImg1.setHorizontalScrollBarEnabled(false);
            myFavorImg1.setWebViewClient(new WebViewClient());
            myFavorImg1.setClickable(false);
            myFavorImg1.setFocusable(false);
            myFavorTitle1.setText(item1.getFavor_title());





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