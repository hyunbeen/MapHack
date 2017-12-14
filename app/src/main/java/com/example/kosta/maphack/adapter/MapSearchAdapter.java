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

            img.loadDataWithBaseURL(null, creHtmlBody(item.getImage()), "text/html", "utf-8", null);

            img.setHorizontalScrollBarEnabled(false);
            img.setWebViewClient(new WebViewClient());
            img.setClickable(false);
            img.setFocusable(false);

            //img.loadUrl(item.getImage());
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
