package com.simplenote.module.photo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.module.imgselect.ImgFileListActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/7/27.
 */

public class ChooseTypeDialog extends Dialog {

    private View contentView;

    private Context context;
    
    private View.OnClickListener listener;

    public ChooseTypeDialog(@NonNull Context context,View.OnClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        initView();
    }

    private void initView(){
        int width = context.getResources().getDimensionPixelSize(R.dimen.dialog_choose_type_w);
        int height = WindowManager.LayoutParams.WRAP_CONTENT;//context().getResources().getDimensionPixelSize(R.dimen.pay_dialog_h);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.y = -60;
        lp.height = height;
        lp.width = width;

        getWindow().setAttributes(lp);
        getWindow().getDecorView().setBackgroundColor(context.getResources().getColor(R.color.transparent));
        setCanceledOnTouchOutside(true);

        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_choose_photo_type, null);
        ViewGroup.LayoutParams param = new LinearLayout.LayoutParams(width, height);
        setContentView(contentView, param);
        ButterKnife.bind(this,contentView);
    }

    @OnClick(R.id.ll_take_photo_access_own)
    void takePhotoOwn(){
        if (context == null){
            return;
        }
        Intent intent = new Intent(context, ImgFileListActivity.class);
        ((Activity)context).startActivityForResult(intent, Constant.REQUEST.VALUE_INTENT_TO_OWN);
        dismiss();
    }

    @OnClick(R.id.ll_take_photo_access_take)
    void takePhotoDevice(){
        if (context == null){
            return;
        }
        if (listener != null){
            listener.onClick(null);
        }
        dismiss();
    }

}
