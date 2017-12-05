package com.simplenote.application;

import android.content.Context;
import android.text.TextUtils;

import com.simplenote.module.account.AccountManager;
import com.simplenote.module.add.AddNoteManager;
import com.simplenote.module.left.SelectManager;
import com.simplenote.module.login.LoginManager;
import com.simplenote.module.manager.NoteV1Manager;
import com.simplenote.module.manager.StorageManager;
import com.simplenote.module.oos.OSSDownloadManager;
import com.simplenote.module.oos.OSSManager;
import com.simplenote.module.oos.OSSUploadManager;
import com.simplenote.module.paster.PasterListResult;
import com.simplenote.module.paster.PasterManager;
import com.simplenote.module.permission.PermissionManager;
import com.simplenote.network.HttpRequestManager;
import com.simplenote.network.IInterface;
import com.simplenote.network.IRequest;

import java.util.HashMap;

/**
 * Created by melon on 2017/1/3.
 */

public class MyClient {

    private static MyClient myClient;

    private Context context;

    public static final String SERVICE_HTTP_REQUEST = "httpRequest";

    private HashMap<String, IInterface> mService = new HashMap<String, IInterface>();


    public static synchronized MyClient getMyClient(){
        if (myClient == null){
            myClient = new MyClient();
        }
        return myClient;
    }

    public void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("IpinClient#init,the param context is null,please check again");
        }
        this.context = context;
        //初始化基础路径
        getStorageManager().init(context);
        //初始化用户信息以及用户路径
        getAccountManager().init();
        //初始化日记常量
        getAddNoteManager().init(context);

        initModule();
    }

    private void initModule() {
        IRequest request = new HttpRequestManager(context);
        mService.put(MyClient.SERVICE_HTTP_REQUEST, request);
    }

    public IInterface getService(String serviceName) {

        if (TextUtils.isEmpty(serviceName)) {
            return null;
        }

        return mService.get(serviceName);

    }

    private NoteV1Manager noteV1Manager;
    private StorageManager storageManager;
    private AddNoteManager addNoteManager;
    private SelectManager selectManager;
    private LoginManager loginManager;
    private AccountManager accountManager;
    private PermissionManager permissionManager;
    private OSSManager ossManager;
    private OSSUploadManager ossUploadManager;
    private OSSDownloadManager ossDownloadManager;
    private PasterManager pasterManager;


    public  synchronized OSSManager getOSSManager(){
        if (ossManager == null){
            ossManager = new OSSManager(context);
        }
        return ossManager;
    }

    public  synchronized OSSUploadManager getOssUploadManager(){
        if (ossUploadManager == null){
            ossUploadManager = new OSSUploadManager();
        }
        return ossUploadManager;
    }

    public  synchronized OSSDownloadManager getOssDownloadManager(){
        if (ossDownloadManager == null){
            ossDownloadManager = new OSSDownloadManager();
        }
        return ossDownloadManager;
    }

    public   synchronized PermissionManager getPermissionManager(){
        if (permissionManager == null){
            permissionManager = new PermissionManager();
        }
        return permissionManager;
    }

    public   synchronized NoteV1Manager getNoteV1Manager(){
        if (noteV1Manager == null){
            noteV1Manager = new NoteV1Manager();
        }
        return noteV1Manager;
    }

    public   synchronized StorageManager getStorageManager(){
        if (storageManager == null){
            storageManager = new StorageManager();
        }
        return storageManager;
    }

    public   synchronized AddNoteManager getAddNoteManager(){
        if (addNoteManager == null){
            addNoteManager = new AddNoteManager();
        }
        return addNoteManager;
    }


    public   synchronized SelectManager getSelectManager(){
        if (selectManager == null){
            selectManager = new SelectManager();
        }
        return selectManager;
    }

    public   synchronized LoginManager getLoginManager(){
        if (loginManager == null){
            loginManager = new LoginManager(context);
        }
        return loginManager;
    }

    public synchronized AccountManager getAccountManager(){
        if (accountManager == null){
            accountManager = new AccountManager();
        }
        return accountManager;
    }

    public synchronized PasterManager getPasterManager(){
        if (pasterManager == null){
            pasterManager = new PasterManager();
        }
        return pasterManager;
    }

}
