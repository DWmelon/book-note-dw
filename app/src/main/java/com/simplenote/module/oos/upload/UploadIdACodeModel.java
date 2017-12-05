package com.simplenote.module.oos.upload;

/**
 * Created by melon on 2017/8/7.
 */

public class UploadIdACodeModel {

    private String noteId;

    private String noteCode;

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteCode() {
        return noteCode;
    }

    public void setNoteCode(String noteCode) {
        this.noteCode = noteCode;
    }
}
