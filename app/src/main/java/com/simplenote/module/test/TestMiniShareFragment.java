package com.simplenote.module.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.simplenote.PageFragment;
import com.simplenote.R;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestMiniShareFragment extends PageFragment {

    @BindView(R.id.bt_share_mini)
    public Button mBtShare;

    private View mVContentView;

    String appId = "wx377fd1eae1b4fe58"; // 填应用AppId
    IWXAPI api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_share,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVContentView = view;
        initView();
        initEvent();
    }

    private void initView(){
        mVContentView.findViewById(R.id.iv_bar_left_icon).setVisibility(View.GONE);
        mBtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                req.userName = "gh_44fe7ba7bb8b"; // 填小程序原始id
//                req.path = "";                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
//                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
//                api.sendReq(req);
                Intent intent = new Intent(getActivity(), TestActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void initEvent(){
        api = WXAPIFactory.createWXAPI(getActivity(), appId);
    }

}
