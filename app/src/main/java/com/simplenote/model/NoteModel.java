package com.simplenote.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by melon on 2017/1/3.
 */

public class NoteModel extends BaseModel implements Serializable{

    private static final long serialVersionUID = 4989859815859221157L;

    private final String KEY_NOTE_ID = "noteId";
    private final String KEY_NOTE_CODE = "noteCode";
    private final String KEY_NOTE_TITLE = "title";
    private final String KEY_NOTE_CONTENT = "content";
    private final String KEY_NOTE_C_DATE = "createDate";
    private final String KEY_NOTE_M_DATE = "modifyDate";
    private final String KEY_NOTE_EMOTION = "emotion";
    private final String KEY_NOTE_WEATHER = "weather";
    private final String KEY_NOTE_THEME = "theme";
    private final String KEY_NOTE_IMAGE_NAME_PATH = "imagePath";
    private final String KEY_NOTE_USER_ID = "userId";

    private final String KEY_INFO = "info";

    private String noteId;

    private String noteCode;//每次修改都要重新set一个新的code

    private String title;

    private String content;

    private Long createDate;

    private Long modifyDate;

    private String emotion;

    private String weather;

    private String theme;

    private List<String> imageNameList = new ArrayList<>();

    private Long userId;

    private int status = 0;//-1标示已删除

    public void decode(JSONObject object){
        super.decode(object);
        if (object == null){
            return;
        }

        object = object.getJSONObject(KEY_DATA);
        if (object == null){
            return;
        }

        object = object.getJSONObject(KEY_INFO);

        noteId = object.getString(KEY_NOTE_ID);
        noteCode = object.getString(KEY_NOTE_CODE);
        title = object.getString(KEY_NOTE_TITLE);
        content = object.getString(KEY_NOTE_CONTENT);
        createDate = object.getLongValue(KEY_NOTE_C_DATE);
        modifyDate = object.getLongValue(KEY_NOTE_M_DATE);
        emotion = object.getString(KEY_NOTE_EMOTION);
        weather = object.getString(KEY_NOTE_WEATHER);
        theme = object.getString(KEY_NOTE_THEME);
        userId = object.getLongValue(KEY_NOTE_USER_ID);

        imageNameList.clear();
        JSONArray array = object.getJSONArray(KEY_NOTE_IMAGE_NAME_PATH);
        if (array!=null){
            for (int i = 0;i < array.size();i++){
                imageNameList.add(array.getString(i));
            }
        }


    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
        this.modifyDate = createDate;
    }

    public Long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImageNameList() {
        return imageNameList;
    }

    public void setImageNameList(List<String> imageNameList) {
        this.imageNameList = imageNameList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public NoteModel clone(){
        NoteModel model = new NoteModel();
        model.setNoteId(noteId);
        model.setNoteCode(noteCode);
        model.setTitle(title);
        model.setContent(content);
        model.setCreateDate(createDate);
        model.setModifyDate(modifyDate);
        model.setEmotion(emotion);
        model.setWeather(weather);
        model.setTheme(theme);
        model.setUserId(userId);
        model.setStatus(status);

        List<String> imageList = new ArrayList<>();
        imageList.addAll(imageNameList);
        model.setImageNameList(imageList);

        return model;
    }

    @Override
    public String toString() {
        return "NoteModel{" +
                "noteId='" + noteId + '\'' +
                ", noteCode='" + noteCode + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", emotion='" + emotion + '\'' +
                ", weather='" + weather + '\'' +
                ", theme='" + theme + '\'' +
                ", imageNameList=" + imageNameList +
                ", userId=" + userId +
                '}';
    }
}
