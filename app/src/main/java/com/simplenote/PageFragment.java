package com.simplenote;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by eddy on 2015/4/27.
 */
public class PageFragment extends Fragment {

    protected Handler mUIHandler = new Handler();
    protected Switchable mSwitchable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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
