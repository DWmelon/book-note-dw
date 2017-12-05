package com.simplenote.module.oos.upload;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.simplenote.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/8/6.
 */

public class OSSCheckSyncModel extends BaseModel {

    private static final String KEY_ID_LIST = "idList";

    private List<String> idList = new ArrayList<>();

    public void decode(JSONObject object){
        super.decode(object);
        if (object == null){
            return;
        }

        object = object.getJSONObject(KEY_DATA);
        if (object == null){
            return;
        }

        JSONArray arr = object.getJSONArray(KEY_ID_LIST);
        if (arr == null){
            return;
        }

        for (int i = 0;i < arr.size(); i++){
            idList.add(arr.getString(i));
        }

    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    @Override
    public String toString() {
        return "OSSCheckSyncModel{" +
                "idList=" + idList +
                "} " + super.toString();
    }
}
