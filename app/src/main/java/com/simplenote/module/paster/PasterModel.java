package com.simplenote.module.paster;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by melon on 2017/11/1.
 */

public class PasterModel {

    private final String KEY_PASTER_ID = "pasterId";
    private final String KEY_PASTER_NAME = "pasterName";
    private final String KEY_PASTER_WIDTH = "pasterWidth";
    private final String KEY_PASTER_HEIGHT = "pasterHeight";
    private final String KEY_PASTER_URL = "pasterUrl";
    private final String KEY_PASTER_REMARK = "pasterRemark";
    private final String KEY_PASTER_FAVOR = "pasterFavor";
    private final String KEY_IS_FAVOR = "isFavor";


    private Long pasterId;
    private String pasterName;
    private Integer pasterWidth;
    private Integer pasterHeight;
    private String pasterUrl;
    private String pasterRemark;
    private Integer pasterFavor;
    private boolean isFavor;

    public void decode(JSONObject object){
        if (object == null){
            return;
        }

        pasterId = object.getLongValue(KEY_PASTER_ID);
        pasterName = object.getString(KEY_PASTER_NAME);
        pasterWidth = object.getIntValue(KEY_PASTER_WIDTH);
        pasterHeight = object.getIntValue(KEY_PASTER_HEIGHT);
        pasterUrl = object.getString(KEY_PASTER_URL);
        pasterRemark = object.getString(KEY_PASTER_REMARK);
        pasterFavor = object.getIntValue(KEY_PASTER_FAVOR);
        isFavor = object.getBooleanValue(KEY_IS_FAVOR);

    }

    public Long getPasterId() {
        return pasterId;
    }

    public void setPasterId(Long pasterId) {
        this.pasterId = pasterId;
    }

    public String getPasterName() {
        return pasterName;
    }

    public void setPasterName(String pasterName) {
        this.pasterName = pasterName;
    }

    public Integer getPasterWidth() {
        return pasterWidth;
    }

    public void setPasterWidth(Integer pasterWidth) {
        this.pasterWidth = pasterWidth;
    }

    public Integer getPasterHeight() {
        return pasterHeight;
    }

    public void setPasterHeight(Integer pasterHeight) {
        this.pasterHeight = pasterHeight;
    }

    public String getPasterUrl() {
        return pasterUrl;
    }

    public void setPasterUrl(String pasterUrl) {
        this.pasterUrl = pasterUrl;
    }

    public String getPasterRemark() {
        return pasterRemark;
    }

    public void setPasterRemark(String pasterRemark) {
        this.pasterRemark = pasterRemark;
    }

    public Integer getPasterFavor() {
        return pasterFavor;
    }

    public void setPasterFavor(Integer pasterFavor) {
        this.pasterFavor = pasterFavor;
    }

    public boolean isFavor() {
        return isFavor;
    }

    public void setFavor(boolean favor) {
        isFavor = favor;
    }
}
