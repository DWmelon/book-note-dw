package com.simplenote.module.paster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.simplenote.PageFragment;
import com.simplenote.R;
import com.simplenote.application.MyClient;
import com.simplenote.module.MyMainActivity;
import com.simplenote.module.login.OnLoginStateChangeListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/11/1.
 */

public class PasterFragment extends PageFragment implements OnGetPasterListListener,OnFavorPasterListener,OnLoginStateChangeListener {

    @BindView(R.id.rv_paster)
    RecyclerView mRvPaster;
    PasterAdapter mAdapter;

    @BindView(R.id.tv_tool_bar_title)
    TextView mTvTitle;

    private View mContentView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentView = view;
        initData();
        MyClient.getMyClient().getLoginManager().registerOnLoginStateChangeListener(this);
        MyClient.getMyClient().getPasterManager().getPasterList(0,this);
    }

    private void initData(){
        mTvTitle.setText(getString(R.string.paster_title));
        mContentView.findViewById(R.id.iv_bar_left_icon).setVisibility(View.GONE);
        mAdapter = new PasterAdapter(getActivity());
        mAdapter.setFavorListener(this);
        mRvPaster.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        MyItemDecorator myItemDecorator = new MyItemDecorator(26,13,30,15);
        mRvPaster.addItemDecoration(myItemDecorator);

        mRvPaster.setAdapter(mAdapter);

    }

    @OnClick(R.id.iv_bar_left_icon)
    void openMenu(){
        ((MyMainActivity) getActivity()).openDrawerLayout();
    }


    @Override
    public void onGetPasterFinish(boolean isSuccess) {
        if (isSuccess){
            mAdapter.notifyDataSetChanged();
            return;
        }

        if (!MyClient.getMyClient().getPasterManager().getPasterModelList().isEmpty()){
            Toast.makeText(getActivity(),"获取贴纸失败，请稍后重试。",Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: 2017/11/1 空白页面

    }

    @Override
    public void onFavorFinish(boolean isSuccess, PasterAdapter.ViewHolder holder) {
        if (getActivity() == null || !isSuccess){
            return;
        }
        if (!holder.mIvFavorIcon.isSelected()){
            holder.mIvFavorIcon.setSelected(true);
            int count = Integer.parseInt(holder.mTvFavorCount.getText().toString());
            holder.mTvFavorCount.setText(String.valueOf(++count));
        }
    }

    @Override
    public void onLoginStatChange(boolean isLogin) {
        if (getActivity() == null){
            return;
        }
        MyClient.getMyClient().getPasterManager().getPasterList(this);
    }
}
