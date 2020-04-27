package com.example.myview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myview.fragment.FragmentFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyViewActivity extends AppCompatActivity {

    private String[] titles = {"drawColor","drawCircle","drawRect","drawArc","drawPath","直方图","饼图"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myview);
        initVIew();
    }

    private void initVIew() {
        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager2 viewPager = findViewById(R.id.viewpager2);
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return FragmentFactory.getViewFragment(position,MyViewActivity.this);
            }

            @Override
            public int getItemCount() {
                return 5;
            }
        });
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();

    }
}
