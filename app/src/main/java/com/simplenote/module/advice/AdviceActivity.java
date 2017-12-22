package com.simplenote.module.advice;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.application.MyClient;
import com.simplenote.util.EmailUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/2/26.
 */

public class AdviceActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.et_advice_1)
    EditText mEtContent;

    @BindView(R.id.et_advice_2)
    EditText mEtContact;

    @BindView(R.id.tv_advice_btn)
    TextView mTvSubmit;

    @BindView(R.id.tv_tool_bar_title)
    TextView mTvTitle;

    private EmailUtil m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initData(){
        mTvTitle.setText(R.string.advice_title);
    }

    private void initListener(){
        mTvSubmit.setOnClickListener(this);
    }

    @OnClick(R.id.iv_bar_left_icon)
    void back(){
        finish();
    }


    @Override
    public void onClick(View view) {
        checkAndSend();
    }

    private void checkAndSend(){
        if(mEtContent.getText().toString().trim().isEmpty()){
            Toast.makeText(this,getString(R.string.advice_toast),Toast.LENGTH_LONG).show();
        }else{
            sendEmail();
        }
    }

    private void sendEmail(){

        m = new EmailUtil("3303847677@qq.com", "uudpnhbbmiqgcjhj");

        String[] toArr = {"3303847677@qq.com"};
        m.setTo(toArr);
        m.setFrom("3303847677@qq.com");

        String contact = mEtContact.getText().toString().trim();
        if (TextUtils.isEmpty(contact)){
            if (MyClient.getMyClient().getLoginManager().isLogin()){
                contact = String.valueOf(MyClient.getMyClient().getAccountManager().getUserId());
            }
        }
        m.setSubject(getString(R.string.advice_subject,contact));
        m.setBody(mEtContent.getText().toString().trim());

        try {
            if(m.send()) {
                Toast.makeText(AdviceActivity.this, "发送成功啦~", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(AdviceActivity.this, "发送失败>.<", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
