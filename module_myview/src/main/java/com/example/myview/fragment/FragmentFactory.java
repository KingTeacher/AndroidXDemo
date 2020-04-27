package com.example.myview.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.example.myview.R;
import com.example.myview.view.CircleView;
import com.example.myview.view.ColorView;

public class FragmentFactory {

    public static Fragment getViewFragment(int position, Context context){
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new ViewFragment(new ColorView(context));
                break;
            case 1:
                fragment = new ViewFragment(new CircleView(context));
                break;
            case 2:
                fragment = new ViewFragment(new ColorView(context));
                break;
            case 3:
                fragment = new ViewFragment(new ColorView(context));
                break;
            case 4:
                fragment = new ViewFragment(new ColorView(context));
                break;
            case 5:
                fragment = new ViewFragment(new ColorView(context));
                break;
            default:
                fragment = new Fragment();
        }

        return fragment;
    }
}
