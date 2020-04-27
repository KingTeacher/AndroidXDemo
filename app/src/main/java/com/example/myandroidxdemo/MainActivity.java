package com.example.myandroidxdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.animation.AnimationActivity;
import com.example.eventlibrary.EventBusActivity;
import com.example.module_webview.JsBridgeActivity;
import com.example.myview.MyViewActivity;
import com.example.servicelibrary.OpenNotificationAndServiceActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void startActivitys(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }

    public void onclickAnim(View view) {
        startActivitys(AnimationActivity.class);
    }

    public void onclickEventBus(View view) {
        startActivitys(EventBusActivity.class);
    }


    public void onclickService(View view) {
        startActivitys(OpenNotificationAndServiceActivity.class);
    }

    public void onclickMyview(View view) {
        startActivitys(MyViewActivity.class);
    }

    public void onclickwebview(View view) {
        startActivitys(JsBridgeActivity.class);
    }
}
