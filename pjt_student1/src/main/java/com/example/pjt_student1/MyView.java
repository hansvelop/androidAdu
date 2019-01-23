package com.example.pjt_student1;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {
    Context context;
    int score;
    int color;

    public MyView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public MyView(Context context, AttributeSet attrs, int styles) {
        super(context, attrs, styles);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs != null){
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AAA);
            color = array.getColor(R.styleable.AAA_scoreColor, Color.YELLOW);
        }
    }

    public void setScore(int score){
        this.score = score;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.alpha(Color.CYAN));
        RectF rectF = new RectF(15,15,70,70);

        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setAntiAlias(true);

        canvas.drawArc(rectF,0,360,false,paint);

        float endAngle = (360*score) / 100;
        paint.setColor(color);

        canvas.drawArc(rectF,-90, endAngle, false, paint);

    }
}
