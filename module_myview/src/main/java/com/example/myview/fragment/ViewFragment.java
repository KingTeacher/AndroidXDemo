package com.example.myview.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewFragment extends Fragment {

    View  view;

    public ViewFragment(View view) {
        this.view = view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null){
            return view;
        }else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
