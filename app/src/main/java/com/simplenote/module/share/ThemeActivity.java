package com.simplenote.module.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.simplenote.application.MyClient;
import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.database.model.Note;
import com.simplenote.module.add.AddNoteManager;
import com.simplenote.module.home.OnSetupImageFinishListener;
import com.simplenote.util.CommonUtil;
import com.simplenote.util.ImageUtils;
import com.simplenote.widgets.DragTextView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/8/22.
 */

public class ThemeActivity extends BaseActivity {

    @BindView(R.id.sdv)
    SimpleDraweeView mSdvView;

    @BindView(R.id.tv_title)
    DragTextView mTvTitle;

    @BindView(R.id.tv_content)
    DragTextView mTvContent;

    @BindView(R.id.iv_theme_photo)
    ImageView mIvPhoto;

    private Note noteModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        ButterKnife.bind(this);
        noteModel = (Note) getIntent().getSerializableExtra(Constant.KEY.NOTE_MODEL);
        mTvTitle.setText(noteModel.getTitle());
        mTvContent.setText(noteModel.getContent());

        if (!noteModel.getImageNameList().isEmpty()){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    setupImage();
                }
            });
        }

        initData();



    }

    private void setupImage(){
        String path = MyClient.getMyClient().getStorageManager().getImagePath() + noteModel.getImageList().get(0) + AddNoteManager.SUPPORT_TYPE;
        float ratio = getImageRatio(path);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvPhoto.getLayoutParams();
        params.width = CommonUtil.getScreenSize(this).get(0);
        params.height = (int) (params.width*ratio);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTvTitle.setUISize(params.width,params.height);
        mTvContent.setUISize(params.width,params.height);
        mIvPhoto.setLayoutParams(params);
        Uri uri = Uri.fromFile(new File(path));
        mIvPhoto.setImageURI(uri);
        mIvPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    private float getImageRatio(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return  ((float) options.outHeight)/options.outWidth;

    }

    private void initData(){
        if (!MyClient.getMyClient().getLoginManager().isLogin()){
            return;
        }

        ImageUtils.setupAvatarAndBackDrop(mSdvView, Constant.VALUE.AVATAR_PAGE_TYPE_LOGO, new OnSetupImageFinishListener() {
            @Override
            public void onLoadImageFinish(String imageType, Uri uri) {
                mSdvView.setImageURI(uri);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.sdv)
    void share(){
        shareText()
                .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                .setCallback(new UmShareCallback())
                .open();
    }

    private class UmShareCallback implements UMShareListener{

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    }

    private ShareAction shareText(){
        return new ShareAction(this).withText("hello");
    }

    private ShareAction shareImage(){
        UMImage image = new UMImage(this, "imageurl");//网络图片
//        UMImage image = new UMImage(this, R.drawable.xxx);//资源文件
//        UMImage image = new UMImage(this, bitmap);//bitmap文件
//        UMImage image = new UMImage(this, byte[]);//字节流
//        UMImage image = new UMImage(this, file);//本地文件

        //缩略图
//        UMImage thumb =  new UMImage(this, R.drawable.thumb);
//        image.setThumb(thumb);

//        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享压缩格式设置
//        image.compressFormat = Bitmap.CompressFormat.PNG;//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
        return new ShareAction(this).withMedia(image);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
