package com.simplenote.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.simplenote.R;


/**
 * Created by eddy on 2015/4/27.
 */
public class RedPointImageView extends android.support.v7.widget.AppCompatImageView {

    private String mTag;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mRect = new Rect();
    private Drawable mRedPoint = null;
    private int radius;
    private boolean mShow = false;

    public RedPointImageView(Context context) {
        super(context);
        init();
    }

    public RedPointImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RedPointImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mPaint.setFilterBitmap(true);
        radius = getResources().getDimensionPixelSize(R.dimen.margin_8);
        mRedPoint = getResources().getDrawable(R.drawable.icon_red_point);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateRect(w, h);
    }

    private void updateRect(int w, int h) {
        int left = (int) (w * 0.615f);
        int top = (int) (h * 0.15f);
        int right = left + radius;
        int bottom = top + radius;
        mRect.set(left, top, right, bottom);

    }

    public void setRedPoint(Drawable drawable) {
        this.mRedPoint = drawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mShow) {
            drawRedPoint(canvas);
        }
    }

    private void drawRedPoint(Canvas canvas) {
        if (mRedPoint == null) {
            return;
        }

        canvas.save();
//        canvas.clipRect(mRect, Region.Op.DIFFERENCE);
        mRedPoint.setBounds(mRect);
        mRedPoint.draw(canvas);
        canvas.restore();
    }

    public void setShow(boolean isShow){
        this.mShow = isShow;
        invalidate();
    }

    public boolean isShow(){
        return mShow;
    }

    public String getmTag() {
        return mTag;
    }

    public void setmTag(String mTag) {
        this.mTag = mTag;
    }

}
