package com.simplenote.module.pic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/9/26.
 */

public class PicPathModel implements Serializable {

    private static final long serialVersionUID = -720925239399006098L;

    private List<String> imagePathList = new ArrayList<>();

    public List<String> getImagePathList() {
        return imagePathList;
    }

    public void setImagePathList(List<String> imagePathList) {
        this.imagePathList = imagePathList;
    }

}
