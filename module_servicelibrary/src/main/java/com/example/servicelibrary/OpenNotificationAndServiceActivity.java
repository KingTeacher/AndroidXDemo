package com.example.servicelibrary;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.servicelibrary.service.ForegroundService;

import java.io.File;

public class OpenNotificationAndServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_notification_and_service);
    }

    public void onclick_SendNormalNotification(View view){
        sendNotification("标题：普通推送","内容:普通推送很长很长很长很长的很长很长很长很长的很长很长很长很长的字符串","channelid1",null,10);
    }
    public void onclick_SendIntentNotification(View view){
        Intent intent = new Intent(this,ShowResultActivity.class);
        intent.putExtra("tv_text","我是跳转推送的内容");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        sendNotification("标题：可跳转推送","内容:可跳转推送","channelid2",pendingIntent,11);
    }
    public void onclick_SendMyViewNotification(View view){
        sendMyViewNotification("标题：自定义布局","内容:自定义布局","channelid1",12);
    }
    /**
     * 发送通知的方法
     * @param title 通知标题
     * @param text 通知内容
     * @param channelId 通知channel
     * @param intent 通知点击意图
     * @param id 通知ID
     */
    private void sendNotification(String title, String text, String channelId, PendingIntent intent,int id) {
        //创建通知栏管理工具
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //高版本需要渠道
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //只在Android O之上需要渠道
            NotificationChannel notificationChannel = new NotificationChannel(channelId,"channelname",NotificationManager.IMPORTANCE_HIGH);
            if (manager != null) {
                //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
                manager.createNotificationChannel(notificationChannel);
            }
        }
        //实例化通知栏构造器
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,channelId);
        mBuilder.setContentTitle(title)//设置标题
                .setContentText(text)//设置内容
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))//对于较长文字时的处理方法
//                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.default_icon)))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.default_icon))//设置大图标
                .setSmallIcon(R.drawable.default_icon)//设置小图标,必须设置，否则报错：IllegalArgumentException: Invalid notification (no valid small icon)
                .setWhen(System.currentTimeMillis()) //设置通知时间
                .setTicker(text)//首次进入时显示效果
                .setPriority(NotificationCompat.PRIORITY_MAX)//设置此通知的相对优先级
                .setAutoCancel(true)
                .setContentIntent(intent)
                .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg"))) //设置通知提示音
                .setVibrate(new long[]{0,1000,1000,1000}) //设置振动， 需要添加权限  <uses-permission android:name="android.permission.VIBRATE"/>
                .setLights(Color.GREEN,1000,1000)//设置前置LED灯进行闪烁， 第一个为颜色值  第二个为亮的时长  第三个为暗的时长
                .setDefaults(NotificationCompat.DEFAULT_ALL);  //使用默认效果， 会根据手机当前环境播放铃声， 是否振动
        if (manager != null) {
            //发送通知请求,第一个参数是ID， 保证每个通知所指定的id都是不同的
            manager.notify(id, mBuilder.build());
        }
    }

    private void sendMyViewNotification(String title,String contentText,String channelId,int id){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(channelId,"channelname",NotificationManager.IMPORTANCE_HIGH);
            if (manager != null){
                manager.createNotificationChannel(notificationChannel);
            }
        }
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setImageViewResource(R.id.iv_notification_icon,R.drawable.default_icon);
        remoteViews.setTextViewText(R.id.tv_title,title);
        remoteViews.setTextViewText(R.id.tv_content,contentText);
        remoteViews.setProgressBar(R.id.pb_progress,100,50,false);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.drawable.default_icon)
                .setContent(remoteViews)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        if (manager != null) {
            manager.notify(id,builder.build());
        }
    }



    public void openBackService(View view){
        Intent intent = new Intent(this, ForegroundService.class);
        intent.putExtra("type",1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(intent);
        }
    }

    public void openBeforeService(View view){
        Intent intent = new Intent(this, ForegroundService.class);
        intent.putExtra("type",2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(intent);
        }
    }
}
