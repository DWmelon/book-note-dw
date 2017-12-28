package com.simplenote.module.setting;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.application.MyClient;
import com.simplenote.module.home.OnSetupImageFinishListener;
import com.simplenote.util.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/9/26.
 */

public class SettingActivity extends BaseActivity implements OnSetupImageFinishListener{

    @BindView(R.id.sdv_user_logo)
    SimpleDraweeView mSdvLogo;

    @BindView(R.id.tv_user_nickname)
    TextView mTvNickName;

    @BindView(R.id.tv_user_account)
    TextView mTvAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initData();

        if (!MyClient.getMyClient().getLoginManager().isLogin()){
            findViewById(R.id.ll_setting_user).setVisibility(View.GONE);
        }
    }

    private void initData(){
        ((TextView)findViewById(R.id.tv_tool_bar_title)).setText("设");
        ImageUtils.setupAvatarAndBackDrop(mSdvLogo, Constant.VALUE.AVATAR_PAGE_TYPE_LOGO,this);
        mTvNickName.setText(MyClient.getMyClient().getAccountManager().getNickName());
        mTvAccount.setText(getString(R.string.setting_account,MyClient.getMyClient().getAccountManager().getAccount()));
    }

    @OnClick(R.id.iv_bar_left_icon)
    void back(){
        finish();
    }

    @OnClick(R.id.tv_logout)
    void logout(){
        //1.清空用户数据
        MyClient.getMyClient().getAccountManager().logout();
        //2.重置相关路径
        MyClient.getMyClient().getStorageManager().initAllDir();
        //3.重新读取日记信息
        MyClient.getMyClient().getNoteV1Manager().getNoteFromDatabase();
        //4.改变登录状态
        MyClient.getMyClient().getLoginManager().setLogin(false);
        finish();
    }

    @Override
    public void onLoadImageFinish(final String imageType, final Uri uri) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSdvLogo.setImageURI(uri);
            }
        });
    }
}
