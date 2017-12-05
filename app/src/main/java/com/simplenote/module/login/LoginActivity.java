package com.simplenote.module.login;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
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

public class LoginActivity extends BaseActivity implements OnRegisterAccountListener {

    @BindView(R.id.et_login_phone)
    EditText mEtPhone;

    @BindView(R.id.et_login_psw)
    EditText mEtPsw;

    @BindView(R.id.btn_login)
    TextView mTvDone;

    @BindView(R.id.tv_login_forget_psw)
    TextView mTvForgetPsw;

    @BindView(R.id.iv_login_psw_switch)
    ImageView mIvPswSwitch;

    private boolean isPswVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        ((TextView)findViewById(R.id.tv_tool_bar_title)).setText(R.string.login);
    }

    private void initListener(){
        MyClient.getMyClient().getLoginManager().registerOnRegisterListener(this);
    }

    private boolean checkPwd(){
        if (TextUtils.isEmpty(mEtPsw.getText().toString())){
            Toast.makeText(this,R.string.signup_please_iput_psw,Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.tv_login_forget_psw)
    void handleForget(){

    }

    @OnClick(R.id.btn_login)
    void handleLogin(){
        String phone = mEtPhone.getText().toString().trim();
        String password = mEtPsw.getText().toString().trim();

        if (!ParamUtils.checkInputPhone(this,phone)) return;
        if (!checkPwd()) return;

        showProgress();
        MyClient.getMyClient().getLoginManager().loginAccount(phone,password);
    }

    @OnClick(R.id.iv_login_psw_switch)
    void switchPswVisibility() {
        isPswVisible = !isPswVisible;
        if (isPswVisible) {
            mIvPswSwitch.setSelected(true);
            mEtPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mIvPswSwitch.setSelected(false);
            mEtPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        Selection.setSelection(mEtPsw.getText(), mEtPsw.getText().length());

    }

    @Override
    public void onRegisterFinish(boolean isSuccess, String msg) {
        hideProgress();
        if (!isSuccess){
            Toast.makeText(this,(TextUtils.isEmpty(msg)?getString(R.string.login_fail):msg),Toast.LENGTH_LONG).show();
            return;
        }

//        Toast.makeText(this,R.string.login_success,Toast.LENGTH_LONG).show();
        finish();

    }
}
