package com.example.kosta.maphack.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.maphack.R;
import com.example.kosta.maphack.model.AlarmList;
import com.example.kosta.maphack.model.MapSearch;

import java.util.List;


//일정에 대한 알람 리스트어댑터
public class AlarmListAdapter extends ArrayAdapter<AlarmList>{

    Activity activity;
    int resource;

    public AlarmListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<AlarmList> objects) {
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
        final AlarmList item = getItem(position);

        if(item != null){
            WebView img = (WebView)itemView.findViewById(R.id.imgView);
            TextView tvUp = (TextView) itemView.findViewById(R.id.alarmup);
            TextView tvDown = (TextView) itemView.findViewById(R.id.alarmdown);

            img.loadDataWithBaseURL(null, creHtmlBody(item.getImage()), "text/html", "utf-8", null);

            img.setHorizontalScrollBarEnabled(false);
            img.setWebViewClient(new WebViewClient());
            img.setClickable(false);
            img.setFocusable(false);

            tvUp.setText(item.getTitle());
            tvDown.setText(item.getDescription());

        }

        //return super.getView(position, convertView, parent);
        return itemView;
    }
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
