package com.simplenote.module.imgselect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.simplenote.R;
import com.simplenote.application.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImgFileListActivity extends BaseActivity implements OnItemClickListener{

	public static String KEY_FILE_COUNT = "filecount";
	public static String KEY_FILE_PATH = "imgpath";
	public static String KEY_FILE_NAME = "filename";

	ListView listView;
	ImgFileListAdapter listAdapter;

	Util util;
	List<FileTraversal> locallist;

	@BindView(R.id.tv_tool_bar_title)
	TextView mTvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_img_file_list);
		ButterKnife.bind(this);
		initView();

		listView=(ListView) findViewById(R.id.listView1);
		util=new Util(this);
		locallist=util.LocalImgFileList();
		List<HashMap<String, String>> listdata=new ArrayList<HashMap<String,String>>();
		Bitmap bitmap[] = null;
		if (locallist!=null) {
			bitmap=new Bitmap[locallist.size()];
			for (int i = 0; i < locallist.size(); i++) {
				HashMap<String, String> map=new HashMap<String, String>();
				map.put(KEY_FILE_COUNT, getString(R.string.image_select_per,locallist.get(i).filecontent.size()));
				map.put(KEY_FILE_PATH, locallist.get(i).filecontent.get(0)==null?null:(locallist.get(i).filecontent.get(0)));
				map.put(KEY_FILE_NAME, locallist.get(i).filename);
				listdata.add(map);
			}
		}
		listAdapter=new ImgFileListAdapter(this, listdata);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);
		
	}

	private void initView(){
		mTvTitle.setText("选");
	}

	@OnClick(R.id.iv_bar_left_icon)
	void back(){
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent=new Intent(this,ImgsActivity.class);
		Bundle bundle=new Bundle();
		bundle.putParcelable("data", locallist.get(arg2));
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
}
