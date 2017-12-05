package com.simplenote.module.photo;

import android.annotation.TargetApi;
import android.os.Bundle;

import com.simplenote.R;
import com.simplenote.application.BaseActivity;

/**
 * Created by melon on 2016/11/1.
 */
@TargetApi(21)
public class Camera2FaceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_face2);
        Camera2Fragment camera2Fragment = Camera2Fragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.fl_contain, camera2Fragment).commit();
    }

}
