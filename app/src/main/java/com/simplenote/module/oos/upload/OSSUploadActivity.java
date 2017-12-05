package com.simplenote.module.oos.upload;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.application.MyClient;
import com.simplenote.model.NoteModel;
import com.simplenote.module.oos.callback.OnCheckIsSyncListener;
import com.simplenote.module.oos.callback.OnGetUploadConfigListener;
import com.simplenote.module.oos.callback.OnUploadFinishListener;
import com.simplenote.module.oos.callback.OnUploadNoteListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/8/7.
 */

public class OSSUploadActivity extends BaseActivity implements OnGetUploadConfigListener,OnCheckIsSyncListener,OnUploadNoteListener,OnUploadFinishListener
{

    @BindView(R.id.iv_sync_up)
    ImageView mIvSyncUp;

    private AnimationDrawable animationDrawable;

    private List<String> needUploadList = new ArrayList<>();

    @BindView(R.id.tv_sync_upload_total)
    TextView mTvTotalCount;

    @BindView(R.id.tv_sync_upload_success)
    TextView mTvSuccessCount;
    int successCount;

    @BindView(R.id.tv_sync_upload_fail)
    TextView mTvFailCount;
    int failCount;

    @BindView(R.id.tv_sync_upload_progress)
    TextView mTvSyncSituaTip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);


        mIvSyncUp.setImageResource(R.drawable.sync_up);
        animationDrawable = (AnimationDrawable) mIvSyncUp.getDrawable();

    }

    private int TYPE_ING = 0;
    private int TYPE_SUCCESS = 1;
    private int TYPE_FAIL = 2;
    private void handleTip(int type){
        if (mTvSyncSituaTip.getVisibility() == View.GONE){
            mTvSyncSituaTip.setVisibility(View.VISIBLE);
        }

        if (type == TYPE_ING){
            mTvSyncSituaTip.setText(R.string.sync_upload_ing);
        }else if (type == TYPE_SUCCESS){
            mTvSyncSituaTip.setText(R.string.sync_upload_success);
        }else{
            mTvSyncSituaTip.setText(R.string.sync_upload_fail);
        }


    }

    @OnClick(R.id.tv_sync_upload_check)
    void checkNotYetList(){
//        String notePath = MyClient.getMyClient().getStorageManager().getNotePath();
//        List<String> name = FileUtil.listFileNames(notePath);

        // TODO: 2017/8/7 得把note信息存在数据库
        //获取所有日记
        List<NoteModel> noteModelList = MyClient.getMyClient().getNoteV1Manager().getNoteModels();
        List<NoteModel> deleteNoteList = MyClient.getMyClient().getNoteV1Manager().getDeleteNoteModels();

        if (noteModelList == null){
            noteModelList = new ArrayList<>();
        }

        if (deleteNoteList == null){
            deleteNoteList = new ArrayList<>();
        }
        if (noteModelList.isEmpty() && deleteNoteList.isEmpty()){
            Toast.makeText(this,"你还没有写过日记哦～",Toast.LENGTH_LONG).show();
            return;
        }

        noteModelList.addAll(deleteNoteList);

        List<UploadIdACodeModel> modelList = new ArrayList<>();
        for (NoteModel model : noteModelList){
            UploadIdACodeModel mo = new UploadIdACodeModel();
            mo.setNoteId(model.getNoteId());
            mo.setNoteCode(model.getNoteCode());
            mo.setStatus(model.getStatus());
            modelList.add(mo);
        }

        MyClient.getMyClient().getOssUploadManager().checkIsUpload(modelList,this);
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
            Toast.makeText(this,"服务器已经保存好最新日志",Toast.LENGTH_LONG).show();
            hideProgress();
            return;
        }

        mTvTotalCount.setText(getString(R.string.sync_upload_need_total_count,needUploadList.size()));

        this.needUploadList = needUploadList;


    }


    /**
     * 开始同步数据
     * 获取OSS配置
     */
    @OnClick(R.id.tv_sync_upload_start)
    void handleSync(){
        handleTip(TYPE_ING);
        MyClient.getMyClient().getOSSManager().getUploadConfig("upload",this);
    }

    @Override
    public void onGetConfigFinish(boolean isSuccess) {
        if (!isSuccess){
            Toast.makeText(this,"抱歉，执行同步任务失败，请稍后重试或联系客服。:1000",Toast.LENGTH_LONG).show();
            handleTip(TYPE_FAIL);
            return;
        }

        MyClient.getMyClient().getOssUploadManager().handleUploadNoteInfo(needUploadList,this,this);
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
            mTvSuccessCount.setText(getString(R.string.sync_upload_need_success_count,successCount));
        }else{
            failCount++;
            mTvFailCount.setText(getString(R.string.sync_upload_need_fail_count,failCount));
        }

        if (status == -1){
            String id = MyClient.getMyClient().getOssUploadManager().getIdByIndex(index);
            MyClient.getMyClient().getAddNoteManager().removeNoteFromFile(id,false);
        }

        MyClient.getMyClient().getOssUploadManager().continueUpload();

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

        //删除多余图片
        MyClient.getMyClient().getOssUploadManager().deleteExtraImage();

    }
}
