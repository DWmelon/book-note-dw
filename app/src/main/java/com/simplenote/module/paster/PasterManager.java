package com.simplenote.module.paster;

import com.alibaba.fastjson.JSONObject;
import com.simplenote.application.MyClient;
import com.simplenote.constants.Constant;
import com.simplenote.network.IRequest;
import com.simplenote.network.IRequestCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by melon on 2017/11/1.
 */

public class PasterManager {

    private final String URL_GET_PASTER_LIST = Constant.URL_MAIN + "/paster/get";
    private final String URL_FAVOR_PASTER = Constant.URL_MAIN + "/paster/favor";

    private List<PasterModel> pasterModelList = new ArrayList<>();

    public void getPasterList(int start,final OnGetPasterListListener listener){

        HashMap<String,String> map = new HashMap<>();

        map.put(Constant.PARAM.START,String.valueOf(start));
        map.put(Constant.PARAM.LENGTH,String.valueOf(20));
        map.put(Constant.PARAM.USER_ID,String.valueOf(MyClient.getMyClient().getAccountManager().getUserId()));

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(URL_GET_PASTER_LIST, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.onGetPasterFinish(false);
                    return;
                }


                PasterListResult result = new PasterListResult();
                result.decode(jsonObject);

                if (result.getCode() == 0){
                    pasterModelList = result.getModelList();
                    listener.onGetPasterFinish(true);
                }else{
                    listener.onGetPasterFinish(false);
                }



            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onGetPasterFinish(false);
                }
            }
        });
    }

    public void favorPaster(Long pasterId,final OnFavorPasterListener listener){

        HashMap<String,String> map = new HashMap<>();

        map.put(Constant.PARAM.USER_ID,String.valueOf(MyClient.getMyClient().getAccountManager().getUserId()));
        map.put(Constant.PARAM.PASTER_ID,String.valueOf(pasterId));

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(URL_FAVOR_PASTER, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.onFavorFinish(false);
                    return;
                }

                if (jsonObject.getIntValue(Constant.KEY.CODE) == 0){
                    listener.onFavorFinish(true);
                }else{
                    listener.onFavorFinish(true);
                }

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onFavorFinish(false);
                }
            }
        });
    }

    public List<PasterModel> getPasterModelList() {
        return pasterModelList;
    }

}
