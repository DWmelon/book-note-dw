package com.simplenote.module.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.application.MyClient;
import com.simplenote.database.model.Note;
import com.simplenote.module.add.AddNoteActivity;
import com.simplenote.module.add.AddNoteManager;
import com.simplenote.module.add.AddNotePicAdapter;
import com.simplenote.module.share.ThemeActivity;
import com.simplenote.util.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/9/26.
 */

public class NoteDetailActivity extends BaseActivity {

    @BindView(R.id.tv_add_note_title)
    TextView mTvTitle;

    @BindView(R.id.ll_bar_right_content)
    LinearLayout mLlRightBar;

    @BindView(R.id.tv_add_note_time_left)
    TextView mTvDateL;

    @BindView(R.id.tv_add_note_time_right)
    TextView mTvDateS;

    @BindView(R.id.ll_add_note_info_icons)
    LinearLayout mLlEmotion;

    @BindView(R.id.rv_add_note_pic)
    RecyclerView mRvImages;

    @BindView(R.id.tv_add_note_content)
    TextView mTvContent;

    @BindView(R.id.tv_add_note_count)
    TextView mTvFontCount;

    private String noteId;
    private Note noteModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ButterKnife.bind(this);

        noteId = getIntent().getExtras().getString(Constant.BUNDEL.NOTE_ID);
        noteModel = MyClient.getMyClient().getNoteV1Manager().getNoteModels(noteId);

        initView();
        initDate();
        
    }

    private void initView(){
        mTvTitle.setText("顾");

        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.icon_tab_write_press);
        int padding = getResources().getDimensionPixelOffset(R.dimen.margin_10);
        iv.setPadding(padding,padding,padding,padding);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteDetailActivity.this, AddNoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.KEY.PAGE_TYPE,Constant.VALUE.TYPE_NOTE_DETAIL_MODIFY);
                bundle.putSerializable(Constant.KEY.NOTE_MODEL,noteModel);
                intent.putExtras(bundle);
                startActivityForResult(intent,Constant.REQUEST.INTENT_NOTE_DETAIL_MODIFY);
            }
        });
        mLlRightBar.addView(iv);

    }
    
    private void initDate(){


        if (TextUtils.isEmpty(noteModel.getTitle())){
            mTvTitle.setVisibility(View.GONE);
            findViewById(R.id.v_title_line).setVisibility(View.GONE);
            findViewById(R.id.v_title_line2).setVisibility(View.VISIBLE);
        }else{
            mTvTitle.setText(noteModel.getTitle());
        }

        
        if (TextUtils.isEmpty(noteModel.getContent())){
            mTvContent.setVisibility(View.GONE);
            mTvFontCount.setVisibility(View.GONE);
        }else{
            mTvContent.setText(noteModel.getContent());
            mTvFontCount.setText(String.valueOf(noteModel.getContent().length()));    
        }

        Date date = new Date(noteModel.getCreateDate());
        mTvDateL.setText(TimeUtils.getNowTime(date,"yyyy-MM-dd"));
        mTvDateS.setText(TimeUtils.getNowTime(date,"hh:mm"));

        updateInfoIcons();

        GridLayoutManager staggered=new GridLayoutManager(this,3);
        mRvImages.setLayoutManager(staggered);
        mRvImages.setAdapter(new AddNotePicAdapter(this,filterImagePath(),false));
    }

    private List<String> filterImagePath(){
        List<String> realImagePaths = new ArrayList<>();
        for (String str : noteModel.getImageList()){
            realImagePaths.add(MyClient.getMyClient().getStorageManager().getImagePath() + str + AddNoteManager.SUPPORT_TYPE);
        }
        return realImagePaths;
    }

    private void updateInfoIcons(){
        mLlEmotion.removeAllViews();
        int emotionImageRes = MyClient.getMyClient().getAddNoteManager().getImageResES(AddNoteManager.TYPE_EMOTION,noteModel);
        if (emotionImageRes>0){
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,getResources().getDimensionPixelOffset(R.dimen.margin_10),0);
            iv.setImageResource(emotionImageRes);
            mLlEmotion.addView(iv,params);
        }

        int weatherImageRes = MyClient.getMyClient().getAddNoteManager().getImageResES(AddNoteManager.TYPE_WEATHER,noteModel);
        if (weatherImageRes>0){
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,getResources().getDimensionPixelOffset(R.dimen.margin_10),0);
            iv.setImageResource(weatherImageRes);
            mLlEmotion.addView(iv,params);
        }

        int themeImageRes = MyClient.getMyClient().getAddNoteManager().getImageResES(AddNoteManager.TYPE_THEME,noteModel);
        if (themeImageRes>0){
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,getResources().getDimensionPixelOffset(R.dimen.margin_10),0);
            iv.setImageResource(themeImageRes);
            mLlEmotion.addView(iv,params);
        }

    }

    @OnClick(R.id.iv_note_detail_share)
    void handlePostCard(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY.NOTE_MODEL,noteModel);
        Intent intent = new Intent(this, ThemeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.iv_bar_left_icon)
    void back(){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.REQUEST.INTENT_NOTE_DETAIL_MODIFY:{
                noteModel = MyClient.getMyClient().getNoteV1Manager().getNoteModels(noteId);
                initDate();
                break;
            }
        }
    }
}
