package com.simplenote.module.add;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.MyClient;
import com.simplenote.database.NoteUtils;
import com.simplenote.database.OnHandleDataFinishListener;
import com.simplenote.database.model.Note;
import com.simplenote.util.FileUtil;
import com.simplenote.util.InputSoftKeyUtils;
import com.simplenote.util.RandomUtils;
import com.simplenote.util.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by melon on 2017/7/17.
 */

public class AddNoteActivity extends AddNoteBaseActivity{

    private TextView mTvFontCount;

    private TextView mTvDateYMD;
    private TextView mTvDateHM;

    private ImageView mIvTakePhoto;

    private LinearLayout mLlInfoIcons;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initListener();
    }

    private void initView(){

        mTvFontCount = (TextView)findViewById(R.id.tv_add_note_count);


        mTvDateYMD = (TextView)findViewById(R.id.tv_add_note_time_left);
        mTvDateHM = (TextView)findViewById(R.id.tv_add_note_time_right);

        mLlInfoIcons = (LinearLayout)findViewById(R.id.ll_add_note_info_icons);

        mIvTakePhoto = (ImageView)findViewById(R.id.iv_add_note_take_photo);

    }

    private void initData(){

        if (TYPE_ADD_NOTE == Constant.VALUE.TYPE_NOTE_DETAIL_ADD){
            initAddData();
        }else{
            initModifyData();
        }

    }

    private void initAddData(){
        Date date = new Date();
        MyClient.getMyClient().getAddNoteManager().getNoteModel().setCreateDate(date.getTime());
        mTvDateYMD.setText(TimeUtils.getNowTime(date,"yyyy-MM-dd"));
        mTvDateHM.setText(TimeUtils.getNowTime(date,"HH:mm"));

        adapterPic = new AddNotePicAdapter(this,MyClient.getMyClient().getAddNoteManager().getNoteImagePaths(),true);
        mRvPic.setAdapter(adapterPic);
    }

    private void initModifyData(){
        Date date = new Date(originNoteModel.getCreateDate());
        MyClient.getMyClient().getAddNoteManager().getNoteModel().setCreateDate(date.getTime());
        mTvDateYMD.setText(TimeUtils.getNowTime(date,"yyyy-MM-dd"));
        mTvDateHM.setText(TimeUtils.getNowTime(date,"HH:mm"));

        List<String> imagePathList = new ArrayList<>();
        for (String name : originNoteModel.getImageList()){
            imagePathList.add(MyClient.getMyClient().getStorageManager().getImagePath() + name + AddNoteManager.SUPPORT_TYPE);
        }
        MyClient.getMyClient().getAddNoteManager().setNoteImagePaths(imagePathList);

        adapterPic = new AddNotePicAdapter(this,MyClient.getMyClient().getAddNoteManager().getNoteImagePaths(),true);
        mRvPic.setAdapter(adapterPic);

        mEtTitle.setText(originNoteModel.getTitle());
        mEtContent.setText(originNoteModel.getContent());

        MyClient.getMyClient().getAddNoteManager().setEmotion(originNoteModel.getEmotion());
        MyClient.getMyClient().getAddNoteManager().setWeather(originNoteModel.getWeather());
        MyClient.getMyClient().getAddNoteManager().setTheme(originNoteModel.getTheme());
        updateInfoIcons();
    }

    private void initListener(){
        mEtContent.addTextChangedListener(new EditTypeListener());
    }

    public class EditTypeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mTvFontCount.setText(String.valueOf(editable.length()));
        }
    }

    @OnClick(R.id.iv_add_note_take_photo)
    void handleMenuTakePhoto(){
        showChooseTypeDialog();
    }

    @OnClick(R.id.iv_add_note_select_emotion)
    void handleMenuSelectEmotion(){
        Intent intent = new Intent(this,SelectEmotionActivity.class);
        startActivityForResult(intent, Constant.REQUEST.SELECT_EMOTION);
    }

    @OnClick(R.id.iv_bar_left_icon)
    void back(){
        if (isShow()){
            InputSoftKeyUtils.hideSoftKeyWord(AddNoteActivity.this,mEtContent);
        }else{
            finish();
        }
    }

    protected void handleFinishNote(){
        if (TYPE_ADD_NOTE == Constant.VALUE.TYPE_NOTE_DETAIL_ADD){
            addNote();
        }else{
            modifyNote();
        }
    }

    private void updateInfoIcons(){
        mLlInfoIcons.removeAllViews();
        int emotionImageRes = MyClient.getMyClient().getAddNoteManager().getImageResES(AddNoteManager.TYPE_EMOTION);
        if (emotionImageRes>0){
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,getResources().getDimensionPixelOffset(R.dimen.margin_10),0);
            iv.setImageResource(emotionImageRes);
            mLlInfoIcons.addView(iv,params);
        }

        int weatherImageRes = MyClient.getMyClient().getAddNoteManager().getImageResES(AddNoteManager.TYPE_WEATHER);
        if (weatherImageRes>0){
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,getResources().getDimensionPixelOffset(R.dimen.margin_10),0);
            iv.setImageResource(weatherImageRes);
            mLlInfoIcons.addView(iv,params);
        }

        int themeImageRes = MyClient.getMyClient().getAddNoteManager().getImageResES(AddNoteManager.TYPE_THEME);
        if (themeImageRes>0){
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,getResources().getDimensionPixelOffset(R.dimen.margin_10),0);
            iv.setImageResource(themeImageRes);
            mLlInfoIcons.addView(iv,params);
        }

    }

    private void addNote(){
        final Note model = MyClient.getMyClient().getAddNoteManager().getNoteModel();
        model.setTitle(mEtTitle.getText().toString().trim());
        model.setContent(mEtContent.getText().toString().trim());
        model.setImageNameList("");


        long time = model.getCreateDate();
        model.setNoteId(MyClient.getMyClient().getAccountManager().getUserId()+"_"+time);
        model.setNoteCode(RandomUtils.genRandomCode());
        model.setUserId(MyClient.getMyClient().getAccountManager().getUserId());
        List<String> imagePathList = MyClient.getMyClient().getAddNoteManager().getNoteImagePaths();
        for (int i = 0;i < imagePathList.size();i++){
            String imageName = time+"_"+i;
            String path = MyClient.getMyClient().getStorageManager().getImagePath() + imageName + AddNoteManager.SUPPORT_TYPE;
            File file = new File(imagePathList.get(i));
            FileUtil.copyFile(file,path);
            model.addImage(imageName);
        }

        NoteUtils.handleData(NoteUtils.OPERATE_INSERT, model, new OnHandleDataFinishListener() {
            @Override
            public void onDataFinish() {
                MyClient.getMyClient().getNoteV1Manager().insertNewNote(model);
            }
        });

        finish();
    }

    private void modifyNote(){
        final Note model = MyClient.getMyClient().getAddNoteManager().getNoteModel();
        model.setTitle(mEtTitle.getText().toString().trim());
        model.setContent(mEtContent.getText().toString().trim());
        model.setImageNameList("");

        long time = new Date().getTime();
        model.setNoteCode(RandomUtils.genRandomCode());

        List<String> imagePathList = MyClient.getMyClient().getAddNoteManager().getNoteImagePaths();
        for (int i = 0;i < imagePathList.size();i++){
            String path = imagePathList.get(i);

            String imageName;
            //如果图片已经在目录下，则不复制文件。
            if (path.contains(MyClient.getMyClient().getStorageManager().getImagePath())){
                imageName = path.replace(MyClient.getMyClient().getStorageManager().getImagePath(),"").replace(AddNoteManager.SUPPORT_TYPE,"");
            }else{
                imageName = time+"_"+i;
                String newPath = MyClient.getMyClient().getStorageManager().getImagePath() + imageName + AddNoteManager.SUPPORT_TYPE;
                File file = new File(imagePathList.get(i));
                FileUtil.copyFile(file,newPath);
            }


            model.addImage(imageName);
        }

        NoteUtils.handleData(NoteUtils.OPERATE_UPDATE, model, new OnHandleDataFinishListener() {
            @Override
            public void onDataFinish() {
                MyClient.getMyClient().getNoteV1Manager().insertOldNote(model);
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.REQUEST.SELECT_EMOTION:{
                updateInfoIcons();
                break;
            }
            case Constant.REQUEST.VALUE_INTENT_TO_OWN:{
                adapterPic.notifyDataSetChanged();
                break;
            }
            case Constant.REQUEST.VALUE_INTENT_TO_TAKE:{
                adapterPic.notifyDataSetChanged();
                break;
            }
        }

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyClient.getMyClient().getAddNoteManager().clean();
    }
}
