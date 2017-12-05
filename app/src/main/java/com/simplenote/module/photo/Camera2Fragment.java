package com.simplenote.module.photo;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.simplenote.R;
import com.simplenote.util.CommonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/7/27.
 */

public class Camera2Fragment extends Camera2BasicFragment {

//    @BindView(R.id.fl_take_photo_content)
//    RelativeLayout mFlContent;

    @BindView(R.id.iv_take_photo_back)
    ImageView mIvTakeBack;

    @BindView(R.id.iv_take_photo_cancel)
    ImageView mIvTakeCancel;

    @BindView(R.id.iv_take_photo_ok)
    ImageView mIvTakeOk;

    @BindView(R.id.iv_take_photo_action)
    ImageView mIvTakeAction;

    public static Camera2Fragment newInstance() {
        return new Camera2Fragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    @Override
    void handleContentLayout(int surfaceHeight) {
//        int height = CommonUtil.getScreenHeight(getActivity())-CommonUtil.getBottomStatusHeight(getActivity())-surfaceHeight;
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mFlContent.getLayoutParams();
//        params.height = height;
//        Log.i("surfaceHeight",surfaceHeight+"");
//        Log.i("height",height+"");
//        mFlContent.setLayoutParams(params);
    }

    @OnClick(R.id.iv_take_photo_action)
    void takePhotoAction(){
        takePicture();
    }

    @OnClick(R.id.iv_take_photo_back)
    void takePhotoBack(){
        getActivity().finish();
    }

    @OnClick(R.id.iv_take_photo_ok)
    void takePhotoOk(){
        mOnImageAvailableListener.saveImageToFile(new Runnable() {
            @Override
            public void run() {
                getActivity().finish();
            }
        });
        // TODO: 2017/7/27 可能图片还没保存完

    }

    @OnClick(R.id.iv_take_photo_cancel)
    void takePhotoCancel() {
        unlockFocus();
        isUiPreview(true);
    }

    @Override
    void takePhotoFinish() {
        isUiPreview(false);
    }

    private void isUiPreview(final boolean flag){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIvTakeCancel.setVisibility(flag?View.GONE:View.VISIBLE);
                mIvTakeOk.setVisibility(flag?View.GONE:View.VISIBLE);

                mIvTakeAction.setVisibility(flag?View.VISIBLE:View.GONE);
                mIvTakeBack.setVisibility(flag?View.VISIBLE:View.GONE);
            }
        });
    }

    @Override
    Size filterScreenSize(Size[] sizes) {
        List<Integer> screenSize = CommonUtil.getScreenSize(getActivity());

        List<Size> sizeList = new ArrayList<>();

        for (Size size: sizes){
            if (screenSize.get(1)==size.getWidth()&&screenSize.get(0)==size.getHeight()){
                return size;
            }
            if (size.getWidth()>=screenSize.get(1)){
                sizeList.add(size);
            }
        }

        float screenDiff = (float)screenSize.get(0)/screenSize.get(1);
        float tempDiff = 100;
        Size sizeReal = null;
        List<Float> sizeDiff = new ArrayList<>();
        for (Size size : sizeList){
            float temp = Math.abs(screenDiff - (float)size.getHeight()/size.getWidth());
            if (temp<tempDiff){
                sizeReal = size;
                tempDiff = temp;
            }
        }



        return sizeReal;
    }



}
