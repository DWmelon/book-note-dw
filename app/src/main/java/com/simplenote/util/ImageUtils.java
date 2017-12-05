package com.simplenote.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.MainApplication;
import com.simplenote.application.MyClient;
import com.simplenote.module.add.AddNoteManager;
import com.simplenote.module.home.OnSetupImageFinishListener;
import com.simplenote.module.oos.callback.OnDownloadImageListener;
import com.simplenote.module.oos.callback.OnGetUploadConfigListener;

import java.io.File;
import java.util.List;

/**
 * Created by melon on 2017/7/18.
 */

public class ImageUtils {

    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param imageName
     * @return
     */
    public static int getResource(String imageName, Context context) {
        int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        return resId;
    }


    /**
     * 将view转成bitmap
     */
    private Bitmap loadBitmapFromView(Context context,View view) {
        if (view == null) {
            return null;
        }

//        if(pageType == Constant.VALUE_PIC_CHOOSE_TYPE_OFFICIAL){

//            view.measure(View.MeasureSpec.makeMeasureSpec(getResources().getDimensionPixelSize(R.dimen.photo_type_width),View.MeasureSpec.EXACTLY),
//                    View.MeasureSpec.makeMeasureSpec(getResources().getDimensionPixelSize(R.dimen.photo_type_height), View.MeasureSpec.EXACTLY));
//            // 这个方法也非常重要，设置布局的尺寸和位置
//            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//            view.measure(View.MeasureSpec.makeMeasureSpec(screenWidth,View.MeasureSpec.EXACTLY),
//                    View.MeasureSpec.makeMeasureSpec(screenHeight - getResources().getDimensionPixelSize(R.dimen.camera_bottom_content_height), View.MeasureSpec.EXACTLY));
            // 这个方法也非常重要，设置布局的尺寸和位置
//            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        }else{

        List<Integer> size = CommonUtil.getScreenSize(context);
            view.measure(View.MeasureSpec.makeMeasureSpec(size.get(0),View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(size.get(1) - context.getResources().getDimensionPixelSize(R.dimen.camera_bottom_content_height), View.MeasureSpec.EXACTLY));
            // 这个方法也非常重要，设置布局的尺寸和位置
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        }


        // 生成bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.RGB_565);
        // 利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);
        // 把view中的内容绘制在画布上
        view.draw(canvas);

        return bitmap;
    }

    public static void setupAvatarAndBackDrop(final SimpleDraweeView sdv, final String imageType, final OnSetupImageFinishListener listener){
        //1.看本地是否存在图片，有则使用
        //2.如果本地不存在，则看OSS是否存在图片，有则下载使用
        //3.如果OSS不存在，则用默认图片

        if (!MyClient.getMyClient().getLoginManager().isLogin()){
            setDefaultImage(imageType,listener);
            return;
        }

        final String localPath = MyClient.getMyClient().getStorageManager().getImageLogoPath(imageType);
        if (FileUtil.isExists(localPath)){
            listener.onLoadImageFinish(imageType,Uri.fromFile(new File(localPath)));
            return;
        }

        final String ossPath = "image/"+ MyClient.getMyClient().getAccountManager().getUserId()+"/" + imageType + AddNoteManager.SUPPORT_TYPE;
        MyClient.getMyClient().getOSSManager().getUploadConfig("download", new OnGetUploadConfigListener() {
            @Override
            public void onGetConfigFinish(boolean isSuccess) {
                if (isSuccess && MyClient.getMyClient().getOSSManager().checkImageIsExist(ossPath)){
                        MyClient.getMyClient().getOSSManager().downloadImageFromOSS(imageType,ossPath, new OnDownloadImageListener() {
                            @Override
                            public void onDownloadImageFinish(boolean isSuccess) {
                                Uri uri;
                                if (isSuccess){
                                    uri = Uri.fromFile(new File(localPath));
                                }else{
                                    uri = Uri.parse("res://"+ MainApplication.PACKAGE_NAME + R.drawable.icon_default_backdrop);
                                }
                                listener.onLoadImageFinish(imageType,uri);
                            }
                        });
                }else{
                    setDefaultImage(imageType,listener);
                }
            }
        }
        );



    }

    private static void setDefaultImage(String imageType,OnSetupImageFinishListener listener){
        Uri uri;
        if (imageType.equals(Constant.VALUE.AVATAR_PAGE_TYPE_LOGO)){
            uri = Uri.parse("res://"+ MainApplication.PACKAGE_NAME + "/" + R.drawable.icon_default_avatar);
        }else{
            uri = Uri.parse("res://"+ MainApplication.PACKAGE_NAME + "/" + R.drawable.icon_default_backdrop);
        }
        listener.onLoadImageFinish(imageType,uri);
    }

}