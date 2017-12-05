package com.simplenote.model;

import android.content.Context;

import com.simplenote.util.ImageUtils;

/**
 * Created by melon on 2017/7/18.
 */

public class ImageModel {

    private int bigPress;
    private int bigUnPress;

    private int smallPress;
    private int smallUnPress;

    private int extraBig;
    private int extraSmall;

    private String selectBigStr;

    public ImageModel(Context context,int i, int j){
        String indexI = String.valueOf(i);
        String indexJ = "";

        if (j<10){
            indexJ += "0";
        }

        indexJ += j;

        String baseStr = "c"+indexI+"_emoticon"+indexJ;

        bigPress = ImageUtils.getResource(baseStr + "_s", context);
        bigUnPress = ImageUtils.getResource(baseStr + "_n", context);

        smallPress = ImageUtils.getResource(baseStr + "_s2", context);
        smallUnPress = ImageUtils.getResource(baseStr + "_n2", context);

        extraBig = ImageUtils.getResource(baseStr + "s", context);
        extraSmall = ImageUtils.getResource(baseStr + "s2", context);

        selectBigStr = "selector_c"+indexI+"_"+indexJ;

    }

    public int getBigPress() {
        return bigPress;
    }

    public void setBigPress(int bigPress) {
        this.bigPress = bigPress;
    }

    public int getBigUnPress() {
        return bigUnPress;
    }

    public void setBigUnPress(int bigUnPress) {
        this.bigUnPress = bigUnPress;
    }

    public int getSmallPress() {
        return smallPress;
    }

    public void setSmallPress(int smallPress) {
        this.smallPress = smallPress;
    }

    public int getSmallUnPress() {
        return smallUnPress;
    }

    public void setSmallUnPress(int smallUnPress) {
        this.smallUnPress = smallUnPress;
    }

    public int getExtraBig() {
        return extraBig;
    }

    public void setExtraBig(int extraBig) {
        this.extraBig = extraBig;
    }

    public int getExtraSmall() {
        return extraSmall;
    }

    public void setExtraSmall(int extraSmall) {
        this.extraSmall = extraSmall;
    }

    public String getSelectBigStr() {
        return selectBigStr;
    }

    public void setSelectBigStr(String selectBigStr) {
        this.selectBigStr = selectBigStr;
    }
}
