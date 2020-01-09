package com.example.eventlibrary;

import android.content.Intent;
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

public class EventBusActivity extends AppCompatActivity {
    /**
     * eventbus：
     * 官方git：https://github.com/greenrobot/EventBus
     * 博客：https://www.jianshu.com/p/f9ae5691e1bb
     * 使用方法：
     * 1.在build中添加依赖：implementation 'org.greenrobot:eventbus:3.1.1'
     * 2.创建一个传递消息的实体类，在类中添加传递消息的属性及相关构造方法，如MessageEvent.class
     * 3.在接受消息的地方：在对应的方法里调用注册EventBus和对应的取消注册，EventBus.getDefault().register（）和 EventBus.getDefault().unregister(），
     *   并在该处定义一个接受信息的方法，方法名自定义，
     *   @Subscribe(threadMode = ThreadMode.MAIN)
     *   public void onMessageEvent(MessageEvent messageEvent){}
     * 4.在发送消息的地方：调用发送的方法：EventBus.getDefault().post(new MessageEvent(））
     *
     * @param savedInstanceState 注册eventBus
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        EventBus.getDefault().register(this);//注册事件

    }

    /**
     * 增加接受消息的方法，增加注解并设置对应处理事情线程
     * @param messageEvent 承载消息的实体类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent){
        Toast.makeText(this,"收到发来的普通消息："+messageEvent.getEventStr(),Toast.LENGTH_SHORT).show();
    }

    /**
     * 取消注册时间
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册事件
    }

    /**
     * 打开下一个页面
     */
    public void openNext(View view) {
        startActivity(new Intent(this, EventBusSecondActivity.class));
    }

    /**
     * 发送黏性事件
     * 黏性事件：所谓粘性事件，就是在发送事件之后再订阅该事件也能收到该事件。请注意这里与普通事件的区别，普通事件是先注册在绑定。
     *
     */
    public void sendStickyMessage(View view){
        EventBus.getDefault().postSticky(new MessageStickyEvent("我是一个黏性事件"));
    }




}
