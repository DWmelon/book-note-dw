package com.simplenote.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by melon on 2017/10/31.
 */

public class DragTextView extends android.support.v7.widget.AppCompatTextView{

    private int widthUI;
    private int heightUI;

    public DragTextView(Context context) {
        super(context);
    }

    public DragTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUISize(int widthUI,int heightUI){
        this.widthUI = widthUI;
        this.heightUI = heightUI;
    }

    int top,bottom,left,right;
    float x,y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_DOWN){
            top = getTop();
            bottom = getBottom();
            left = getLeft();
            right = getRight();
            x = event.getX();
            y = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            float diffX = event.getX()-x;
            float diffY = event.getY()-y;
            float topN = top + diffY;
            float bottomN = bottom + diffY;
            float leftN = left + diffX;
            float rightN = right + diffX;
            if (topN <0){
                bottomN += Math.abs(topN);
                topN = 0;
            }
            if (bottomN>heightUI){
                topN -= (bottomN - heightUI);
                bottomN = heightUI;
            }

            if (leftN<0){
                rightN += Math.abs(leftN);
                leftN = 0;
            }

            if (rightN > widthUI){
                leftN -= (rightN - widthUI);
                rightN = widthUI;
            }

            layout((int)leftN,(int)topN,(int)rightN,(int)bottomN);
        }
        if (event.getAction() == MotionEvent.ACTION_UP){

        }


        return true;
    }
}
