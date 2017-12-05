package com.simplenote.module.login;

import com.alibaba.fastjson.JSONObject;
import com.simplenote.model.BaseModel;

/**
 * Created by melon on 2017/8/11.
 */

public class RegisterResultModel extends BaseModel {

    private final String KEY_TOKEN = "token";
    private final String KEY_USER_ID = "userId";
    private final String KEY_NICK_NAME = "nickName";
    private final String KEY_MAX_IMAGE_COUNT = "maxImageCount";
    private final String KEY_AVATAR_PATH = "avatarPath";
    private final String KEY_BACKDROP_PATH = "backdropPath";



    private String token;

    private Long userId;

    private String nickName;

    private int maxImageCount;

    private String avatarPath;

    private String backdropPath;

    @Override
    public void decode(JSONObject object) {
        super.decode(object);
        if (object == null){
            return;
        }

        object = object.getJSONObject(KEY_DATA);
        if (object == null){
            return;
        }

        token = object.getString(KEY_TOKEN);
        userId = object.getLongValue(KEY_USER_ID);
        nickName = object.getString(KEY_NICK_NAME);
        maxImageCount = object.getIntValue(KEY_MAX_IMAGE_COUNT);
        avatarPath = object.getString(KEY_AVATAR_PATH);
        backdropPath = object.getString(KEY_BACKDROP_PATH);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getMaxImageCount() {
        return maxImageCount;
    }

    public void setMaxImageCount(int maxImageCount) {
        this.maxImageCount = maxImageCount;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
}
