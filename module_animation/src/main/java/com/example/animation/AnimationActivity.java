package com.example.animation;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.animation.fragment.FragmentFactory;
import com.example.animation.fragment.ValueAnimationFragment;
import com.example.animation.fragment.ViewAnimationFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AnimationActivity extends AppCompatActivity{

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
    }

    private void initView() {
        final String[] titles = {"view动画和帧动画","属性动画","lotte动画"};
        TabLayout tabLayout = findViewById(R.id.tab_anim);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager_anim);
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return FragmentFactory.getFragment(position);
            }

            @Override
            public int getItemCount() {
                return titles.length;
            }
        });
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //为activity增加退出动画，在finish（）方法结束之后调用
        overridePendingTransition(R.anim.anim_normal,R.anim.anim_out);
    }
}
