package com.example.eventlibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventlibrary.bean.MessageEvent;
import com.example.eventlibrary.bean.MessageStickyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventBusSecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_event_bus);
    }


    public void sendMessage(View view) {
        EventBus.getDefault().post(new MessageEvent("土豆土豆，我是地瓜"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);//注册EventBus,为接受黏性事件准备
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);//取消注册EventBus
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(MessageStickyEvent messageEvent){
        Toast.makeText(this,"收到事件："+messageEvent.getEventStr(),Toast.LENGTH_SHORT).show();
        EventBus.getDefault().removeAllStickyEvents();
    }



}
