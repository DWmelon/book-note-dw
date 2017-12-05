package com.simplenote.module;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import com.simplenote.R;
import com.simplenote.TabFragment;
import com.simplenote.application.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by melon on 2017/7/19.
 */

public class MainBaseActivity extends BaseActivity {

    protected DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_drawer);
        ButterKnife.bind(this);
        TabFragment mTabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.f_tab_fragment);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            //将侧边栏顶部延伸至status bar
//            drawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
//            drawerLayout.setClipToPadding(false);
//        }

    }
}
