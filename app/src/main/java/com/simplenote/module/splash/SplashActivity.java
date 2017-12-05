package com.simplenote.module.splash;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;


import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.application.MyClient;
import com.simplenote.module.MyMainActivity;
import com.simplenote.module.listener.OnDataLoadFinishListener;
import com.simplenote.module.permission.OnRequestPermissionFinish;
import com.simplenote.module.permission.PermissionManager;
import com.simplenote.module.share.ThemeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/1/3.
 */

public class SplashActivity extends BaseActivity implements OnRequestPermissionFinish {

    private List<String> denyPermissions = new ArrayList<>();

    private boolean isHandlePermissionFinish = false;
    private boolean isCountDownFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        checkMyPermissions();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                isCountDownFinish = true;
                if (isHandlePermissionFinish){
                    performIntent();
                }
            }
        },2000);

    }

    private void showSettingDialog(){
        showCommonAlert(R.string.dialog_title,R.string.dialog_permission_lack,R.string.dialog_setting,R.string.dialog_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:"+ getPackageName()));
                startActivityForResult(intent,2);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMaterialDialog.dismiss();
                handlePermissionFinish();
            }
        });
    }

    private void handlePermissionFinish(){
        isHandlePermissionFinish = true;
        if (isCountDownFinish){
            performIntent();
        }
    }
    
    private void performIntent(){

        //初始化用户日记内容
        MyClient.getMyClient().getNoteV1Manager().init();

        Intent intent = new Intent(SplashActivity.this,MyMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            handlePermissionFinish();
        }
    }

    private void checkMyPermissions(){
        MyClient.getMyClient().getPermissionManager().handlePermission(this,PermissionManager.PERMISSION_STORAGE,this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyClient.getMyClient().getPermissionManager().onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionFinish(boolean result) {
        if (result){
            handlePermissionFinish();
        }else{
            showSettingDialog();
        }
    }

}
