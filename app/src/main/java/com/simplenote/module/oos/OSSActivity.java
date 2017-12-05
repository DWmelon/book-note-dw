package com.simplenote.module.oos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.module.oos.download.OSSDownloadActivity;
import com.simplenote.module.oos.upload.OSSUploadActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/8/5.
 */

public class OSSActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oss);
        ButterKnife.bind(this);

        initView();

    }

    private void initView(){
        TextView mTvTitle = ButterKnife.findById(this,R.id.tv_tool_bar_title);
        mTvTitle.setText("云");
    }

    @OnClick(R.id.tv_sync_choose_upload)
    void handleIntentUpload(){
        Intent intent = new Intent(this,OSSUploadActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_sync_choose_download)
    void handleIntentDownload(){
        Intent intent = new Intent(this,OSSDownloadActivity.class);
        startActivity(intent);
    }


}
