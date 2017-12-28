package com.simplenote.database.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.simplenote.model.BaseModel;
import com.simplenote.util.V2ArrayUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by melon on 2017/12/26.
 */
@Entity
public class Note extends BaseModel implements Serializable{

    private static final long serialVersionUID = 2545309922995926512L;

    @Transient
    private final String KEY_NOTE_ID = "noteId";
    @Transient
    private final String KEY_NOTE_CODE = "noteCode";
    @Transient
    private final String KEY_NOTE_TITLE = "title";
    @Transient
    private final String KEY_NOTE_CONTENT = "content";
    @Transient
    private final String KEY_NOTE_C_DATE = "createDate";
    @Transient
    private final String KEY_NOTE_M_DATE = "modifyDate";
    @Transient
    private final String KEY_NOTE_EMOTION = "emotion";
    @Transient
    private final String KEY_NOTE_WEATHER = "weather";
    @Transient
    private final String KEY_NOTE_THEME = "theme";
    @Transient
    private final String KEY_NOTE_IMAGE_NAME_PATH = "imagePath";
    @Transient
    private final String KEY_NOTE_USER_ID = "userId";
    @Transient
    private final String KEY_INFO = "info";

    @Id
    private String noteId;

    private String noteCode;//每次修改都要重新set一个新的code

    private String title;

    private String content;

    private Long createDate;

    private Long modifyDate;

    private String emotion;

    private String weather;

    private String theme;

    private String imageNameList;

    private Long userId;

    private int status = 0;//-1标示已删除

    @Generated(hash = 1389674387)
    public Note(String noteId, String noteCode, String title, String content,
            Long createDate, Long modifyDate, String emotion, String weather,
            String theme, String imageNameList, Long userId, int status) {
        this.noteId = noteId;
        this.noteCode = noteCode;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.emotion = emotion;
        this.weather = weather;
        this.theme = theme;
        this.imageNameList = imageNameList;
        this.userId = userId;
        this.status = status;
    }

    @Generated(hash = 1272611929)
    public Note() {
    }

    public String getNoteId() {
        return this.noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteCode() {
        return this.noteCode;
    }

    public void setNoteCode(String noteCode) {
        this.noteCode = noteCode;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(Long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getEmotion() {
        return this.emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getWeather() {
        return this.weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTheme() {
        return this.theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getImageNameList() {
        return this.imageNameList;
    }

    public void setImageNameList(String imageNameList) {
        this.imageNameList = imageNameList;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getImageList(){
        return V2ArrayUtil.getListByJson(imageNameList);
    }

    public void addImage(String image){
        if (TextUtils.isEmpty(image)){
            return;
        }
        List<String> list = V2ArrayUtil.getListByJson(imageNameList);
        list.add(image);
        setImageNameList(V2ArrayUtil.getJsonArrData(list));
    }

    public Note clone(){
        Note model = new Note();
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
        model.setImageNameList(imageNameList);

        return model;
    }

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
        imageNameList = object.getString(KEY_NOTE_IMAGE_NAME_PATH);

    }



//    public void decode(NoteModel model){
//        this.noteId = model.getNoteId();
//        this.noteCode = model.getNoteCode();
//        this.title = model.getTitle();
//        this.content = model.getContent();
//        this.createDate = model.getCreateDate();
//        this.modifyDate = model.getModifyDate();
//        this.emotion = model.getEmotion();
//        this.weather = model.getWeather();
//        this.theme = model.getTheme();
//        this.imageNameList = V2ArrayUtil.getJsonArrData(model.getImageNameList());
//        this.userId = model.getUserId();
//        this.status = model.getStatus();
//    }
//
//    public NoteModel copy(){
//        NoteModel model = new NoteModel();
//        model.setNoteId(noteId);
//        model.setNoteCode(noteCode);
//        model.setTitle(title);
//        model.setContent(content);
//        model.setCreateDate(createDate);
//        model.setModifyDate(modifyDate);
//        model.setEmotion(emotion);
//        model.setWeather(weather);
//        model.setTheme(theme);
//        model.setImageNameList(V2ArrayUtil.getListByJson(imageNameList));
//        model.setUserId(userId);
//        model.setStatus(status);
//        return model;
//    }

}
