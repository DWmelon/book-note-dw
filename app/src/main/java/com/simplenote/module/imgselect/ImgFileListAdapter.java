package com.simplenote.module.imgselect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simplenote.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImgFileListAdapter extends BaseAdapter{

	private Context context;

	private List<HashMap<String, String>> listdata;
	private Util util;
    private Bitmap[] bitmaps;

	public ImgFileListAdapter(Context context,List<HashMap<String, String>> listdata) {
		this.context=context;
		this.listdata=listdata;
		bitmaps=new Bitmap[listdata.size()];
		util=new Util(context);
	}
	
	@Override
	public int getCount() {
		return listdata.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listdata.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup parent) {
		Holder holder;

		if (contentView == null) {
			contentView = LayoutInflater.from(context).inflate(R.layout.adapter_img_file,parent,false);
			holder=new Holder(contentView);
			contentView.setTag(holder);
		}else{
			holder= (Holder)contentView.getTag();
		}

		performView(holder,position);

		return contentView;
	}

	private void performView(Holder holder, final int position){
		HashMap<String,String> map = listdata.get(position);

		holder.mTvName.setText(map.get(ImgFileListActivity.KEY_FILE_NAME));
		holder.mTvCount.setText(map.get(ImgFileListActivity.KEY_FILE_COUNT));


		if (bitmaps[position] == null) {
			util.imgExcute(holder.mIvPhoto,new ImgCallBack() {
				@Override
				public void resultImgCall(ImageView imageView, Bitmap bitmap) {
					bitmaps[position]=bitmap;
					imageView.setImageBitmap(bitmap);
				}
			}, map.get(ImgFileListActivity.KEY_FILE_PATH));
		}
		else {
			holder.mIvPhoto.setImageBitmap(bitmaps[position]);
		}
	}

	class Holder{
		@BindView(R.id.filephoto_imgview)
		public ImageView mIvPhoto;

		@BindView(R.id.filecount_textview)
		public TextView mTvCount;

		@BindView(R.id.filename_textview)
		public TextView mTvName;

		public Holder(View view){
			ButterKnife.bind(this,view);

		}

	}

	
	
}
