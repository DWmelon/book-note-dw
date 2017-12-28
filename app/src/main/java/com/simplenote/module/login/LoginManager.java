package com.simplenote.module.login;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.GlobalPreferenceManager;
import com.simplenote.application.MyClient;
import com.simplenote.module.account.AccountManager;
import com.simplenote.network.IRequest;
import com.simplenote.network.IRequestCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by melon on 2017/7/20.
 */

public class LoginManager {

    private static final String URL_SEND_SMS = Constant.URL_MAIN + "/user/sms_code";
    private static final String URL_REGISTER_MOBILE = Constant.URL_MAIN + "/user/register/mobile";
    private static final String URL_LOGIN_MOBILE = Constant.URL_MAIN + "/user/login/mobile";

    private Context context;

    private boolean isLogin = false;

    private List<OnLoginStateChangeListener> onLoginStateChangeListenerList = new ArrayList<>();
    private List<OnRegisterAccountListener> onRegisterAccountListenerList = new ArrayList<>();


    public LoginManager(Context context){
        this.context = context;
        isLogin = GlobalPreferenceManager.isLogin(context);

    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        if (isLogin == login){
            return;
        }
        isLogin = login;
        dispatchOnLoginStateChangeListener(login);
    }

    public void registerOnLoginStateChangeListener(OnLoginStateChangeListener listener){
        onLoginStateChangeListenerList.add(listener);
    }

    public void unregisterOnLoginStateChangeListener(OnLoginStateChangeListener listener){
        onLoginStateChangeListenerList.remove(listener);
    }

    private void dispatchOnLoginStateChangeListener(boolean isLogin){
        for (OnLoginStateChangeListener listener : onLoginStateChangeListenerList){
            listener.onLoginStatChange(isLogin);
        }
    }

    public void registerOnRegisterListener(OnRegisterAccountListener listener){
        onRegisterAccountListenerList.add(listener);
    }

    public void unregisterOnRegisterListener(OnRegisterAccountListener listener){
        onRegisterAccountListenerList.remove(listener);
    }

    private void dispatchOnRegisterListener(boolean isSuccess,String msg){
        for (OnRegisterAccountListener listener : onRegisterAccountListenerList){
            listener.onRegisterFinish(isSuccess,msg);
        }
    }

    /**
     *
     * @param phone
     */
    public void sendSms(String phone,final OnSendSmsListener listener){

        HashMap<String,String> map = new HashMap<>();

        map.put(Constant.PARAM.PHONE,phone);
        map.put(Constant.PARAM.REQ_TYPE,Constant.VALUE.AUTH_CODE_REQ_TYPE_REGISTER);

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(URL_SEND_SMS, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.sendSmsFinish(false,"发送验证码失败，请稍后重试。");
                    return;
                }


                int code = jsonObject.getIntValue("code");

                if (code == 0){
                    listener.sendSmsFinish(true,"");
                }else{
                    listener.sendSmsFinish(false,jsonObject.getString("msg"));
                }

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.sendSmsFinish(false,"发送验证码失败，请稍后重试。");
                }
            }
        });
    }

    public void registerAccount(String phone,String passWord,String authCode,String nickName){

        HashMap<String,String> map = new HashMap<>();

        map.put(Constant.PARAM.PHONE,phone);
        map.put(Constant.PARAM.PWD,passWord);
        map.put(Constant.PARAM.AUTH_CODE,authCode);
        map.put(Constant.PARAM.NICK_NAME,nickName);

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(URL_REGISTER_MOBILE, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {

                if (jsonObject == null) {
                    dispatchOnRegisterListener(false, context.getString(R.string.signup_fail));
                    return;
                }

                RegisterResultModel model = new RegisterResultModel();
                model.decode(jsonObject);


                if (model.getCode() == 0){
                    AccountManager accountManager = MyClient.getMyClient().getAccountManager();
                    accountManager.setUserId(model.getUserId(),false);
                    accountManager.setNickName(model.getNickName(),false);
                    accountManager.setToken(model.getToken(),false);
                    accountManager.setMaxImageCount(model.getMaxImageCount(),true);
                    accountManager.setAccount(model.getAccount(),true);
                    setLogin(true);
                    MyClient.getMyClient().getStorageManager().initAllDir();
                    MyClient.getMyClient().getNoteV1Manager().getNoteFromDatabase();
                    dispatchOnRegisterListener(true, context.getString(R.string.signup_success));
                }else{
                    dispatchOnRegisterListener(false, model.getMsg());
                }

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                dispatchOnRegisterListener(false, context.getString(R.string.signup_fail));
            }
        });
    }

    public void loginAccount(String phone,String passWord){

        HashMap<String,String> map = new HashMap<>();

        map.put(Constant.PARAM.PHONE,phone);
        map.put(Constant.PARAM.PWD,passWord);
        map.put(Constant.PARAM.LOGIN_TYPE,Constant.VALUE.LOGIN_TYPE_PWD);


        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(URL_LOGIN_MOBILE, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {

                if (jsonObject == null) {
                    dispatchOnRegisterListener(false, context.getString(R.string.login_fail));
                    return;
                }

                RegisterResultModel model = new RegisterResultModel();
                model.decode(jsonObject);


                if (model.getCode() == 0){
                    AccountManager accountManager = MyClient.getMyClient().getAccountManager();
                    accountManager.setUserId(model.getUserId(),false);
                    accountManager.setNickName(model.getNickName(),false);
                    accountManager.setToken(model.getToken(),false);
                    accountManager.setMaxImageCount(model.getMaxImageCount(),false);
                    accountManager.setAvatarPath(model.getAvatarPath(),false);
                    accountManager.setBackdropPath(model.getBackdropPath(),true);
                    accountManager.setAccount(model.getAccount(),true);
                    MyClient.getMyClient().getStorageManager().initAllDir();
                    MyClient.getMyClient().getNoteV1Manager().getNoteFromDatabase();
                    setLogin(true);
                    dispatchOnRegisterListener(true, "");
                }else{
                    dispatchOnRegisterListener(false, model.getMsg());
                }

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                dispatchOnRegisterListener(false, context.getString(R.string.login_fail));
            }
        });
    }

}
