package com.simplenote.module.pic;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.imagepipeline.common.ResizeOptions;
import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.widgets.MyPhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by melon on 2017/7/18.
 */

public class OpenPicActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private int mPostion;
    private TextView mTvNum;
    private TextView mTvTitle;
    private ViewPager mViewPager;
    private List<String> pathList = new ArrayList<>();
    private int imageResType;

    @BindView(R.id.relativeLayout1)
    RelativeLayout mRlBar;
    TranslateAnimation animationHide;
    TranslateAnimation animationShow;
    boolean isShowBar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pic);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        initListeners();
        hideAnimation(1500);
    }

    private void initListeners() {
        mViewPager.addOnPageChangeListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    private void getIntentData() {
        mPostion = getIntent().getIntExtra(Constant.BUNDEL.POSITION, 0);
        imageResType = getIntent().getIntExtra(Constant.BUNDEL.PIC_PATH_TYPE, Constant.VALUE.PIC_PATH_TYPE_FILE);
        PicPathModel pathModel = (PicPathModel) getIntent().getSerializableExtra(Constant.BUNDEL.PIC_PATH);
        pathList = pathModel.getImagePathList();

    }

    private void initView() {
        mTvNum = (TextView) findViewById(R.id.tv_num);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mViewPager = (ViewPager) findViewById(R.id.vp_pic);

        if (pathList.size() == 1){
            mTvNum.setVisibility(View.GONE);
        }else{
            mTvNum.setText((mPostion + 1) + "/" + pathList.size());
        }

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pathList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                MyPhotoView myPhotoView = new MyPhotoView(OpenPicActivity.this);
                if (!TextUtils.isEmpty(pathList.get(position))) {
                    Uri uri;
                    if (imageResType == Constant.VALUE.PIC_PATH_TYPE_FILE){
                        uri = Uri.fromFile(new File(pathList.get(position)));
                    }else{
                        uri = Uri.parse(pathList.get(position));
                    }
                    myPhotoView.setImageUri(uri, new ResizeOptions(50, 50));
                }
                myPhotoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        if (isShowBar){
                            hideAnimation(0);
                        }else{
                            showAnimation();
                        }
                    }

                    @Override
                    public void onOutsidePhotoTap() {
                        //finish();
                    }
                });
                myPhotoView.setAllowParentInterceptOnEdge(true);
                container.addView(myPhotoView);
                return myPhotoView;
            }


        });
        mViewPager.setCurrentItem(mPostion);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTvNum.setText((position + 1) + "/" + pathList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(this);
    }

    private void showAnimation(){
        if (animationShow == null){
            animationShow = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-1f,Animation.RELATIVE_TO_SELF,0f);
            animationShow.setDuration(250);
            animationShow.setFillAfter(true);
            animationShow.setInterpolator(new LinearInterpolator());
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mRlBar.startAnimation(animationShow);
                isShowBar = true;
            }
        });
    }

    private void hideAnimation(int delayTime){
        if (animationHide == null){
            animationHide = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-1f);
            animationHide.setDuration(250);
            animationHide.setFillAfter(true);
            animationHide.setInterpolator(new LinearInterpolator());
        }

        if (delayTime > 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRlBar.startAnimation(animationHide);
                    isShowBar = false;
                }
            },1500);
        }else{
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mRlBar.startAnimation(animationHide);
                    isShowBar = false;
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        finish();
    }

}
