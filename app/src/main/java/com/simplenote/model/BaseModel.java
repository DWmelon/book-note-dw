package com.simplenote.model;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by melon on 2017/8/3.
 */

public class BaseModel {

    protected final String KEY_CODE = "code";
    protected final String KEY_MSG = "msg";
    protected final String KEY_DATA = "data";

    private int code = -1;
    private String msg;

    public void decode(JSONObject object){
        if (object == null){
            return;
        }
        code = object.getIntValue(KEY_CODE);
        msg = object.getString(KEY_MSG);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "KEY_CODE='" + KEY_CODE + '\'' +
                ", KEY_MSG='" + KEY_MSG + '\'' +
                ", KEY_DATA='" + KEY_DATA + '\'' +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
