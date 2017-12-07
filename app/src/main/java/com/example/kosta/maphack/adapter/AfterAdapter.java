package com.example.kosta.maphack.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.maphack.R;
import com.example.kosta.maphack.model.After;


import java.net.URL;
import java.util.List;


/**
 * Created by kosta on 2017-10-25.
 */

public class AfterAdapter extends ArrayAdapter<After> {
    Activity activity; //화면을 구성하는 객체를 참조하는 클래스(객체)
    int resource; //화면을 구성하는 xml 파일을 찾는 아이디값
    CheckBox checkBox;

    public AfterAdapter(@NonNull final Context context, @LayoutRes int resource, @NonNull List<After> objects) {
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
        final After item1 = getItem(2*position);
        final After item2 = getItem(2*position+1);
        if(item1 != null){
            ImageView img1 = (ImageView)itemView.findViewById(R.id.img1);
            ImageView img2 = (ImageView)itemView.findViewById(R.id.img2);
            final TextView afterTitle1 = (TextView)itemView.findViewById(R.id.AfterTitle1);
            final TextView afterTitle2 = (TextView)itemView.findViewById(R.id.AfterTitle2);

            try {
                String imageUrl = "";
                String imageUrl2 = "";
                imageUrl = "http://localhost:8080/MapHack/upload/Koala.jpg";
                imageUrl2 = "http://localhost:8080/MapHack/upload/Koala.jpg";
                /*imageUrl = "http://localhost:8080/MapHack/upload/"+item1.getAft_image();
                imageUrl2 ="http://localhost:8080/MapHack/upload/"+item2.getAft_image();*/
                URL url = new URL(imageUrl);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                img1.setImageBitmap(bmp);
                URL url2 = new URL(imageUrl2);
                Bitmap bmp2 = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
                img1.setImageBitmap(bmp2);
            } catch (Exception e) {
                e.printStackTrace();
            }


            afterTitle1.setText(item1.getAft_title());
            afterTitle2.setText(item2.getAft_title());




        }
       /* return super.getView(position, convertView, parent);*/
        return itemView;
    }
}
