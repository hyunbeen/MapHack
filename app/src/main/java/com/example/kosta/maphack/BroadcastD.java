package com.example.kosta.maphack;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



//알람을 핸드폰에 띄우기 위한 함수
public class BroadcastD extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고

        //TabAlarmActivity에서 각각의 알림에 대한 타이들과 내용 전달받음
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        //알림을 하기위한 서비스
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        //아이콘, 제목, 내용 등록 및 소리와 진동 허가
        builder.setSmallIcon(R.drawable.alarmlogo).setTicker("MapHack").setWhen(System.currentTimeMillis())
                .setNumber(1).setContentTitle(title).setContentText(content)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);

        notificationmanager.notify(1, builder.build());
    }
}

