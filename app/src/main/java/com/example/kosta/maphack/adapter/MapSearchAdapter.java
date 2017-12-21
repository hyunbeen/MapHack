package com.example.kosta.maphack.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.maphack.R;
import com.example.kosta.maphack.model.MapSearch;

import java.net.URI;
import java.util.List;

/**
 * Created by kosta on 2017-12-08.
 */

//TabMapActivity에 검색리스트에 대한 어댑터
public class MapSearchAdapter extends ArrayAdapter<MapSearch>{

    Activity activity;
    int resource;

    public MapSearchAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MapSearch> objects) {
        super(context, resource, objects);
        this.resource = resource;
        activity = (Activity)context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View itemView = convertView;
        if(itemView == null){
            itemView =this.activity.getLayoutInflater().inflate(this.resource, null);
        }
        final MapSearch item = getItem(position);

        if(item != null){
            WebView img = (WebView)itemView.findViewById(R.id.img);
            TextView tvUp = (TextView) itemView.findViewById(R.id.tvUp);
            TextView tvDown = (TextView) itemView.findViewById(R.id.tvDown);

            //웹뷰에 사진을 가로세로크기를 맞추기위함
            img.loadDataWithBaseURL(null, creHtmlBody(item.getImage()), "text/html", "utf-8", null);

            img.setHorizontalScrollBarEnabled(false); //웹뷰 스크롤바 제거
            img.setWebViewClient(new WebViewClient()); //웹뷰에 보여주기 위함
            img.setClickable(false); //웹뷰 클릭제거
            img.setFocusable(false); //웹뷰의 포커싱 제거

            //img.loadUrl(item.getImage());
            tvUp.setText(item.getTitle());
            tvDown.setText(item.getDescription());

        }

        //return super.getView(position, convertView, parent);
        return itemView;
    }
    //웹뷰에 불러온 사진들의 크기를 맞추는 함수호출
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
