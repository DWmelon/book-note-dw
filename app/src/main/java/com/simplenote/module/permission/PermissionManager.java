package com.simplenote.module.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/7/25.
 */

public class PermissionManager {

    private List<String> denyPermissions = new ArrayList<>();

    public PermissionManager(){
    }

    public static final String[] PERMISSION_STORAGE = new String[]{
            //向SDCard读取数据权限
            Manifest.permission.READ_EXTERNAL_STORAGE,
            //向SDCard写入数据权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

            //umeng
            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.SYSTEM_ALERT_WINDOW
//            Manifest.permission.CALL_PHONE,
//            Manifest.permission.READ_LOGS,
//            Manifest.permission.SET_DEBUG_APP,
//            Manifest.permission.GET_ACCOUNTS,
//            Manifest.permission.WRITE_APN_SETTINGS
    };

    public static final String[] PERMISSION_CAMERA = new String[]{
            //获取拍照权限
            Manifest.permission.CAMERA,
    };

    public void handlePermission(Context context,String[] permissions,OnRequestPermissionFinish listener){
        if (!(context instanceof Activity)){
            return;
        }

        for (int i = 0; i < permissions.length; i++){
            int result = ActivityCompat.checkSelfPermission(context,permissions[i]);
            if (result == PackageManager.PERMISSION_DENIED){
                denyPermissions.add(permissions[i]);
            }
        }
        if (denyPermissions.isEmpty()){
            if (listener != null){
                listener.onPermissionFinish(true);
                return;
            }
        }else {
            String[] array = denyPermissions.toArray(new String[denyPermissions.size()]);
            ActivityCompat.requestPermissions((Activity) context, array ,1);
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults,OnRequestPermissionFinish listener){
        if(requestCode == 1){
            if(grantResults.length >0 ){
                for (int i = 0; i < grantResults.length; i++) {
                    if( grantResults[i] == PackageManager.PERMISSION_GRANTED){//拒绝啦
                        denyPermissions.remove(permissions[i]);
                    }
                }
                if (listener == null){
                    return;
                }

                listener.onPermissionFinish(denyPermissions.isEmpty());
            }
        }
        denyPermissions.clear();
    }

}
