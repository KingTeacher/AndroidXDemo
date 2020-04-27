package com.example.animation.fragment;


import androidx.fragment.app.Fragment;

public class FragmentFactory {

    public static Fragment getFragment(int position){
        switch (position){
            case 0:
                return new ViewAnimationFragment();
            case 1:
                return new ValueAnimationFragment();
            case 2:
                return new LottieAnimationFragment();
        }
        return new Fragment();
    }
}
