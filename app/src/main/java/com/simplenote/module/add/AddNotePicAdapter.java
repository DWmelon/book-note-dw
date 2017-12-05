package com.simplenote.module.add;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.module.pic.OpenPicActivity;
import com.simplenote.module.pic.PicPathModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/7/18.
 */

public class AddNotePicAdapter extends RecyclerView.Adapter<AddNotePicAdapter.ViewHolder> {

    private Context context;
    private List<String> imagePath = new ArrayList<>();
    private boolean isShowDelete = false;

    public AddNotePicAdapter(Context context,List<String> imageList,boolean isShowDelete){
        this.context = context;
        imagePath = imageList;
        this.isShowDelete = isShowDelete;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_add_note_pic,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Uri uri = Uri.fromFile(new File(imagePath.get(position)));
        holder.image.setImageURI(uri);
        holder.image.setTag(imagePath.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PicPathModel model = new PicPathModel();
                model.setImagePathList(imagePath);

                Intent intent = new Intent(context,OpenPicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.BUNDEL.POSITION,position);
                bundle.putSerializable(Constant.BUNDEL.PIC_PATH,model);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.mIvDelete.setVisibility(isShowDelete?View.VISIBLE:View.GONE);
        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0;i < imagePath.size();i++){
                    String path = imagePath.get(i);
                    if (path.equals(holder.image.getTag())){
                        imagePath.remove(i);
                        notifyItemRemoved(i);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagePath.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView image;
        ImageView mIvDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.sdv_add_note);
            mIvDelete = (ImageView) itemView.findViewById(R.id.iv_add_note_delete);
        }
    }

}
