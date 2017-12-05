package com.simplenote.module.paster;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.simplenote.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/11/1.
 */

public class PasterListResult extends BaseModel {

    private final String KEY_LIST = "paster_list";

    private List<PasterModel> modelList = new ArrayList<>();

    public void decode(JSONObject object){
        super.decode(object);
        if (object == null){
            return;
        }

        object = object.getJSONObject(KEY_DATA);
        if (object == null){
            return;
        }

        JSONArray array = object.getJSONArray(KEY_LIST);
        if (array == null){
            return;
        }

        modelList.clear();
        for (int i = 0;i < array.size();i++){
            JSONObject obj = array.getJSONObject(i);
            PasterModel model = new PasterModel();
            model.decode(obj);
            modelList.add(model);
        }

    }

    public List<PasterModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<PasterModel> modelList) {
        this.modelList = modelList;
    }
}
