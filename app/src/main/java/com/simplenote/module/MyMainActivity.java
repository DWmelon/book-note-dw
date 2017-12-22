package com.simplenote.module;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.MyClient;
import com.simplenote.module.account.AccountManager;
import com.simplenote.module.advice.AdviceActivity;
import com.simplenote.module.home.OnSetupImageFinishListener;
import com.simplenote.module.home.OnSyncTimeListener;
import com.simplenote.module.left.SelectAvatarActivity;
import com.simplenote.module.login.LoginActivity;
import com.simplenote.module.login.OnLoginStateChangeListener;
import com.simplenote.module.login.SignActivity;
import com.simplenote.module.oos.OSSActivity;
import com.simplenote.module.setting.SettingActivity;
import com.simplenote.util.ImageUtils;
import com.simplenote.util.TimeUtils;
import com.simplenote.widgets.CircleImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by melon on 2017/1/3.
 */

public class MyMainActivity extends MainBaseActivity implements OnLoginStateChangeListener,View.OnClickListener,OnSetupImageFinishListener,OnSyncTimeListener{


    private LinearLayout mLlLoginLayout;
    private CircleImageView mCivUserLogo;
    private TextView mTvUserName;
    private TextView mTvDisplayTime;

    private LinearLayout mLlUnLoginLayout;
    private TextView mTvGoLogin;
    private TextView mTvGoSign;

    @BindView(R.id.ll_left_sync)
    LinearLayout mLlSync;

    @BindView(R.id.ll_left_setting)
    LinearLayout mLlSetting;

    @BindView(R.id.sdv_backdrop)
    SimpleDraweeView mSdvBackdrop;

    @BindView(R.id.tv_sync_time)
    TextView mTvSyncTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initListener();
        handleLogin(MyClient.getMyClient().getLoginManager().isLogin());

    }

    private void initView(){

        mLlLoginLayout = (LinearLayout)findViewById(R.id.ll_layout_login);
        mCivUserLogo = (CircleImageView)findViewById(R.id.civ_user_logo);
        mTvUserName = (TextView)findViewById(R.id.tv_user_name);
        mTvDisplayTime = (TextView)findViewById(R.id.tv_user_time);

        mLlUnLoginLayout = (LinearLayout)findViewById(R.id.ll_layout_unlogin);
        mTvGoLogin = (TextView)findViewById(R.id.tv_go_login);
        mTvGoSign = (TextView)findViewById(R.id.tv_go_sign);


    }

    private void initData(){

        ImageUtils.setupAvatarAndBackDrop(mCivUserLogo, Constant.VALUE.AVATAR_PAGE_TYPE_LOGO,this);
        ImageUtils.setupAvatarAndBackDrop(mSdvBackdrop,Constant.VALUE.AVATAR_PAGE_TYPE_BACKDROP,this);

        mTvDisplayTime.setText(TimeUtils.getNowTime());
    }

    private void initListener(){
        MyClient.getMyClient().getLoginManager().registerOnLoginStateChangeListener(this);
        mTvGoLogin.setOnClickListener(this);
        mTvGoSign.setOnClickListener(this);

        mCivUserLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BUNDEL.AVATAR_TYPE,Constant.VALUE.AVATAR_PAGE_TYPE_LOGO);

                Intent intent = new Intent(MyMainActivity.this,SelectAvatarActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,Constant.REQUEST.INTENT_SELECT_AVATAR);
                return true;
            }
        });

        mSdvBackdrop.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BUNDEL.AVATAR_TYPE,Constant.VALUE.AVATAR_PAGE_TYPE_BACKDROP);

                Intent intent = new Intent(MyMainActivity.this,SelectAvatarActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,Constant.REQUEST.INTENT_SELECT_AVATAR);
                return true;
            }
        });

    }


    private void updateUserInfo(){
        AccountManager accountManager = MyClient.getMyClient().getAccountManager();
        mTvUserName.setText(accountManager.getNickName());
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.tv_go_login:{
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tv_go_sign:{
                intent = new Intent(this, SignActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onLoginStatChange(boolean isLogin) {
        handleLogin(isLogin);
    }

    private void handleLogin(boolean isLogin){
        if (isLogin){
            mLlLoginLayout.setVisibility(View.VISIBLE);
            mLlUnLoginLayout.setVisibility(View.GONE);
            updateUserInfo();
            MyClient.getMyClient().getOSSManager().handleSyncTime(Constant.VALUE.TYPE_SYNC_TIME_GET,this);
        }else {
            mLlLoginLayout.setVisibility(View.GONE);
            mLlUnLoginLayout.setVisibility(View.VISIBLE);
        }
        initData();
    }

    public void openDrawerLayout(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyClient.getMyClient().getLoginManager().unregisterOnLoginStateChangeListener(this);
    }

    @OnClick(R.id.ll_left_sync)
    void syncFunc(){
        if (!MyClient.getMyClient().getLoginManager().isLogin()){
            showNeedLoginAlert(R.string.dialog_use_tip,null);
            return;
        }
        Intent intent = new Intent(this, OSSActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_left_setting)
    void handleSetting(){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_left_advice)
    void handleAdvice(){
        Intent intent = new Intent(this, AdviceActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST.INTENT_SELECT_AVATAR){
            if (resultCode == RESULT_OK){
                String pageType = data.getStringExtra(Constant.BUNDEL.AVATAR_TYPE);
                String pageRes = data.getStringExtra(Constant.BUNDEL.PIC_RES);
                onLoadImageFinish(pageType,Uri.fromFile(new File(pageRes)));
//                if (pageType.equals(Constant.VALUE.AVATAR_PAGE_TYPE_LOGO)){
//                    ImageUtils.setupAvatarAndBackDrop(mCivUserLogo, Constant.VALUE.AVATAR_PAGE_TYPE_LOGO,this);
//                }else{
//                    ImageUtils.setupAvatarAndBackDrop(mSdvBackdrop, Constant.VALUE.AVATAR_PAGE_TYPE_BACKDROP,this);
//                }
            }
        }
    }

    @Override
    public void onLoadImageFinish(final String imageType, final Uri uri) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (imageType.equals(Constant.VALUE.AVATAR_PAGE_TYPE_LOGO)){
                    mCivUserLogo.setImageURI(uri);
                }else{
                    mSdvBackdrop.setImageURI(uri);
                }
            }
        });
    }

    @Override
    public void onSyncTimeFinish(boolean isSuccess, long time) {
        if (!isSuccess){
            findViewById(R.id.ll_sync_time).setVisibility(View.GONE);
            return;
        }
        findViewById(R.id.ll_sync_time).setVisibility(View.VISIBLE);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        String str = format.format(date);
        mTvSyncTime.setText(getString(R.string.sync_time,str));
    }
}
