package com.example.myview.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myview.R;

public class ColorView extends View {
    public ColorView(Context context) {
        this(context,null);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Paint paint = new Paint();
    }

    private int color = Color.YELLOW;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(color);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                int A = (int) (Math.random()*1000);
                int R = (int) (Math.random()*1000);
                int G = (int) (Math.random()*1000);
                int B = (int) (Math.random()*1000);
                color = Color.argb(A,R,G,B);
                invalidate();
                break;
        }
        return true;
    }
}
