package com.simplenote.module.login;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.application.MyClient;
import com.simplenote.util.ParamUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/7/20.
 */

public class SignActivity extends BaseActivity implements OnSendSmsListener,OnRegisterAccountListener {

    @BindView(R.id.et_signup_phone)
    EditText mEtPhone;

    @BindView(R.id.et_signup_psw)
    EditText mEtPassWord;

    @BindView(R.id.et_signup_verification_code)
    EditText mEtAuthCode;

    @BindView(R.id.et_signup_nick_name)
    EditText mEtNickName;

    @BindView(R.id.tv_signup_get_verification_code)
    TextView mTvGetSmsCode;

    @BindView(R.id.btn_signup)
    TextView mTvSignUp;

    public static final int VALUE_PHONE_LENGTH = 11;

    private final Handler handler = new Handler();

    private int TIME_LENGTH;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initData(){
        ((ImageView)findViewById(R.id.iv_bar_left_icon)).setImageResource(R.drawable.icon_back);
        findViewById(R.id.iv_bar_left_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.tv_tool_bar_title)).setText(R.string.signup);
    }

    private void initListener(){
        MyClient.getMyClient().getLoginManager().registerOnRegisterListener(this);

        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkSendSmsEnable();
            }
        });
    }

    @OnClick(R.id.tv_signup_get_verification_code)
    void sendSmS(){

        if (!ParamUtils.checkInputPhone(this,mEtPhone.getText().toString())) {
            return;
        }

        showProgress();
        mTvGetSmsCode.setClickable(false);
        mTvGetSmsCode.setSelected(false);
        flag = true;
        MyClient.getMyClient().getLoginManager().sendSms(mEtPhone.getText().toString(),this);
    }

    private void checkSendSmsEnable() {

        if (!isGettingVC()) {
            if (mEtPhone.getText().toString().trim().length() == VALUE_PHONE_LENGTH) {
                mTvGetSmsCode.setSelected(true);
                mTvGetSmsCode.setClickable(true);
                mTvGetSmsCode.setTextColor(getResources().getColor(R.color.primary_white));
            } else {
                mTvGetSmsCode.setSelected(false);
                mTvGetSmsCode.setClickable(false);
                mTvGetSmsCode.setTextColor(getResources().getColor(R.color.login_get_vc_default_color));
            }
        }
    }

    private boolean isGettingVC() {
        return flag;
    }

    private void startTimer() {
        mTvGetSmsCode.setSelected(false);
        mTvGetSmsCode.setClickable(false);
        mTvGetSmsCode.setTextColor(getResources().getColor(R.color.login_get_vc_default_color));
        handler.post(runnable);
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mTvGetSmsCode.setText(getString(R.string.signup_minute_count, TIME_LENGTH));
            TIME_LENGTH--;
            if (TIME_LENGTH > 0) {
                handler.postDelayed(this, 1000);
            } else {
                TIME_LENGTH = 120;
                mTvGetSmsCode.setSelected(true);
                mTvGetSmsCode.setClickable(true);
                mTvGetSmsCode.setTextColor(getResources().getColor(R.color.primary_white));
                mTvGetSmsCode.setText(getString(R.string.signup_get_captcha));
                flag = false;
                checkSendSmsEnable();
            }
        }
    };

    @Override
    public void sendSmsFinish(boolean isSuccess, String msg) {
        hideProgress();
        if (!isSuccess){
            flag = false;
            mTvGetSmsCode.setClickable(true);
            mTvGetSmsCode.setSelected(true);
            Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this,R.string.signup_send_code_success,Toast.LENGTH_LONG).show();
        startTimer();

    }


    @OnClick(R.id.btn_signup)
    void requestSignIn() {
        String phone = mEtPhone.getText().toString().trim();
        String password = mEtPassWord.getText().toString().trim();
        String captcha = mEtAuthCode.getText().toString().trim();
        String nickName = mEtNickName.getText().toString().trim();

        if (!ParamUtils.checkInputPhone(this,mEtPhone.getText().toString())) return;
        if (!checkPsw()) return;
        if (!ParamUtils.checkIsValidCaptcha(captcha)) {
            Toast.makeText(this, R.string.signup_please_iput_captcha, Toast.LENGTH_LONG).show();
            return;
        }
        if (!checkNickName()){
            Toast.makeText(this, R.string.signup_please_nick_name, Toast.LENGTH_LONG).show();
            return;
        }


        showProgress(R.string.signuping,false);


        MyClient.getMyClient().getLoginManager().registerAccount(phone, password, captcha,nickName);

    }

    private boolean checkNickName(){
        return !TextUtils.isEmpty(mEtNickName.getText().toString());
    }

    private boolean checkPsw() {

        String psw = mEtPassWord.getText().toString();

        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(this, R.string.signup_please_iput_psw, Toast.LENGTH_LONG).show();
            return false;
        } else if (psw.length() < 6 || psw.length() > 18) {
            Toast.makeText(this, R.string.signup_psw_hint, Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    @Override
    public void onRegisterFinish(boolean isSuccess, String msg) {
        hideProgress();
        if (!isSuccess){
            Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this,R.string.signup_success,Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyClient.getMyClient().getLoginManager().unregisterOnRegisterListener(this);
    }


}
