package com.simplenote.module.paster;

import com.alibaba.fastjson.JSONObject;
import com.simplenote.application.MyClient;
import com.simplenote.constants.Constant;
import com.simplenote.network.IRequest;
import com.simplenote.network.IRequestCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by melon on 2017/11/1.
 */

public class PasterManager {

    private final String URL_GET_PASTER_LIST = Constant.URL_MAIN + "/paster/get";
    private final String URL_FAVOR_PASTER = Constant.URL_MAIN + "/paster/favor";

    private List<PasterModel> pasterModelList = new ArrayList<>();

    private HashMap<String,String> rMap = new HashMap<>();

    public void getPasterList(final OnGetPasterListListener listener){
        if (rMap.isEmpty()){
            return;
        }
        rMap.put(Constant.PARAM.TOKEN,MyClient.getMyClient().getAccountManager().getToken());
        getPasterList(rMap,listener);
    }

    public void getPasterList(int start,final OnGetPasterListListener listener){
        HashMap<String,String> map = new HashMap<>();

        map.put(Constant.PARAM.START,String.valueOf(start));
        map.put(Constant.PARAM.LENGTH,String.valueOf(20));
        map.put(Constant.PARAM.TOKEN,MyClient.getMyClient().getAccountManager().getToken());
        getPasterList(map,listener);
    }

    public void getPasterList(HashMap<String,String> map,final OnGetPasterListListener listener){
        rMap = map;
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

    public void favorPaster(Long pasterId, final PasterAdapter.ViewHolder holder, final OnFavorPasterListener listener){

        HashMap<String,String> map = new HashMap<>();

        map.put(Constant.PARAM.TOKEN,MyClient.getMyClient().getAccountManager().getToken());
        map.put(Constant.PARAM.PASTER_ID,String.valueOf(pasterId));

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(URL_FAVOR_PASTER, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.onFavorFinish(false,holder);
                    return;
                }

                if (jsonObject.getIntValue(Constant.KEY.CODE) == 0){
                    listener.onFavorFinish(true,holder);
                }else{
                    listener.onFavorFinish(true,holder);
                }

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onFavorFinish(false,holder);
                }
            }
        });
    }

    public List<PasterModel> getPasterModelList() {
        return pasterModelList;
    }

}
