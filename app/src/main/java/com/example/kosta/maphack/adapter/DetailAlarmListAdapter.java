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
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kosta.maphack.R;
import com.example.kosta.maphack.model.DetailAlarmList;

import java.util.List;



//일정에대한 세부알람내용에대한 리스트어댑터
public class DetailAlarmListAdapter extends ArrayAdapter<DetailAlarmList>{

    Activity activity;
    int resource;

    public DetailAlarmListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DetailAlarmList> objects) {
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
        final DetailAlarmList item = getItem(position);

        if(item != null){
            WebView img = (WebView)itemView.findViewById(R.id.detailimgView);
            TextView tvUp = (TextView) itemView.findViewById(R.id.detailalarmup);
            TextView tvDown = (TextView) itemView.findViewById(R.id.detailalarmdown);
            TextView tvDown1 = (TextView)itemView.findViewById(R.id.detailalarmdown1);
            final CheckBox checkBox = (CheckBox)itemView.findViewById(R.id.checkbox);

            img.loadDataWithBaseURL(null, creHtmlBody(item.getImage()), "text/html", "utf-8", null);

            img.setHorizontalScrollBarEnabled(false);
            img.setWebViewClient(new WebViewClient());
            img.setClickable(false);
            img.setFocusable(false);

            tvUp.setText(item.getTitle());
            tvDown.setText(item.getDescription());
            tvDown1.setText(item.getDescription1());

            checkBox.setChecked(true);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        item.setCheck(true);
                    }else{
                        item.setCheck(false);
                        Toast.makeText(getContext(), "알람 해제", Toast.LENGTH_SHORT).show();
                    }
                }
            });

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
