package com.example.module_mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.module_mvvm.databinding.ActivityMainBinding;
import com.example.module_mvvm.model.UserRepository;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding viewDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewDataBinding.setUser(UserRepository.getUserProfile());
    }

    public void openUserProfile(View view) {

        startActivity(new Intent(this,ContainerActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewDataBinding.setUser(UserRepository.getUserProfile());
    }
}
