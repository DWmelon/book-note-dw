package com.simplenote.module.account;

import android.text.TextUtils;

import com.simplenote.application.MyClient;
import com.simplenote.module.login.LoginManager;
import com.simplenote.module.manager.StorageManager;
import com.simplenote.module.manager.TaskExecutor;
import com.simplenote.util.FileUtil;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by melon on 2017/7/20.
 */

public class AccountManager {

    private static final String mFileName = "userInfoData.dat";

    private UserInfo userInfo;

    public void init(){
        getUserInfoFromFile();
    }

    private void checkInfoObj(){
        if (userInfo == null){
            userInfo = new UserInfo();
            MyClient.getMyClient().getLoginManager().setLogin(false);
        }
    }

    public Long getUserId(){
        checkInfoObj();
        return userInfo.getUid();
    }

    public void setUserId(Long userId,boolean isSaveData){
        checkInfoObj();
        userInfo.setUid(userId);
        if (isSaveData){
            saveUserInfoToFile();
        }
    }

    public String getAccount(){
        checkInfoObj();
        return userInfo.getAccount();
    }

    public void setAccount(String account,boolean isSaveData){
        checkInfoObj();
        userInfo.setAccount(account);
        if (isSaveData){
            saveUserInfoToFile();
        }
    }

    public int getMaxImageCount(){
        checkInfoObj();
        return userInfo.getMaxImageCount();
    }

    public void setMaxImageCount(int count,boolean isSaveData){
        checkInfoObj();
        userInfo.setMaxImageCount(count);
        if (isSaveData){
            saveUserInfoToFile();
        }
    }

    public String getToken(){
        checkInfoObj();
        return userInfo.getToken();
    }

    public void setToken(String token,boolean isSaveData){
        checkInfoObj();
        userInfo.setToken(token);
        if (isSaveData){
            saveUserInfoToFile();
        }
    }

    public String getNickName(){
        checkInfoObj();
        return userInfo.getNickName();
    }

    public void setNickName(String nickName,boolean isSaveData){
        checkInfoObj();
        userInfo.setNickName(nickName);
        if (isSaveData){
            saveUserInfoToFile();
        }
    }

    public String getAvatarPath(){
        checkInfoObj();
        return userInfo.getAvatarPath();
    }

    public void setAvatarPath(String avatar,boolean isSaveData){
        checkInfoObj();
        userInfo.setAvatarPath(avatar);
        if (isSaveData){
            saveUserInfoToFile();
        }
    }

    public String getBackdropPath(){
        checkInfoObj();
        return userInfo.getBackdropPath();
    }

    public void setBackdropPath(String backdrop,boolean isSaveData){
        checkInfoObj();
        userInfo.setBackdropPath(backdrop);
        if (isSaveData){
            saveUserInfoToFile();
        }
    }

    public void saveUserInfoToFile(){

        TaskExecutor.getInstance().post(new Runnable() {
            @Override
            public void run() {
                if (userInfo != null) {
                    String path = MyClient.getMyClient().getStorageManager().getPackageFiles() + mFileName;
                    FileUtil.writeObjectToPath(userInfo, path);
                }
            }
        });
    }

    public void getUserInfoFromFile(){
        Observable<UserInfo> observable = Observable.create(new Observable.OnSubscribe<UserInfo>() {
            @Override
            public void call(Subscriber<? super UserInfo> subscriber) {
                String path = MyClient.getMyClient().getStorageManager().getPackageFiles() + mFileName;
                Object oj = FileUtil.readObjectFromPath(path);
                UserInfo userInfo;
                if (oj == null){
                    userInfo = new UserInfo();
                }else{
                    userInfo = (UserInfo)oj;
                }
                subscriber.onNext(userInfo);
                subscriber.onCompleted();
            }
        });
        Observer<UserInfo> observer = new Observer<UserInfo>() {
            @Override
            public void onCompleted() {
                //初始化用户相关路径
                MyClient.getMyClient().getStorageManager().initAllDir();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UserInfo ui) {
                if (TextUtils.isEmpty(ui.getToken())){
                    MyClient.getMyClient().getLoginManager().setLogin(false);
                }else{
                    MyClient.getMyClient().getLoginManager().setLogin(true);
                }
                userInfo = ui;
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


//        TaskExecutor.getInstance().post(new Runnable() {
//            @Override
//            public void run() {
//                String path = MyClient.getMyClient().getStorageManager().getPackageFiles() + mFileName;
//
//                Object oj = FileUtil.readObjectFromPath(path);
//                if (oj != null){
//                    userInfo = (UserInfo)oj;
//                    if (!TextUtils.isEmpty(userInfo.getToken())){
//                        MyClient.getMyClient().getLoginManager().setLogin(true);
//                    }else{
//                        userInfo = new UserInfo();
//                        MyClient.getMyClient().getLoginManager().setLogin(false);
//                    }
//                }else{
//                    userInfo = new UserInfo();
//                    MyClient.getMyClient().getLoginManager().setLogin(false);
//                }
//
//                //初始化用户相关路径
//                MyClient.getMyClient().getStorageManager().initAllDir();
//            }
//        });
    }

    public void logout(){
        userInfo = new UserInfo();
        saveUserInfoToFile();
    }

}
