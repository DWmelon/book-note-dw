package com.simplenote.module.add;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.application.MyClient;
import com.simplenote.model.NoteModel;
import com.simplenote.module.permission.OnRequestPermissionFinish;
import com.simplenote.module.permission.PermissionManager;
import com.simplenote.module.photo.Camera2FaceActivity;
import com.simplenote.module.photo.ChooseTypeDialog;
import com.simplenote.util.CommonUtil;
import com.simplenote.util.InputSoftKeyUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/7/18.
 */

public class AddNoteBaseActivity extends BaseActivity implements InputSoftKeyUtils.OnGetSoftHeightListener,InputSoftKeyUtils.OnSoftKeyWordShowListener,OnRequestPermissionFinish {

    public static final int TYPE_FOCUS_TITLE = 0;
    public static final int TYPE_FOCUS_CONTENT = 1;
    public int focusType = TYPE_FOCUS_TITLE;

    private View mRlContent;

    private ScrollView mSvContent;
    private int editHeightOrigin;
    private int editHeightAfter;

    protected EditText mEtTitle;
    protected EditText mEtContent;

    protected RecyclerView mRvPic;
    protected AddNotePicAdapter adapterPic;

    private TextView mTvTitle;

    private boolean isShow = false;

    private ChooseTypeDialog chooseTypeDialog;

    protected int TYPE_ADD_NOTE = Constant.VALUE.TYPE_NOTE_DETAIL_ADD;
    protected NoteModel originNoteModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        initBundle();
        initView();
        initData();
        initListener();

    }

    private void initBundle(){
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            TYPE_ADD_NOTE = Constant.VALUE.TYPE_NOTE_DETAIL_ADD;
            return;
        }

        TYPE_ADD_NOTE = bundle.getInt(Constant.KEY.PAGE_TYPE,Constant.VALUE.TYPE_NOTE_DETAIL_ADD);

        if (TYPE_ADD_NOTE == Constant.VALUE.TYPE_NOTE_DETAIL_MODIFY){
            originNoteModel = (NoteModel) bundle.getSerializable(Constant.KEY.NOTE_MODEL);
        }
    }

    private void initListener(){
        InputSoftKeyUtils.doMonitorSoftKeyWord(mRlContent,this);
        InputSoftKeyUtils.getSoftKeyboardHeight(mRlContent,this);

        mEtTitle.setOnTouchListener(new OnEditTouchListener(TYPE_FOCUS_TITLE));
        mEtContent.setOnTouchListener(new OnEditTouchListener(TYPE_FOCUS_CONTENT));

    }

    private void initView(){
        mTvTitle = (TextView) findViewById(R.id.tv_tool_bar_title);

        mRvPic = (RecyclerView)findViewById(R.id.rv_add_note_pic);

        mRlContent =  findViewById(R.id.rl_add_note_rootview);
        mSvContent = (ScrollView)findViewById(R.id.sv_add_note);

        mEtTitle = (EditText)findViewById(R.id.et_add_note_title);
        mEtContent = (EditText)findViewById(R.id.et_add_note_content);


    }

    private void initData(){

        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText(R.string.add_note_title);

        GridLayoutManager staggered=new GridLayoutManager(this,3);
        mRvPic.setLayoutManager(staggered);
    }

    @OnClick(R.id.iv_bar_left_icon)
    void back(){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        return true;
    }


    private void adjustIconLayout(final boolean isShow){
        if (editHeightOrigin == 0 || this.isShow == isShow){
            return;
        }

        this.isShow = isShow;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isShow){
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,editHeightAfter);
                    mSvContent.setLayoutParams(params);

                    if (focusType == TYPE_FOCUS_CONTENT){
                        findViewById(R.id.ll_add_note_header).setVisibility(View.GONE);
                    }
                }else{
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,editHeightOrigin);
                    mSvContent.setLayoutParams(params);


                    findViewById(R.id.ll_add_note_header).setVisibility(View.VISIBLE);

                }
            }
        });

    }


    @Override
    public void onShowed(final int height) {


        if (mSvContent.getHeight() <=  0){
            mSvContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    actionIconOpen(height);
                }
            });
        }else{
            actionIconOpen(height);
        }



    }

    private void actionIconOpen(int height){
        editHeightOrigin = mSvContent.getHeight();
        editHeightAfter = editHeightOrigin - height + CommonUtil.getBottomStatusHeight(this);
        adjustIconLayout(true);
    }


    @Override
    public void hasShow(boolean isShow) {
        adjustIconLayout(isShow);
    }

    protected boolean isShow() {
        return isShow;
    }

    public class OnEditTouchListener implements View.OnTouchListener{

        int type;

        public OnEditTouchListener(int index){
            type = index;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (type == TYPE_FOCUS_TITLE){

            }else{
                if (isShow){
                    findViewById(R.id.ll_add_note_header).setVisibility(View.GONE);
                }
            }
            focusType = type;
            return false;
        }
    }

    /**
     * 选择照片方式
     */
    protected void showChooseTypeDialog(){
        if (chooseTypeDialog == null){
            chooseTypeDialog = new ChooseTypeDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyClient.getMyClient().getPermissionManager().handlePermission(AddNoteBaseActivity.this, PermissionManager.PERMISSION_CAMERA,AddNoteBaseActivity.this);
                }
            });
        }
        chooseTypeDialog.show();
    }

    /**
     * 系统权限设置弹框
     */
    private void showSettingDialog(){
        showCommonAlert(R.string.dialog_title,R.string.dialog_permission_lack,R.string.dialog_setting,R.string.dialog_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:"+ getPackageName()));
                startActivityForResult(intent,Constant.REQUEST.INTENT_SYSTEM_SETTING);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyClient.getMyClient().getPermissionManager().onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionFinish(boolean result) {
        if (result){
            Intent i = new Intent(this, Camera2FaceActivity.class);
            startActivityForResult(i, Constant.REQUEST.VALUE_INTENT_TO_TAKE);
        }else{
            showSettingDialog();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        InputSoftKeyUtils.isFirst = true;
    }

}
