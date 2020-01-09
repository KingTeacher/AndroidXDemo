package com.example.animation;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.animation.fragment.ValueAnimationFragment;
import com.example.animation.fragment.ViewAnimationFragment;

public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewAnimationFragment viewAnimationFragment;
    private ValueAnimationFragment valueAnimationFragment;
    private TextView tvViewAnim;
    private TextView tvValueAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_in,R.anim.anim_normal);
        //为activity增加进入动画，需在startactivity之后，也可放在当前位置
        setContentView(R.layout.activity_animation);
        initView();
        initData();
    }

    private void initView() {
        tvViewAnim = findViewById(R.id.tv_view_anim);
        tvValueAnim = findViewById(R.id.tv_value_anim);
        tvViewAnim.setOnClickListener(this);
        tvValueAnim.setOnClickListener(this);

    }


    private void initData() {
        tvViewAnim.setSelected(true);
        viewAnimationFragment = new ViewAnimationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //add方法：如果没有，则直接添加一个新的fragment，如果有则重新启动不销毁上一个
        transaction.add(R.id.framly_animat,viewAnimationFragment).commit();
        //replace方法：每次将上一个fragment销毁，再启动新添加的fragment
//        transaction.replace(R.id.framly_animat,viewAnimationFragment).commit();


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_view_anim) {
            tvViewAnim.setSelected(true);
            tvValueAnim.setSelected(false);
            if (viewAnimationFragment == null) {
                viewAnimationFragment = new ViewAnimationFragment();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framly_animat,viewAnimationFragment).commit();
        } else if (id == R.id.tv_value_anim) {
            tvViewAnim.setSelected(false);
            tvValueAnim.setSelected(true);
            if (valueAnimationFragment == null) {
                valueAnimationFragment = new ValueAnimationFragment();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framly_animat,valueAnimationFragment).commit();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //为activity增加退出动画，在finish（）方法结束之后调用
        overridePendingTransition(R.anim.anim_normal,R.anim.anim_out);
    }
}
