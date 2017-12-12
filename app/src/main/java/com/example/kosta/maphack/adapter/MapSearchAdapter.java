package com.example.kosta.maphack.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
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

            if(item.getImage().equals("http://api.visitkorea.or.kr/static/images/common/noImage.gif")){
                img.setInitialScale(200);
            }else{
                img.setInitialScale(80);
            }
            img.setHorizontalScrollBarEnabled(false);
            img.setWebViewClient(new WebViewClient());

            img.loadUrl(item.getImage());
            tvUp.setText(item.getTitle());
            tvDown.setText(item.getDescription());

        }

        //return super.getView(position, convertView, parent);
        return itemView;
    }
}
