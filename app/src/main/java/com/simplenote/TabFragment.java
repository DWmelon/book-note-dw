package com.simplenote;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simplenote.module.paster.PasterFragment;
import com.simplenote.module.home.MyMainFragment;
import com.simplenote.widgets.RedPointImageView;

import java.util.HashSet;

/**
 * Created by melon on 2017/6/23.
 */

public class TabFragment extends Fragment implements View.OnClickListener,PageFragment.Switchable{

    public static final String TAB_HOME = MyMainFragment.class.getName();

    public static final String TAB_INFORMATION = PasterFragment.class.getName();

    private Context mContext;
    private View mContentView;
    private View mLastSelectView;

    private RedPointImageView mIvHome;
    private RedPointImageView mIvInfo;

    private TextView mTvWrite;
    private TextView mTvMiao;

    private PageFragment mLastShowFragment;

    private HashSet<String> mFragmentTags = new HashSet<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContainFragmentTags();
        checkAndHideFragment();
    }

    private void initContainFragmentTags() {
        mFragmentTags.add(TAB_HOME);
        mFragmentTags.add(TAB_INFORMATION);
    }

    private void checkAndHideFragment() {
        FragmentManager fm = getFragmentManager();
        if (fm == null) {
            return;
        }

        FragmentTransaction ft = fm.beginTransaction();
        PageFragment home = (PageFragment) fm.findFragmentByTag(TAB_HOME);
        if (home != null) {
            ft.hide(home);
            home.setOnTabSwitchListener(this);
        }

        PageFragment information = (PageFragment) fm.findFragmentByTag(TAB_INFORMATION);
        if (information != null) {
            ft.hide(information);
            information.setOnTabSwitchListener(this);
        }

        ft.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_tab, container, false);
        init();
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void init() {
        View home = mContentView.findViewById(R.id.ll_tab_item_home);
        home.setOnClickListener(this);


        View info = mContentView.findViewById(R.id.ll_tab_item_me);
        info.setOnClickListener(this);

        mIvHome = (RedPointImageView) home.findViewById(R.id.iv_tab_item_home);
        mIvInfo = (RedPointImageView) info.findViewById(R.id.iv_tab_item_me);

        mTvWrite = (TextView) home.findViewById(R.id.tv_tab_write);
        mTvMiao = (TextView) info.findViewById(R.id.tv_tab_miao);

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        switchTo(TAB_HOME, null);
        mLastSelectView = mContentView.findViewById(R.id.ll_tab_item_home);
        mLastSelectView.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        handleOnClick(v, null);
    }

    private void handleOnClick(View v, Bundle bundle) {
        if (v == mLastSelectView) {
            return;
        }
        mLastSelectView.setSelected(false);
        mLastSelectView.setPressed(false);
        switch (v.getId()) {
            case R.id.ll_tab_item_home:
                switchTo(TAB_HOME, bundle);
                mIvHome.setSelected(true);
                mIvInfo.setSelected(false);
                mTvWrite.setTextColor(getResources().getColor(R.color.primary_color));
                mTvMiao.setTextColor(getResources().getColor(R.color.tab_bar_txt));
                break;

            case R.id.ll_tab_item_me:
                switchTo(TAB_INFORMATION, bundle);
                mIvInfo.setSelected(true);
                mIvHome.setSelected(false);
                mTvWrite.setTextColor(getResources().getColor(R.color.tab_bar_txt));
                mTvMiao.setTextColor(getResources().getColor(R.color.primary_color));
                break;

        }
        v.setSelected(true);
        mLastSelectView = v;
    }

    private void handleTabSwitchCallback(String name, Bundle bundle) {
        if (TAB_HOME.equals(name)) {
            handleOnClick(mContentView.findViewById(R.id.ll_tab_item_home), bundle);
        } else if (TAB_INFORMATION.equals(name)){
            handleOnClick(mContentView.findViewById(R.id.ll_tab_item_me), bundle);
        }
    }

    private void switchTo(String name, Bundle args) {
        if (TextUtils.isEmpty(name)) {
            return;
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (mLastShowFragment != null) {
            ft.hide(mLastShowFragment);
        }

        PageFragment fragment = (PageFragment) fm.findFragmentByTag(name);//mFragments.get(name);
        if (fragment == null) {
            fragment = (PageFragment) Fragment.instantiate(mContext, name);
            fragment.setArguments(args);
            ft.add(R.id.fl_container, fragment, name);
            fragment.setOnTabSwitchListener(this);
        } else {
            ft.show(fragment);
        }



        mLastShowFragment = fragment;
        ft.commitAllowingStateLoss();

    }

    @Override
    public void onTabSwitch(String tag, Bundle bundle) {
        if (mFragmentTags.contains(tag)) {
            handleTabSwitchCallback(tag, bundle);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
