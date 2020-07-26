package com.simplenote;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.simplenote.util.CommonUtil;

import butterknife.BindView;


/**
 * Created by eddy on 2015/4/27.
 */
public class PageFragment extends Fragment {

    protected Handler mUIHandler = new Handler();
    protected Switchable mSwitchable;

    @BindView(R.id.v_tool_bar_top)
    View mVTBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mVTBar.getLayoutParams();
        params.height = CommonUtil.getStatusBarHeight(getActivity());
        mVTBar.setLayoutParams(params);
    }

    public void setOnTabSwitchListener(Switchable listener) {
        this.mSwitchable = listener;
    }

    public Switchable getOnTabSwitchListener() {
        return this.mSwitchable;
    }

    public interface Switchable {
        public void onTabSwitch(String tag, Bundle bundle);
    }
}
