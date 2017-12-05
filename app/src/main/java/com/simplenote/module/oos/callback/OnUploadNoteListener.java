package com.simplenote.module.oos.callback;

/**
 * Created by melon on 2017/8/6.
 */

public interface OnUploadNoteListener {

    void onUploadNoteFinish(boolean isSuccess,int noteIndex,int status);

}
