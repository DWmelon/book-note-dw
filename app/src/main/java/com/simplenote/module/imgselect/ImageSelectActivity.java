package com.simplenote.module.imgselect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/7/25.
 */

public class ImageSelectActivity extends BaseActivity {

    ListView listView;
    List<String> listfile=new ArrayList<String>();
    ArrayAdapter<String> arryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        listView=(ListView) findViewById(R.id.listView1);



    }

    public void chise(View v){
        Intent intent = new Intent();
        intent.setClass(this,ImgFileListActivity.class);
        startActivityForResult(intent, Constant.REQUEST.VALUE_INTENT_TO_OWN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST.VALUE_INTENT_TO_OWN){
            arryAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listfile);
            listView.setAdapter(arryAdapter);
        }
    }
}
