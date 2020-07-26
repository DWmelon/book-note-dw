package com.simplenote.module.login;

import android.os.Bundle;

import com.simplenote.R;
import com.simplenote.application.BaseActivity;

import butterknife.ButterKnife;

public class RetrievePsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_ps);
        ButterKnife.bind(this);
    }
}
