package com.simplenote.module.home;

import com.alibaba.fastjson.JSONObject;
import com.simplenote.model.BaseModel;

/**
 * Created by melon on 2017/12/22.
 */

public class SyncTimeModel extends BaseModel {

    private final String KEY_SYNC_TIME = "sync_time";

    private long syncTime;

    public void decode(JSONObject object){
        super.decode(object);
        if (object == null){
            return;
        }

        object = object.getJSONObject(KEY_DATA);
        if (object == null){
            return;
        }

        syncTime = object.getLongValue(KEY_SYNC_TIME);

    }

    public long getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(long syncTime) {
        this.syncTime = syncTime;
    }
}
