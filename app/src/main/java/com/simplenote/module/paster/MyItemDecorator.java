package com.simplenote.module.paster;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by melon on 2017/11/1.
 */

public class MyItemDecorator extends RecyclerView.ItemDecoration {

    private int spaceLeft;
    private int spaceRight;
    private int spaceTop;
    private int spaceBottom;

    public MyItemDecorator(int spaceL,int spaceR,int spaceT,int spaceB) {
        this.spaceLeft = spaceL;
        this.spaceRight = spaceR;
        this.spaceTop = spaceT;
        this.spaceBottom = spaceB;
    }

    //自定义item之间的距离，如果是第一个的话就没有距离，
    //设置上下左右的距离
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom=spaceBottom;

        if(parent.getChildPosition(view)!=0){
            outRect.top=spaceTop;
        }

        if (parent.getChildPosition(view)%2==1){
            outRect.right=spaceLeft;
            outRect.left=spaceRight;
        }else{
            outRect.right=spaceRight;
            outRect.left=spaceLeft;
        }

    }


}
