package com.simplenote.module.oos.download;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.application.MyClient;
import com.simplenote.database.NoteUtils;
import com.simplenote.database.OnHandleDataFinishListener;
import com.simplenote.database.model.Note;
import com.simplenote.module.oos.callback.OnCheckIsSyncListener;
import com.simplenote.module.oos.callback.OnGetUploadConfigListener;
import com.simplenote.module.oos.callback.OnUploadFinishListener;
import com.simplenote.module.oos.callback.OnUploadNoteListener;
import com.simplenote.module.oos.upload.UploadIdACodeModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/8/7.
 */

public class OSSDownloadActivity extends BaseActivity implements OnGetUploadConfigListener,OnCheckIsSyncListener,OnUploadNoteListener,OnUploadFinishListener
{

//    @BindView(R.id.iv_sync_up)
//    ImageView mIvSyncUp;
//
//    private AnimationDrawable animationDrawable;

    private List<String> needUploadList = new ArrayList<>();

    @BindView(R.id.tv_tool_bar_title)
    TextView mTvTitle;

    @BindView(R.id.tv_sync_download_total)
    TextView mTvTotalCount;

    @BindView(R.id.tv_sync_download_success)
    TextView mTvSuccessCount;
    int successCount = 0;

    @BindView(R.id.tv_sync_download_fail)
    TextView mTvFailCount;
    int failCount = 0;

    @BindView(R.id.tv_sync_download_progress)
    TextView mTvSyncSituaTip;

    @BindView(R.id.tv_sync_download_start)
    TextView mTvDownloadStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        mTvTitle.setText(R.string.sync_download_title);

//        mIvSyncUp.setImageResource(R.drawable.sync_up);
//        animationDrawable = (AnimationDrawable) mIvSyncUp.getDrawable();

    }

    @OnClick(R.id.iv_bar_left_icon)
    void back(){
        finish();
    }

    private int TYPE_ING = 0;
    private int TYPE_SUCCESS = 1;
    private int TYPE_FAIL = 2;
    private void handleTip(int type){
        if (mTvSyncSituaTip.getVisibility() == View.GONE){
            mTvSyncSituaTip.setVisibility(View.VISIBLE);
        }

        if (type == TYPE_ING){
            mTvSyncSituaTip.setText(R.string.sync_download_ing);
        }else if (type == TYPE_SUCCESS){
            mTvSyncSituaTip.setText(R.string.sync_download_success);
        }else{
            mTvSyncSituaTip.setText(R.string.sync_download_fail);
        }


    }

    @OnClick(R.id.tv_sync_download_check)
    void checkNotYetList(){

        mTvDownloadStart.setSelected(false);
        mTvDownloadStart.setClickable(false);
        // TODO: 2017/8/7 得把note信息存在数据库
        //获取所有日记
        List<Note> noteModelList = MyClient.getMyClient().getNoteV1Manager().getNoteModels();

        List<UploadIdACodeModel> modelList = new ArrayList<>();
        for (Note model : noteModelList){
            UploadIdACodeModel mo = new UploadIdACodeModel();
            mo.setNoteId(model.getNoteId());
            mo.setNoteCode(model.getNoteCode());
            modelList.add(mo);
        }

        MyClient.getMyClient().getOssDownloadManager().checkIsDownload(modelList,this);
    }

    /**
     * 检查还未上传的日记列表
     * @param needUploadList
     */
    @Override
    public void onCheckSyncFinish(List<String> needUploadList) {
        if (needUploadList == null){
            Toast.makeText(this,"抱歉，执行同步任务失败，请稍后重试或联系客服。:1100",Toast.LENGTH_LONG).show();
            hideProgress();
            return;
        }

        if (needUploadList.isEmpty()){
            Toast.makeText(this,"本地已经更新好最新日志",Toast.LENGTH_LONG).show();
            hideProgress();
            return;
        }

        mTvDownloadStart.setSelected(true);
        mTvDownloadStart.setClickable(true);
        mTvTotalCount.setVisibility(View.VISIBLE);
        mTvTotalCount.setText(getString(R.string.sync_download_need_total_count,needUploadList.size()));

        this.needUploadList = needUploadList;


    }


    /**
     * 开始同步数据
     * 获取OSS配置
     */
    @OnClick(R.id.tv_sync_download_start)
    void handleSync(){
        mTvDownloadStart.setSelected(false);
        mTvDownloadStart.setClickable(false);
        handleTip(TYPE_ING);
        MyClient.getMyClient().getOSSManager().getUploadConfig(Constant.VALUE.OSS_CONFIG_TYPE_DOWNLOAD,this);
    }

    @Override
    public void onGetConfigFinish(boolean isSuccess) {
        if (!isSuccess){
            Toast.makeText(this,"抱歉，执行同步任务失败，请稍后重试或联系客服。:1000",Toast.LENGTH_LONG).show();
            handleTip(TYPE_FAIL);
            return;
        }

        MyClient.getMyClient().getOssDownloadManager().handleDownloadNoteInfo(needUploadList,this,this);
    }



    /**
     * 上传完日记的回调
     * 接下来上传图片
     * @param isSuccess
     */
    @Override
    public void onUploadNoteFinish(boolean isSuccess,int index,int status) {

        if (isSuccess){
            successCount++;

            final Note noteModel = MyClient.getMyClient().getOssDownloadManager().getNoteModel();
            if (noteModel.getCode() == -1){
//                // TODO: 2017/10/20 删除日记以及图片 只负责同步到本地，不覆盖本地
//                MyClient.getMyClient().getAddNoteManager().removeNoteFromFile(noteModel.getNoteId(),false);
//                List<String> imagePathList = noteModel.getImageNameList();
//                for (String imageName : imagePathList){
//                    String localPath = MyClient.getMyClient().getStorageManager().getImagePath() + imageName + AddNoteManager.SUPPORT_TYPE;
//                    FileUtil.deleteFile(new File(localPath));
//                }
            }else{
                //如果已经存在，则删除
                // TODO: 2017/10/20 只需保存日记，图片已经下载到本地
                if (NoteUtils.isExist(noteModel.getNoteId())){
                    NoteUtils.handleData(NoteUtils.OPERATE_UPDATE,noteModel,false,null);
                }else{
                    NoteUtils.handleData(NoteUtils.OPERATE_INSERT, noteModel, false, new OnHandleDataFinishListener() {
                        @Override
                        public void onDataFinish() {
                            MyClient.getMyClient().getNoteV1Manager().insertNewNote(noteModel);
                        }
                    });
                }
            }
        }else{
            failCount++;

        }


        //继续下载日记
        //给点时延，减小IO压力
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvSuccessCount.setVisibility(View.VISIBLE);
                mTvFailCount.setVisibility(View.VISIBLE);
                mTvSuccessCount.setText(getString(R.string.sync_download_need_success_count,successCount));
                mTvFailCount.setText(getString(R.string.sync_download_need_fail_count,failCount));
                MyClient.getMyClient().getOssDownloadManager().downNoteInfo();
            }
        },100);

    }

    @Override
    public void onUploadFinish(boolean isSuccess, String msg) {
        if (!isSuccess){
            Toast.makeText(this,"同步信息失败，请稍后重试或联系客服。:"+msg,Toast.LENGTH_LONG).show();
            handleTip(TYPE_FAIL);
            return;
        }

        handleTip(TYPE_SUCCESS);
        // TODO: 2017/8/8 更新成功
        MyClient.getMyClient().getAddNoteManager().displayOnAddNoteListener();

    }
}
