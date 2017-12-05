package com.simplenote.model;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by melon on 2017/8/3.
 */

public class UploadResultModel extends BaseModel {

    private final String KEY_ID = "id";

    private String id;

    public void decode(JSONObject object){
        if (object == null){
            return;
        }
        object = object.getJSONObject(KEY_DATA);

        if (object == null){
            return;
        }

        id = object.getString(KEY_ID);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UploadResultModel{" +
                "KEY_ID='" + KEY_ID + '\'' +
                ", id='" + id + '\'' +
                "} " + super.toString();
    }
}
