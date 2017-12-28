package com.simplenote.module.oos;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.simplenote.application.MyClient;
import com.simplenote.constants.Constant;
import com.simplenote.database.model.Note;
import com.simplenote.module.add.AddNoteManager;
import com.simplenote.module.oos.callback.OnCheckIsSyncListener;
import com.simplenote.module.oos.callback.OnDeleteImageListener;
import com.simplenote.module.oos.callback.OnUploadFinishListener;
import com.simplenote.module.oos.callback.OnUploadNoteListener;
import com.simplenote.module.oos.upload.OSSCheckSyncModel;
import com.simplenote.module.oos.upload.UploadIdACodeModel;
import com.simplenote.network.IRequest;
import com.simplenote.network.IRequestCallback;
import com.simplenote.util.V2ArrayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by melon on 2017/10/20.
 */

public class OSSUploadManager implements OnDeleteImageListener{

    private OnUploadNoteListener noteListener;
    private OnUploadFinishListener uploadFinishListener;

    private int uploadNoteIndex;
    private int uploadImageIndex;
    private List<String> idList;
    private Note noteModel;
    private List<String> failImageList;

    private List<String> deleteImageList = new ArrayList<>();

    /**
     * 检查还未上传的日记id列表
     * @param listener
     */
    public void checkIsUpload(List<UploadIdACodeModel> idList, final OnCheckIsSyncListener listener){

        HashMap<String,String> map = new HashMap<>();
        map.put("idList", V2ArrayUtil.getJsonArrData(idList));
        map.put(Constant.PARAM.TOKEN, MyClient.getMyClient().getAccountManager().getToken());

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(OSSManager.URL_UPLOAD_CHECK, map, new IRequestCallback() {
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
     * 上传日记信息
     * @param idList
     * @param listener
     */
    public void  handleUploadNoteInfo(List<String> idList, final OnUploadNoteListener listener,OnUploadFinishListener listener2){
        uploadNoteIndex = 0;
        uploadImageIndex = -1;
        failImageList = new ArrayList<>();
        deleteImageList = new ArrayList<>();
        this.idList = idList;
        this.noteListener = listener;
        this.uploadFinishListener = listener2;
        culNoteModel();
        //先获取日记，根据服务器端返回的错误吗进行相应处理
        //先上传照片再上传日记，避免日记上传成功，但是照片失败导致日记不完整。
        handleNoteModel();
    }

    private void culNoteModel(){
        noteModel = null;

        String id = idList.get(uploadNoteIndex);
        noteModel = MyClient.getMyClient().getNoteV1Manager().getNoteModels(id);
        noteModel.setUserId(MyClient.getMyClient().getAccountManager().getUserId());

        if (noteModel == null){
            noteListener.onUploadNoteFinish(false,uploadNoteIndex,0);
        }

    }

    public void continueUpload(){
        uploadNoteIndex ++;
        if (uploadNoteIndex >= idList.size()){
            uploadFinishListener.onUploadFinish(true,"success");

        }else{
            culNoteModel();
            uploadImageIndex = -1;
            handleNoteModel();

        }

    }

    private void handleNoteModel(){
        //删除操作
        if (noteModel.getStatus() == -1){
            deleteImageList.addAll(noteModel.getImageList());
            uploadNoteInfo();
            return;
        }

        downNoteInfo();
    }

    private void downNoteInfo(){

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
                    uploadFinishListener.onUploadFinish(false,"server error");
                    return;
                }



                Note model = new Note();
                model.decode(jsonObject);
                if (model.getCode() == -1){
                    uploadImage();
                } else if (model.getCode() == 0){
                    uploadImage();

                    //获取服务器中该日记的旧照片，看是否需要删除
                    Set<String> imageSet = new HashSet<>(noteModel.getImageList());
                    for (String imageName : model.getImageList()){
                        if (!imageSet.contains(imageName)){
                            deleteImageList.add(imageName);
                        }
                    }

                } else{
                    noteListener.onUploadNoteFinish(false,uploadNoteIndex,0);
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

    private void uploadNoteInfo(){

        HashMap<String,String> map = new HashMap<>();

        map.put("noteId",noteModel.getNoteId());
        map.put("noteCode",noteModel.getNoteCode());
        map.put("title",noteModel.getTitle());
        map.put("content",noteModel.getContent());
        map.put("createDate",String.valueOf(noteModel.getCreateDate()));
        map.put("modifyDate",String.valueOf(noteModel.getModifyDate()));
        map.put("emotion",noteModel.getEmotion());
        map.put("weather",noteModel.getWeather());
        map.put("theme",noteModel.getTheme());
        map.put("imagePath",noteModel.getImageNameList());
        map.put("userId",String.valueOf(noteModel.getUserId()));
        map.put("status",String.valueOf(noteModel.getStatus()));
        map.put(Constant.PARAM.TOKEN,MyClient.getMyClient().getAccountManager().getToken());

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(OSSManager.URL_UPLOAD_ADD_NOTE, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (noteListener == null) {
                    return;
                }

                if (jsonObject == null) {
                    uploadFinishListener.onUploadFinish(false,"server error");
                    return;
                }

                int code = jsonObject.getIntValue("code");
                if (code == 0){
                    noteListener.onUploadNoteFinish(true,uploadNoteIndex,noteModel.getStatus());
                }else{
                    noteListener.onUploadNoteFinish(false,uploadNoteIndex,noteModel.getStatus());
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

    private void uploadImage(){

        uploadImageIndex ++;
        if (uploadImageIndex >= noteModel.getImageList().size()){
            uploadNoteInfo();
            return;
        }

        if (MyClient.getMyClient().getOSSManager().checkKeyExpire()){
            uploadFinishListener.onUploadFinish(false,"oss_expire");
            return;
        }



        String imageName = noteModel.getImageList().get(uploadImageIndex);
        String targetPath = "image/"+MyClient.getMyClient().getAccountManager().getUserId()+"/"+imageName+ AddNoteManager.SUPPORT_TYPE;

        //覆盖，不判断重复
//        if (MyClient.getMyClient().getOSSManager().checkImageIsExist(targetPath)){
//            uploadImage();
//            return;
//        }

        String path = MyClient.getMyClient().getStorageManager().getImagePath()+imageName+AddNoteManager.SUPPORT_TYPE;

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(OSSManager.bucketName, targetPath, path);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = MyClient.getMyClient().getOSSManager().getOss().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                uploadImage();
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {


                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();

                    uploadFinishListener.onUploadFinish(false,"上传失败，请联系客服。：1400");
                    return;
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    uploadFinishListener.onUploadFinish(false,"上传失败，请联系客服。：1400");
                    return;
                }

                failImageList.add(uploadNoteIndex+"-"+uploadImageIndex);
                uploadImage();
            }
        });
        // task.cancel(); // 可以取消任务
        task.waitUntilFinished(); // 可以等待直到任务完成

    }

    public String getIdByIndex(int index){
        return idList.get(index);
    }

    int deleteImageIndex;

    public void deleteExtraImage(){
        if (deleteImageList == null || deleteImageList.isEmpty()){
            return;
        }
        deleteImageIndex = 0;
        String imageName = deleteImageList.get(deleteImageIndex);
        String localPath = MyClient.getMyClient().getStorageManager().getImagePath() + imageName + AddNoteManager.SUPPORT_TYPE;
        String targetPath = "image/"+MyClient.getMyClient().getAccountManager().getUserId()+"/"+imageName+ AddNoteManager.SUPPORT_TYPE;

        MyClient.getMyClient().getOSSManager().deleteOssImage(localPath,targetPath,this);

    }


    @Override
    public void onDeleteImageFinish(boolean isSuccess) {
        deleteImageIndex ++ ;
        if (deleteImageList.size() > deleteImageIndex){
            String imageName = deleteImageList.get(deleteImageIndex);
            String localPath = MyClient.getMyClient().getStorageManager().getImagePath() + imageName + AddNoteManager.SUPPORT_TYPE;
            String targetPath = "image/"+MyClient.getMyClient().getAccountManager().getUserId()+"/"+imageName+ AddNoteManager.SUPPORT_TYPE;

            MyClient.getMyClient().getOSSManager().deleteOssImage(localPath,targetPath,this);
        }
    }
}
