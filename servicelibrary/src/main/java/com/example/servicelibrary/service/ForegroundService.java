package com.example.servicelibrary.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.servicelibrary.R;

public class ForegroundService extends Service {
    private static final String TAG = "ForegroundService";
    private MyHandler handler;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "service onCreate");
        handler = new MyHandler();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        int type = intent.getIntExtra("type",1);
        Log.d(TAG, "the create notification type is " + type + "----" + (type == 1 ? "true" : "false"));
        if(type == 1){
            createNotificationChannel();
        }else{
            createErrorNotification();
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(startId);
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void createErrorNotification() {
        Notification notification = new Notification.Builder(this).build();
        startForeground(0, notification);//id不可以是0
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //高版本需要渠道
        String CHANNEL_ID = "my_channel_01";// 通知渠道的id
        CharSequence name = getString(R.string.channel_name);// 用户可以看到的通知渠道的名字.
        String description = getString(R.string.channel_description);// 用户可以看到的通知渠道的描述
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
        mChannel.setDescription(description);// 配置通知渠道的属性
        mChannel.enableLights(true);//设置通知出现时的闪灯（如果 android 设备支持的话）
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);//设置通知出现时的震动（如果 android 设备支持的话）
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//         最后在notificationmanager中创建该通知渠道
        mNotificationManager.createNotificationChannel(mChannel);

        // 为该通知设置一个id
        int notifyID = 4;
        // Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(this,CHANNEL_ID)
                .setContentTitle("QQ消息")
                .setContentText("你收到一条群消息，请点击查看")
                .setSmallIcon(R.drawable.default_icon)
                .setAutoCancel(true)
                .build();
        startForeground(notifyID,notification);
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(ForegroundService.this, "收到消息", Toast.LENGTH_LONG).show();
            stopSelf(msg.what);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "5s onDestroy");
        Toast.makeText(this, "this service destroy", Toast.LENGTH_LONG).show();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
