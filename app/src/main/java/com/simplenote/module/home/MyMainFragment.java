package com.simplenote.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.simplenote.constants.Constant;
import com.simplenote.PageFragment;
import com.simplenote.R;
import com.simplenote.application.MyClient;
import com.simplenote.module.MyMainActivity;
import com.simplenote.module.add.AddNoteActivity;
import com.simplenote.module.add.OnAddNoteListener;
import com.simplenote.module.home.adapter.NoteRvListAdapter;
import com.simplenote.module.listener.OnDataLoadFinishListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/1/3.
 */

public class MyMainFragment extends PageFragment implements OnDataLoadFinishListener,OnAddNoteListener {

    private View mContentView;

    private RecyclerView mRvNoteList;
    private NoteRvListAdapter adapter;

    @BindView(R.id.ll_home_more)
    View mLlMore;

    @BindView(R.id.iv_home_more_take_photo)
    View mIvTakePhoto;

    @BindView(R.id.iv_home_more_search)
    View mIvSearch;

    private AnimationSet translateAnimInA;
    private AnimationSet translateAnimInB;
    private AnimationSet translateAnimOutA;
    private AnimationSet translateAnimOutB;
    private boolean isAnimRunning = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_main,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentView = view;
        
        initBar(view);
        initView(view);

    }
    
    private void initBar(View view){

        LinearLayout mLlContent = (LinearLayout) view.findViewById(R.id.ll_bar_right_content);

        int padding = getResources().getDimensionPixelOffset(R.dimen.margin_10);
        
        ImageView mIvAdd = new ImageView(getActivity());
        mIvAdd.setId(R.id.iv_home_add);
        LinearLayout.LayoutParams paramsAdd = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mIvAdd.setPadding(padding,padding,padding,padding);
        mIvAdd.setImageResource(R.drawable.icon_add);
        mLlContent.addView(mIvAdd,paramsAdd);
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNoteActivity.class);
                startActivityForResult(intent, Constant.REQUEST.ADD_NOTE);
            }
        });

        ImageView mIvMore = new ImageView(getActivity());
        mIvMore.setId(R.id.iv_home_more);
        LinearLayout.LayoutParams paramsMore = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mIvMore.setPadding(padding,padding,padding,padding);
        mIvMore.setImageResource(R.drawable.icon_home_menu);
        mLlContent.addView(mIvMore,paramsMore);
        mIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAnimRunning)return;
                if (mLlMore.getVisibility() == View.VISIBLE){
                    hideMenuMore();
                }else {
                    showMenuMore();
                }
            }
        });
    }

    private void initView(View view){
        mRvNoteList = (RecyclerView) view.findViewById(R.id.rv_home_note);

        mRvNoteList.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (MyClient.getMyClient().getNoteV1Manager().isLoadFinish()){
            adapter = new NoteRvListAdapter(getActivity());
            mRvNoteList.setAdapter(adapter);
            handleEmptyView();
        }

        MyClient.getMyClient().getNoteV1Manager().registerDataLoadListener(this);
        MyClient.getMyClient().getAddNoteManager().registerOnAddNoteListener(this);
    }

    private void handleEmptyView(){
        mContentView.findViewById(R.id.rl_home_empty).setVisibility(adapter.getItemCount()==0?View.VISIBLE:View.GONE);
        mContentView.findViewById(R.id.rl_home_view).setVisibility(adapter.getItemCount()==0?View.GONE:View.VISIBLE);
    }

    @OnClick(R.id.iv_bar_left_icon)
    void handleHomeMenu(){
        ((MyMainActivity) getActivity()).openDrawerLayout();
    }

    private void showMenuMore(){
        if (translateAnimInA == null){
            translateAnimInA = (AnimationSet) AnimationUtils.loadAnimation(getActivity(),R.anim.translate_more_in);
            translateAnimInB = (AnimationSet) AnimationUtils.loadAnimation(getActivity(),R.anim.translate_more_in);
            translateAnimInA.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIvSearch.startAnimation(translateAnimInB);
                    mIvTakePhoto.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            translateAnimInB.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIvSearch.setVisibility(View.VISIBLE);
                    isAnimRunning = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        isAnimRunning = true;
        mLlMore.setVisibility(View.VISIBLE);
        mIvTakePhoto.startAnimation(translateAnimInA);
    }

    private void hideMenuMore(){
        if (translateAnimOutA == null){
            translateAnimOutA = (AnimationSet) AnimationUtils.loadAnimation(getActivity(),R.anim.translate_more_out);
            translateAnimOutB = (AnimationSet) AnimationUtils.loadAnimation(getActivity(),R.anim.translate_more_out);
            translateAnimOutB.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIvTakePhoto.startAnimation(translateAnimOutA);
                    mIvSearch.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            translateAnimOutA.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIvTakePhoto.setVisibility(View.INVISIBLE);
                    mLlMore.setVisibility(View.GONE);
                    isAnimRunning = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        isAnimRunning = true;
        mIvSearch.startAnimation(translateAnimOutB);
    }

    @Override
    public void loadDataFinish() {
        if (getActivity() == null){
            return;
        }
        //加载列表
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new NoteRvListAdapter(getActivity());
                mRvNoteList.setAdapter(adapter);
                handleEmptyView();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyClient.getMyClient().getNoteV1Manager().unregisterDataLoadListener(this);
        MyClient.getMyClient().getAddNoteManager().unRegisterOnAddNoteListener(this);
    }

    @Override
    public void onAddNoteFinish() {
        if (getActivity() == null){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.initData();
                adapter.notifyDataSetChanged();
                handleEmptyView();
            }
        });
    }
}
