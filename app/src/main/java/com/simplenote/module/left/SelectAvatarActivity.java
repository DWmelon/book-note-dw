package com.simplenote.module.left;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.application.MyClient;
import com.simplenote.module.add.AddNoteManager;
import com.simplenote.module.oos.callback.OnGetUploadConfigListener;
import com.simplenote.module.oos.callback.OnUploadImageListener;
import com.simplenote.module.permission.OnRequestPermissionFinish;
import com.simplenote.util.FileUtil;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/9/26.
 */

public class SelectAvatarActivity extends BaseActivity implements OnRequestPermissionFinish,OnGetUploadConfigListener,OnUploadImageListener {

    @BindView(R.id.tv_avatar_tip)
    TextView mTvTip;

    private String pageType;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);
        ButterKnife.bind(this);

        setFinishOnTouchOutside(true);

        pageType = getIntent().getExtras().getString(Constant.BUNDEL.AVATAR_TYPE);

        initData();

    }

    private void initData(){

        if (pageType.equals(Constant.VALUE.AVATAR_PAGE_TYPE_LOGO)){
            mTvTip.setText(R.string.avatar_type_logo);
        }else{
            mTvTip.setText(R.string.avatar_type_backdrop);
        }

    }

    private void saveImage(){
        MyClient.getMyClient().getOSSManager().getUploadConfig("upload",this);
    }

    @OnClick(R.id.ll_avatar_content)
    void selectPic(){
        checkPermission();
    }

    private void checkPermission(){
        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        MyClient.getMyClient().getPermissionManager().handlePermission(this,permission,this);

    }

    @Override
    public void onPermissionFinish(boolean result) {
        if (!result){
            Toast.makeText(this,"获取读取权限失败，无法获得图片。",Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, Constant.REQUEST.INTENT_SELECT_PIC);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyClient.getMyClient().getPermissionManager().onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST.INTENT_SELECT_PIC){
            if (resultCode == RESULT_OK){
                //获得图片的uri
                Uri originalUri = data.getData();
                //获取图片的路径：
                String[] proj = {MediaStore.Images.Media.DATA};
                //好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                //按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                //将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                //最后根据索引值获取图片路径
                imagePath = cursor.getString(column_index);

                saveImage();
            }else{

            }
        }
    }

    @Override
    public void onUploadImageFinish(final boolean isSuccess, int index) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSuccess){
                    String path = MyClient.getMyClient().getStorageManager().getImageLogoPath(pageType);
                    FileUtil.copyFile(new File(imagePath), path);
                    Intent intent = new Intent();
                    intent.putExtra(Constant.BUNDEL.PIC_RES,imagePath);
                    intent.putExtra(Constant.BUNDEL.AVATAR_TYPE,pageType);
                    setResult(RESULT_OK,intent);
                }else{
                    Toast.makeText(SelectAvatarActivity.this,"保存图片失败",Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }

    @Override
    public void onGetConfigFinish(boolean isSuccess) {
        if (isSuccess){
            String targetPath = "image/"+MyClient.getMyClient().getAccountManager().getUserId() + "/"+ pageType + AddNoteManager.SUPPORT_TYPE;
            MyClient.getMyClient().getOSSManager().uploadImageToOSS(targetPath,imagePath,this);
        }else{
            Toast.makeText(this,"保存图片失败",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
