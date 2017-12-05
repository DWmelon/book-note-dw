package com.simplenote.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by melon on 2017/7/18.
 */

public class CircleImageView extends SimpleDraweeView {

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        RoundingParams roundingParams = RoundingParams.asCircle();
        roundingParams.setRoundAsCircle(true);
        getHierarchy().setRoundingParams(roundingParams);
    }

}
