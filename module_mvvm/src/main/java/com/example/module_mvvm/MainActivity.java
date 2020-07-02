package com.example.module_mvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.module_mvvm.model.UserRepository;
import com.example.module_mvvm.model.bean.UserProfile;

public class MainActivity extends AppCompatActivity {
    int i;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String name = UserRepository.getUserProfile().getName();
            UserRepository.getUserProfile().setName(name+ i++);
            UserRepository.getUserProfile().setAge(""+ i++);
            sendEmptyMessageDelayed(1,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openUserProfile(View view) {
        handler.sendEmptyMessageDelayed(1,1000);

        startActivity(new Intent(this,ContainerActivity.class));
    }
}
