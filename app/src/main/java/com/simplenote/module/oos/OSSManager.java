package com.simplenote.module.oos;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.simplenote.constants.Constant;
import com.simplenote.application.GlobalPreferenceManager;
import com.simplenote.application.MyClient;
import com.simplenote.module.add.*;
import com.simplenote.module.oos.callback.OnDeleteImageListener;
import com.simplenote.module.oos.callback.OnDownloadImageListener;
import com.simplenote.module.oos.callback.OnGetUploadConfigListener;
import com.simplenote.module.oos.callback.OnUploadImageListener;
import com.simplenote.network.IRequest;
import com.simplenote.network.IRequestCallback;
import com.simplenote.util.FileUtil;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by melon on 2017/8/4.
 */

public class OSSManager {

    private Context context;

    private OSS oss;
    public static final String endPoint = "http://oss-cn-shenzhen.aliyuncs.com";
    public static final String bucketName = "simple-note";
    private OSSConfigModel ossConfigModel;

    //上传相关
    public static final String UPLOAD_TYPE_LOCAL = "local";
    public static final String UPLOAD_TYPE_SERVICE = "service";



    public static final String URL_UPLOAD_CONFIG = Constant.URL_MAIN + "/data/oss/config";

    public static final String URL_UPLOAD_CHECK = Constant.URL_MAIN+"/note/upload/check";
    public static final String URL_UPLOAD_ADD_NOTE = Constant.URL_MAIN+"/note/add";

    public static final String URL_UPLOAD_GET_NOTE = Constant.URL_MAIN+"/note/get";
    public static final String URL_DOWNLOAD_CHECK = Constant.URL_MAIN+"/note/download/check";

    private OnGetUploadConfigListener listener;

    public static final String ACTION_MODIFY = "note_modify";
    public static final String ACTION_REMOVE = "note_remove";
    public static final String ACTION_ADD = "note_add";


    public void setListener(OnGetUploadConfigListener listener){
        this.listener = listener;
    }

    public OSSManager(Context context){
        this.context = context;
        initOOS();
    }

    private void initOOS(){
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考访问控制章节
        String AccessKeyId = GlobalPreferenceManager.getString(context,GlobalPreferenceManager.KEY_ACCESS_KEY_ID);
        String SecretKeyId = GlobalPreferenceManager.getString(context,GlobalPreferenceManager.KEY_SECRET_KEY_ID);
        String SecurityToken = GlobalPreferenceManager.getString(context,GlobalPreferenceManager.KEY_SECURITY_TOKEN);

        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(AccessKeyId,SecretKeyId, SecurityToken);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        oss = new OSSClient(context, endPoint, credentialProvider, conf);
        if (listener != null){
            listener.onGetConfigFinish(true);
        }
    }

    private void updateOOS(){
        GlobalPreferenceManager.setString(context,GlobalPreferenceManager.KEY_ACCESS_KEY_ID,ossConfigModel.getAccessKeyId());
        GlobalPreferenceManager.setString(context,GlobalPreferenceManager.KEY_SECRET_KEY_ID,ossConfigModel.getAccessKeySecret());
        GlobalPreferenceManager.setString(context,GlobalPreferenceManager.KEY_SECURITY_TOKEN,ossConfigModel.getSecurityToken());
        oss.updateCredentialProvider(new OSSStsTokenCredentialProvider(ossConfigModel.getAccessKeyId(), ossConfigModel.getAccessKeySecret(), ossConfigModel.getSecurityToken()));
    }

    public boolean checkKeyExpire(){
        return ossConfigModel == null || oss == null;
    }

    /**
     * 获取OSS的密钥
     * @param listener
     */
    public void getUploadConfig(String type,final OnGetUploadConfigListener listener){

        if (!checkKeyExpire()){
            listener.onGetConfigFinish(true);
            return;
        }

        HashMap<String,String> map = new HashMap<String, String>();
        map.put("userId",String.valueOf(MyClient.getMyClient().getAccountManager().getUserId()));
        map.put("operate",type);
        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(URL_UPLOAD_CONFIG, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.onGetConfigFinish(false);
                    return;
                }


                ossConfigModel = new OSSConfigModel();
                ossConfigModel.decode(jsonObject);


                if (ossConfigModel.getCode() == 0){
                    updateOOS();
                    listener.onGetConfigFinish(true);
                }else{
                    listener.onGetConfigFinish(false);
                }

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onGetConfigFinish(false);
                }
            }
        });
    }

    public boolean checkImageIsExist(String imageName){
        try {
            if (oss.doesObjectExist(bucketName, imageName)) {
                return true;
            } else {
                return false;
            }
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("RequestId", e.getRequestId());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
        return false;
    }

    public void uploadImageToOSS(String targetPath, String resPath, final OnUploadImageListener listener){

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucketName, targetPath, resPath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                listener.onUploadImageFinish(true,0);
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                } else if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                listener.onUploadImageFinish(false,0);
            }
        });
        // task.cancel(); // 可以取消任务
        task.waitUntilFinished(); // 可以等待直到任务完成

    }

    public void downloadImageFromOSS(String imageName,String ossPath, final OnDownloadImageListener listener){

        if (checkKeyExpire()){
            return;
        }

        final String localPath = MyClient.getMyClient().getStorageManager().getImagePath() + imageName + AddNoteManager.SUPPORT_TYPE;

        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(bucketName, ossPath);
        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                Log.d("Content-Length", "" + result.getContentLength());
                InputStream inputStream = result.getObjectContent();
                if (FileUtil.writeImageToFile(inputStream,localPath)){
                    listener.onDownloadImageFinish(true);
                }else{
                    listener.onDownloadImageFinish(false);
                }

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
                listener.onDownloadImageFinish(false);
            }
        });

        task.waitUntilFinished(); // 如果需要等待任务完成

    }

    public void deleteOssImage(final String localPath, String targetPath, final OnDeleteImageListener listener){

        DeleteObjectRequest delete = new DeleteObjectRequest(OSSManager.bucketName, targetPath);
        // 异步删除
        OSSAsyncTask deleteTask = MyClient.getMyClient().getOSSManager().getOss().asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                Log.d("asyncCopyAndDelObject", "success!");
                if (listener!=null){
                    FileUtil.deleteFile(new File(localPath));
                    listener.onDeleteImageFinish(true);
                }
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

                if (listener!=null){
                    listener.onDeleteImageFinish(false);
                }

            }

        });
        deleteTask.waitUntilFinished(); // 如果需要等待任务完成
    }

    public OSS getOss() {
        return oss;
    }

    public void setOss(OSS oss) {
        this.oss = oss;
    }
}
