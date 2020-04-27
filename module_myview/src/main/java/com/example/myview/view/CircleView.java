package com.example.myview.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleView extends View {

    private Paint paint;

    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(300,300,200,paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawCircle(750,300,200,paint);
        paint.setColor(Color.BLUE);
        Path path = new Path();
        path.addCircle(300,750,200, Path.Direction.CW);
        path.addCircle(750,750,200, Path.Direction.CW);
        path.addCircle(750,750,100, Path.Direction.CW);
        path.setFillType(Path.FillType.EVEN_ODD);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path,paint);
    }

}
