package com.simplenote.module.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.simplenote.R;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends Activity {
    @BindView(R.id.bt_share_mini)
    public Button mBtShare;

    @BindView(R.id.tv_return_info)
    public TextView mTvInfo;

    @BindView(R.id.et_appid)
    public EditText mEtAPPId;

    @BindView(R.id.et_path)
    public EditText mEtPath;

    @BindView(R.id.rbt_pre)
    public AppCompatRadioButton mRbtPre;

    @BindView(R.id.rbt_test)
    public AppCompatRadioButton mRbtTest;

    @BindView(R.id.rbt_official)
    public AppCompatRadioButton mRbtOffi;

    @BindView(R.id.id_rg_version)
    public RadioGroup mRgVersion;

    String appId = "wx377fd1eae1b4fe58"; // 填应用AppId
    IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initView();
        initEvent();
        initData();
    }

    private void initView(){
        mBtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                req.userName = mEtAPPId.getText().toString(); // 填小程序原始id
                req.path = mEtPath.getText().toString();                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
                if (mRbtPre.isChecked()){
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
                }else if (mRbtTest.isChecked()){
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_TEST;// 可选打开 开发版，体验版和正式版
                }else{
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                }
                api.sendReq(req);
            }
        });
        mRgVersion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });
//        mRbtPre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                mRbtTest.setChecked(!b);
//                mRbtOffi.setChecked(!b);
//            }
//        });
//        mRbtTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                mRbtTest.setChecked(!b);
//                mRbtPre.setChecked(!b);
//            }
//        });
//        mRbtOffi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                mRbtTest.setChecked(!b);
//                mRbtPre.setChecked(!b);
//            }
//        });
    }

    private void initEvent(){
        api = WXAPIFactory.createWXAPI(this, appId);
    }

    private void initData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        String info = bundle.getString("info");
        mTvInfo.setText("小程序返回信息："+ info);
    }

}
