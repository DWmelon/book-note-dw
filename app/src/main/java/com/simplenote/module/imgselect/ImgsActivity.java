package com.simplenote.module.imgselect;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simplenote.R;
import com.simplenote.application.MyClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImgsActivity extends Activity {

	Bundle bundle;
	FileTraversal fileTraversal;

	@BindView(R.id.tv_select_image_confirm)
	TextView mTvSelectCount;

	@BindView(R.id.gridView1)
	GridView imgGridView;
	ImgsAdapter imgsAdapter;

	@BindView(R.id.selected_image_layout)
	LinearLayout select_layout;

	@BindView(R.id.relativeLayout2)
	RelativeLayout relativeLayout2;

	Util util;

	HashMap<Integer, ImageView> hashImage;

	ArrayList<String> filelist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grally_photo);
		ButterKnife.bind(this);

		initBundle();
		initData();
	}

	private void initBundle(){
		bundle= getIntent().getExtras();
		fileTraversal=bundle.getParcelable("data");
	}

	private void initData(){
		hashImage=new HashMap<>();
		filelist=new ArrayList<>();

		imgsAdapter=new ImgsAdapter(this, fileTraversal.filecontent,onItemClickClass);
		imgGridView.setAdapter(imgsAdapter);

		util=new Util(this);
	}
	
	@SuppressLint("NewApi")
	public ImageView iconImage(String filepath) throws FileNotFoundException{
		int height = getResources().getDimensionPixelOffset(R.dimen.select_image_bottom_height);
		LayoutParams params=new LayoutParams(height, height);
		ImageView imageView=new ImageView(this);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(R.drawable.icon_select_img);
		util.imgExcute(imageView, imgCallBack, filepath);

		return imageView;
	}
	
	private ImgCallBack imgCallBack=new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};
	
	ImgsAdapter.OnItemClickClass onItemClickClass=new ImgsAdapter.OnItemClickClass() {
		@Override
		public void OnItemClick(int Position, CheckBox checkBox) {
			String filePath = fileTraversal.filecontent.get(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				select_layout.removeView(hashImage.get(Position));
				filelist.remove(filePath);
				setCountTip(select_layout.getChildCount());
			}else {
				if (!judgeSelectCount()){
					Toast.makeText(ImgsActivity.this,R.string.over_image_count_limit,Toast.LENGTH_SHORT).show();
					return;
				}

				try {
					ImageView imageView=iconImage(filePath);
					if (imageView !=null) {
						hashImage.put(Position, imageView);
						filelist.add(filePath);
						select_layout.addView(imageView);
						setCountTip(select_layout.getChildCount());
					}
					checkBox.setChecked(true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}

		}
	};

	private void setCountTip(int count){
		if (count == 0){
			mTvSelectCount.setText(R.string.image_select_count);
		}else{
			int maxCount = MyClient.getMyClient().getAccountManager().getMaxImageCount();
			int nowCount = MyClient.getMyClient().getAddNoteManager().getNoteImagePaths().size();
			mTvSelectCount.setText(getString(R.string.image_select_count1,count,maxCount-nowCount));
		}
	}

	private boolean judgeSelectCount(){
		int count = MyClient.getMyClient().getAccountManager().getMaxImageCount();
		int nowCount = MyClient.getMyClient().getAddNoteManager().getNoteImagePaths().size();
		return count - nowCount > select_layout.getChildCount();
	}

	@OnClick(R.id.tv_select_image_back)
	public void turnBack(){
		Intent intent = new Intent(this,ImgFileListActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		turnBack();
	}

	@OnClick(R.id.tv_select_image_confirm)
	public void sendFiles(View view){
		List<String> imagePaths = MyClient.getMyClient().getAddNoteManager().getNoteImagePaths();
		imagePaths.addAll(filelist);
		finish();
	}
}
