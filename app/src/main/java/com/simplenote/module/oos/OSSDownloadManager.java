package com.simplenote.module.oos;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.simplenote.constants.Constant;
import com.simplenote.application.MyClient;
import com.simplenote.model.NoteModel;
import com.simplenote.module.add.AddNoteManager;
import com.simplenote.module.oos.callback.OnCheckIsSyncListener;
import com.simplenote.module.oos.callback.OnUploadFinishListener;
import com.simplenote.module.oos.callback.OnUploadNoteListener;
import com.simplenote.module.oos.upload.OSSCheckSyncModel;
import com.simplenote.module.oos.upload.UploadIdACodeModel;
import com.simplenote.network.IRequest;
import com.simplenote.network.IRequestCallback;
import com.simplenote.util.FileUtil;
import com.simplenote.util.V2ArrayUtil;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by melon on 2017/10/20.
 */

public class OSSDownloadManager {



    private OnUploadNoteListener noteListener;
    private OnUploadFinishListener uploadFinishListener;

    private int uploadNoteIndex;
    private int uploadImageIndex;
    private List<String> idList;
    private NoteModel noteModel;


    private NoteModel noteTempModel;
    private boolean isModifying = false;

    /**
     * 检查还未上传的日记id列表
     * @param listener
     */
    public void checkIsDownload(List<UploadIdACodeModel> idList, final OnCheckIsSyncListener listener){

        HashMap<String,String> map = new HashMap<>();
        map.put("idList", V2ArrayUtil.getJsonArrData(idList));
        map.put(Constant.PARAM.TOKEN, MyClient.getMyClient().getAccountManager().getToken());

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(OSSManager.URL_DOWNLOAD_CHECK, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.onCheckSyncFinish(null);
                    return;
                }


                OSSCheckSyncModel model = new OSSCheckSyncModel();
                model.decode(jsonObject);


                if (model.getCode() == 0){
                    listener.onCheckSyncFinish(model.getIdList());
                }else{
                    listener.onCheckSyncFinish(null);
                }

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onCheckSyncFinish(null);
                }
            }
        });
    }


    /**
     * 下载日记信息
     * @param idList
     * @param listener
     */
    public void  handleDownloadNoteInfo(List<String> idList, final OnUploadNoteListener listener, OnUploadFinishListener listener2){
        uploadNoteIndex = -1;
        uploadImageIndex = -1;
        this.idList = idList;
        this.noteListener = listener;
        this.uploadFinishListener = listener2;

        downNoteInfo();
    }

    public void downNoteInfo(){
        uploadNoteIndex ++;
        if (uploadNoteIndex>=idList.size()){
            uploadFinishListener.onUploadFinish(true,"日记下载成功");
            return;
        }

        final String id = idList.get(uploadNoteIndex);

        HashMap<String,String> map = new HashMap<>();
        map.put(Constant.PARAM.NOTE_ID,id);
        map.put(Constant.PARAM.TOKEN,MyClient.getMyClient().getAccountManager().getToken());

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(OSSManager.URL_UPLOAD_GET_NOTE, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (noteListener == null) {
                    return;
                }

                if (jsonObject == null) {
                    uploadFinishListener.onUploadFinish(false, "server error");
                    return;
                }


                noteModel = new NoteModel();
                noteModel.decode(jsonObject);
                if (noteModel.getCode() == 0) {
                    if (!MyClient.getMyClient().getNoteV1Manager().isExistNote(id)){
                        uploadImageIndex = 0;
                        handleActionImage(OSSManager.ACTION_ADD);
                    } else {
                        //保存新日记，先删除旧日记
                        isModifying = true;
                        noteTempModel = noteModel;
                        noteModel = MyClient.getMyClient().getNoteV1Manager().getNoteModels(id);
                        noteModel.setCode(Constant.RESULT_CODE.CODE_LOCAL_HAD_SERVER_NOT);
                        uploadImageIndex = 0;
                        handleActionImage(OSSManager.ACTION_REMOVE);
                    }
                } else {
                    noteListener.onUploadNoteFinish(false, uploadNoteIndex, 0);
                }


            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (uploadFinishListener != null) {
                    uploadFinishListener.onUploadFinish(false,"server error"+code);
                }
            }
        });
    }

    private void handleActionImage(String action){

        if (uploadImageIndex>=noteModel.getImageNameList().size()){

            //修改操作，已经完成删除旧日记，下面这里判断是否修改操作，进行新增新日记操作
            if (noteTempModel != null && noteTempModel.getNoteId().equals(noteModel.getNoteId()) && isModifying){
                isModifying = false;
                noteModel = noteTempModel;
                noteModel.setCode(Constant.RESULT_CODE.CODE_LOCAL_NOT_SERVER_HAD);
                uploadImageIndex = 0;
                handleActionImage(OSSManager.ACTION_ADD);
                return;
            }

            noteListener.onUploadNoteFinish(true,uploadNoteIndex,0);
            return;
        }

        String imageName = noteModel.getImageNameList().get(uploadImageIndex);
        String targetPath = "image/"+MyClient.getMyClient().getAccountManager().getUserId()+"/"+imageName+ AddNoteManager.SUPPORT_TYPE;

        if (action.equals(OSSManager.ACTION_ADD)){
            downloadOssImage(imageName,targetPath);
        } else if (action.equals(OSSManager.ACTION_REMOVE)){
            deleteOssImage(imageName,targetPath);
        }


    }

    private void downloadOssImage(String imageName, String targetPath){

        if (MyClient.getMyClient().getOSSManager().checkKeyExpire()){
            return;
        }

        final String localPath = MyClient.getMyClient().getStorageManager().getImagePath() + imageName + AddNoteManager.SUPPORT_TYPE;

        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(OSSManager.bucketName, targetPath);
        OSSAsyncTask task = MyClient.getMyClient().getOSSManager().getOss().asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                Log.d("Content-Length", "" + result.getContentLength());
                InputStream inputStream = result.getObjectContent();
                FileUtil.writeImageToFile(inputStream,localPath);
                uploadImageIndex++;
                handleActionImage(OSSManager.ACTION_ADD);
            }
            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {

                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                noteListener.onUploadNoteFinish(false,uploadNoteIndex,0);
            }
        });
        task.waitUntilFinished(); // 如果需要等待任务完成
    }

    private void deleteOssImage(String imageName, String targetPath){

        final String localPath = MyClient.getMyClient().getStorageManager().getImagePath() + imageName + AddNoteManager.SUPPORT_TYPE;

        DeleteObjectRequest delete = new DeleteObjectRequest(OSSManager.bucketName, targetPath);
        // 异步删除
        OSSAsyncTask deleteTask = MyClient.getMyClient().getOSSManager().getOss().asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                Log.d("asyncCopyAndDelObject", "success!");
                FileUtil.deleteFile(new File(localPath));
                uploadImageIndex++;
                handleActionImage(OSSManager.ACTION_REMOVE);
            }

            @Override
            public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }

                FileUtil.deleteFile(new File(localPath));
                uploadImageIndex++;
                handleActionImage(OSSManager.ACTION_REMOVE);
            }

        });
        deleteTask.waitUntilFinished(); // 如果需要等待任务完成
    }

    public NoteModel getNoteModel() {
        return noteModel;
    }

    public void setNoteModel(NoteModel noteModel) {
        this.noteModel = noteModel;
    }
}
