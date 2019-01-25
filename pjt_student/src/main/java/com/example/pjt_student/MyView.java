package com.example.pjt_student;

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

    //View를 activity개발자가 코드에서 직접 생성해서 이용한다..
    //==>생성자는 하나만...
    //layout xml 파일에 등록해서 이용한다면...
    //개발자 코드에 의해 생성되지 않는다.. 상황에 따라 호출되는 생성자 다르다.
    //==>layout xml 등록에 의해 정상적으로 이용되게 하려면 생성자 3개 모두 정의
    public MyView(Context context){
        super(context);
        this.context=context;
        init(null);
    }
    //속성 설정이 되어 있을때 호출..
    public MyView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context=context;
        init(attrs);
    }
    //style 까지 선언되어 있을때..
    public MyView(Context context, AttributeSet attrs, int styles){
        super(context, attrs, styles);
        this.context=context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs != null){
            TypedArray array=context.obtainStyledAttributes(
                    attrs, R.styleable.AAA);
            color=array.getColor(R.styleable.AAA_scoreColor,
                    Color.YELLOW);
        }
    }
    //activity에서 score 전달 목적으로 호출..
    public void setScore(int score){
        this.score=score;
        //새로운 score가 전달된거다.. 그림 다시 그려야 한다..
        invalidate();//자동으로 onDraw() 호출된다..
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //화면 지우고..
        canvas.drawColor(Color.alpha(Color.CYAN));
        //원을 그리기 위한 사각형 정보..
        RectF rectF=new RectF(15, 15, 70, 70);

        //그리기 옵션..
        Paint paint=new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setAntiAlias(true);

        //기본 원 360도로 그리고..
        canvas.drawArc(rectF, 0, 360,
                false, paint);
        //시험 점수에 해당되는 각도 계산..
        float endAngle=(360*score)/100;
        paint.setColor(color);
        //점수 원 그린다..
        //동쪽이 0이다.. 북쪽 부터 그릴려고 -90
        canvas.drawArc(rectF, -90, endAngle, false,
                paint);

    }
}
