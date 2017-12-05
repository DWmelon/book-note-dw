package com.simplenote.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.simplenote.R;


/**
 * Created by melon on 15/5/11.
 */
public class TopLineLinearLayout extends LinearLayout {


    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mColor;


    public TopLineLinearLayout(Context context) {
        super(context);
        init();
    }

    public TopLineLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopLineLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.margin_line));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mColor = getResources().getColor(R.color.primary_line);
        mPaint.setColor(mColor);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawLine(canvas);

    }

    public int layoutCount = 0;
    public int drawCount = 0;

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        layoutCount = 0;
        drawCount = 0;
    }




    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        layoutCount++;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCount++;
    }

    public int getLayoutCount(){
        return layoutCount;
    }

    public int getDrawCount(){
        return drawCount;
    }

    private void drawLine(Canvas canvas) {
        canvas.save();
        canvas.clipRect(getPaddingLeft(),0,getWidth()-getPaddingRight(),mPaint.getStrokeWidth());
        canvas.drawLine(getPaddingLeft(), 0, getWidth()-getPaddingRight(), 0, mPaint);
        canvas.restore();
    }


}
