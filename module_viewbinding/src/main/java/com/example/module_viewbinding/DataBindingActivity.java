package com.example.module_viewbinding;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.example.module_viewbinding.bean.Scores;
import com.example.module_viewbinding.bean.User;
import com.example.module_viewbinding.databinding.ActivityDataBinding;

public class DataBindingActivity extends AppCompatActivity {

    private ViewDataBinding dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDataBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_data);
//        以下适用于ListView或RecycleView
//        或
//        ActivityDataBinding viewDataBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_data,viewGroup,false);
//        setContentView(viewDataBinding.getRoot());
//        或
//        ActivityDataBinding viewDataBinding = ActivityDataBinding.inflate(getLayoutInflater(), ViewGroup,false);
//        setContentView(viewDataBinding.getRoot());


        User user = new User("花千骨","1112","女",18,"天宫");
        Scores scores = new Scores(89.2f,99,59);
        viewDataBinding.setUser(user);
        viewDataBinding.setScores(scores);

    }
}
