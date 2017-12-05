package com.simplenote.module.account;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by melon on 2017/7/22.
 */

public class UserInfo implements Serializable {

    private static final long serialVersionUID = -8521058429899657508L;

    private Long uid = 1L;
    private String token = "";
    private String nickName = "";
    private int maxImageCount = 3;

    private String avatarPath = "http://v1.qzone.cc/avatar/201310/12/15/42/5258fd6f0db4b914.jpg%21200x200.jpg";
    private String backdropPath;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getMaxImageCount() {
        return maxImageCount>6?6:maxImageCount;
    }

    public void setMaxImageCount(int maxImageCount) {
        this.maxImageCount = maxImageCount;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
