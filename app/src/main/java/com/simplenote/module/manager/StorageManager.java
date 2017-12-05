package com.simplenote.module.manager;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.simplenote.constants.Constant;
import com.simplenote.application.MyClient;
import com.simplenote.module.add.AddNoteManager;

import java.io.File;

/**
 * Created by eddy on 2015/4/20.
 */
public class StorageManager {

    //    public static final String DIR_ROOT_DIR_NAME = "iPIN";
    public static final String DIR_APP_ROOT_NAME = "weiji";

    public static final String DIR_IMAGE = "imgfile";
    public static final String DIR_NOTE = "noteinfo";
    public static final String DIR_TAKE_PHOTO = "takephoto";

    private String PATH_REAL_ROOT;
    private String PATH_REAL_IMAGE;
    private String PATH_REAL_NOTE;
    private String PATH_TAKE_PHOTO;
    private String PATH_IMAGE_AVATAR;
    private String PATH_IMAGE_BACKDROP;

//    public static final String DIR_IMAGE_CACHE_NAME = "imgCache";
//    public static final String DIR_STRING_CACHE_NAME = "strCache";
//    public static final String DIR_FAV_CACHE_NAME = "favCache";
//    public static final String DIR_LOG_CACHE_NAME = "log";
//    private static final String PATCH_NAME = "iPIN.apatch";

    public static final String FILE_NEW_VERSION_NAME = "wanmeizhiyuan_";
    public static final String SUFFIX_NEW_VERSION = ".apk";

//    public static final String PATH_NEW_VERSION = new StringBuilder(DIR_APP_ROOT_NAME).append(File.separator)
//            .append(DIR_NEW_VERSION_NAME).append(File.separator).toString();
//    public static final String PATH_IMG_CACHE = new StringBuilder(DIR_APP_ROOT_NAME).append(File.separator).
//            append(DIR_IMAGE_CACHE_NAME).append(File.separator).toString();
//    public static final String PATH_STRING_CACHE = new StringBuilder(DIR_APP_ROOT_NAME).append(File.separator).
//            append(DIR_STRING_CACHE_NAME).append(File.separator).toString();
//    public static final String PATH_FAV_CACHE = new StringBuilder(DIR_APP_ROOT_NAME).append(File.separator).
//            append(DIR_FAV_CACHE_NAME).append(File.separator).toString();
//    public static final String PATH_LOG_CACHE = new StringBuilder(DIR_APP_ROOT_NAME).append(File.separator).
//            append(DIR_LOG_CACHE_NAME).append(File.separator).toString();


    private static String mExternalRootDirPath;
    private static String mRootDirPath;
    private static String mPrivateCacheDir;
    private static String mFilesDir;
    private static String mLogFilePath;

    private static StorageManager mInstance;
    private Context mContext;

    public StorageManager() {

    }

    public void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("the param context is null,please check again");
        }

        mContext = context.getApplicationContext();
        mPrivateCacheDir = mContext.getCacheDir().getAbsolutePath() + File.separator;
        mFilesDir = mContext.getFilesDir() + File.separator;

        if (isExternalStorageAvailable()) {
            File sdRootFile = Environment.getExternalStorageDirectory();
            if (sdRootFile != null && sdRootFile.exists()) {
                mExternalRootDirPath = sdRootFile.getAbsolutePath() + File.separator;
            }

            if (mContext.getExternalCacheDir() != null) {
                mRootDirPath = mContext.getExternalCacheDir().getPath() + File.separator;
            } else {
                if (!TextUtils.isEmpty(mExternalRootDirPath)) {
                    mRootDirPath = mExternalRootDirPath;
                } else {
                    mRootDirPath = mPrivateCacheDir;
                }
            }

        } else {
            mRootDirPath = mPrivateCacheDir;//mContext.getCacheDir().getAbsolutePath();
        }

//        File data = Environment.getDataDirectory();


    }

    public void initAllDir() {

        //跟目录
//        File rootDir = new File(new StringBuilder(mRootDirPath).append(File.separator).append(DIR_ROOT_DIR_NAME).append(File.separator).toString());
//        if(!rootDir.exists()){
//         rootDir.mkdirs();
//        }

        //app目录
        String appDirStr;
        File externalAppDir;

        File externalAppImage;
        File externalAppNote;
        File externalAppTakePhoto;
        String userImageUrl =  MyClient.getMyClient().getAccountManager().getUserId() + File.separator + DIR_IMAGE;
        String userNoteUrl =  MyClient.getMyClient().getAccountManager().getUserId() + File.separator + DIR_NOTE;
        String userTakePhotoUrl =  MyClient.getMyClient().getAccountManager().getUserId() + File.separator + DIR_TAKE_PHOTO;

        if (!TextUtils.isEmpty(mExternalRootDirPath)) {
            //sd卡可用
            appDirStr = new StringBuilder(mExternalRootDirPath).append(DIR_APP_ROOT_NAME).append(File.separator).toString();

        } else {

            appDirStr = new StringBuilder(mRootDirPath).append(DIR_APP_ROOT_NAME).append(File.separator).toString();

        }

        externalAppDir = new File(appDirStr);
        if (!externalAppDir.exists()) {
            externalAppDir.mkdirs();
        }

        externalAppImage = new File(externalAppDir,userImageUrl);
        if (!externalAppImage.exists()) {
            externalAppImage.mkdirs();
        }


        externalAppNote = new File(externalAppDir,userNoteUrl);
        if (!externalAppNote.exists()) {
            externalAppNote.mkdirs();
        }

        externalAppTakePhoto = new File(externalAppDir,userTakePhotoUrl);
        if (!externalAppTakePhoto.exists()) {
            externalAppTakePhoto.mkdirs();
        }

        PATH_REAL_ROOT = appDirStr;
        PATH_REAL_IMAGE = externalAppImage.getPath() + File.separator;
        PATH_REAL_NOTE = externalAppNote.getPath() + File.separator;
        PATH_TAKE_PHOTO = externalAppTakePhoto.getPath() + File.separator;

        PATH_IMAGE_AVATAR = PATH_REAL_IMAGE + Constant.VALUE.AVATAR_PAGE_TYPE_LOGO + AddNoteManager.SUPPORT_TYPE;
        PATH_IMAGE_BACKDROP = PATH_REAL_IMAGE + Constant.VALUE.AVATAR_PAGE_TYPE_BACKDROP + AddNoteManager.SUPPORT_TYPE;


    }

    public void release() {

    }

    public String getRootDirPath() {
        return mRootDirPath;
    }

    public static boolean isExternalStorageAvailable() {
        if (!Environment.isExternalStorageRemovable()) {
            return true;
        } else {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return true;
            } else {
                return false;
            }
        }
    }


    public static String getPackageCacheRoot() {
        return mPrivateCacheDir;
    }

    public String getPackageFiles() {
        return mFilesDir;
    }

//    public static String getImgCachePath() {
//        return getImgCacheDirPath();
//    }

    public String getImagePath() {
        return PATH_REAL_IMAGE;
    }

    public String getNotePath() {
        return PATH_REAL_NOTE;
    }

    public String getTakePhotoPath() {
        return PATH_TAKE_PHOTO;
    }

    public String getImageLogoPath(String imageType){
        return imageType.equals(Constant.VALUE.AVATAR_PAGE_TYPE_LOGO)?getImageAvatarPath():getImageBackDropPath();
    }

    public String getImageAvatarPath(){
        return PATH_IMAGE_AVATAR;
    }

    public String getImageBackDropPath(){
        return PATH_IMAGE_BACKDROP;
    }

//    private static String getImgCacheDirPath() {
//        return new StringBuilder(mRootDirPath).append(PATH_IMG_CACHE).toString();
//    }



}
