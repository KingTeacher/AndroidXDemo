package com.example.module_viewbinding;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class MyView extends androidx.appcompat.widget.AppCompatTextView {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextName(int color){

    }
}
