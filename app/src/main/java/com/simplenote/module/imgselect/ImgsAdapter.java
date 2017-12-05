package com.simplenote.module.imgselect;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.module.pic.OpenPicActivity;
import com.simplenote.module.pic.PicPathModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImgsAdapter extends BaseAdapter {

	private Context context;
	private List<String> data;
	private  Bitmap bitmaps[];
	private Util util;
	private OnItemClickClass onItemClickClass;

	public ImgsAdapter(Context context,List<String> data,OnItemClickClass onItemClickClass) {
		this.context=context;
		this.data=data;
		this.onItemClickClass=onItemClickClass;
		bitmaps=new Bitmap[data.size()];
		util=new Util(context);
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		Holder holder;
		if (contentView == null) {
			contentView = LayoutInflater.from(context).inflate(R.layout.adapter_item_imgs,parent, false);
			holder=new Holder(contentView);
			contentView.setTag(holder);
		}else {
			holder= (Holder)contentView.getTag();
		}

		performView(holder,position);

		return contentView;
	}

	private void performView(Holder holder, final int position){
		if (bitmaps[position] == null) {
			util.imgExcute(holder.imageView,new ImgCallBackListener(position), data.get(position));
		}
		else {
			holder.imageView.setImageBitmap(bitmaps[position]);
		}

		holder.checkBox.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if (motionEvent.getAction() == MotionEvent.ACTION_UP){
					if (onItemClickClass != null){
						onItemClickClass.OnItemClick(position,(CheckBox) view);
					}
				}
				return true;
			}
		});

		holder.imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				PicPathModel model = new PicPathModel();
				model.getImagePathList().add(data.get(position));

				Intent intent = new Intent(context,OpenPicActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constant.BUNDEL.PIC_PATH,model);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});

	}
	
	class Holder{
		@BindView(R.id.imageView1)
		ImageView imageView;

		@BindView(R.id.checkBox1)
		CheckBox checkBox;

		public Holder(View view){
			ButterKnife.bind(this,view);
		}

	}

	public class ImgCallBackListener implements ImgCallBack{

		int num;

		public ImgCallBackListener(int num) {
			this.num=num;
		}
		
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			bitmaps[num]=bitmap;
			imageView.setImageBitmap(bitmap);
		}
	}

	public interface OnItemClickClass{
		void OnItemClick(int Position, CheckBox checkBox);
	}
	
}
