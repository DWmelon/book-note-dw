package com.simplenote.module.oos.callback;

import java.util.List;

/**
 * Created by melon on 2017/8/6.
 */

public interface OnCheckIsSyncListener {

    void onCheckSyncFinish(List<String> needUploadList);

}
