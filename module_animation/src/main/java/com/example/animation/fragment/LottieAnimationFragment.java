package com.example.animation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.animation.R;


public class LottieAnimationFragment extends Fragment {

    private final String TAG = "ValueAnimationFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lotte_animation,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        final LottieAnimationView lottie1 = view.findViewById(R.id.lottie1);
        lottie1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lottie1.playAnimation();
            }
        });

    }

}
