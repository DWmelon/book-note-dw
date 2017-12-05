package com.simplenote.module.paster;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.simplenote.R;
import com.simplenote.application.MyClient;
import com.simplenote.constants.Constant;
import com.simplenote.module.pic.OpenPicActivity;
import com.simplenote.module.pic.PicPathModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/11/1.
 */

public class PasterAdapter extends RecyclerView.Adapter<PasterAdapter.ViewHolder> {

    private Context mContext;

    public PasterAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_paster_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PasterModel pasterModel = MyClient.getMyClient().getPasterManager().getPasterModelList().get(position);

        float ratio = ((float)pasterModel.getPasterWidth())/pasterModel.getPasterHeight();
        holder.mSdvView.setAspectRatio(ratio);

        Uri uri = Uri.parse(pasterModel.getPasterUrl());
        holder.mSdvView.setImageURI(uri);

        holder.mIvFavorIcon.setSelected(pasterModel.isFavor());
        holder.mTvFavorCount.setText(String.valueOf(pasterModel.getPasterFavor()));

        holder.mIvFavorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.mIvFavorIcon.isSelected()){
                    MyClient.getMyClient().getPasterManager().favorPaster(pasterModel.getPasterId(),null);
                    holder.mIvFavorIcon.setSelected(true);
                    int count = Integer.parseInt(holder.mTvFavorCount.getText().toString());
                    holder.mTvFavorCount.setText(String.valueOf(++count));
                }
            }
        });

        holder.mSdvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> pathList = new ArrayList<>();
                pathList.add(pasterModel.getPasterUrl());
                PicPathModel model = new PicPathModel();
                model.setImagePathList(pathList);

                Intent intent = new Intent(mContext,OpenPicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.BUNDEL.PIC_PATH_TYPE,Constant.VALUE.PIC_PATH_TYPE_URL);
                bundle.putSerializable(Constant.BUNDEL.PIC_PATH,model);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return MyClient.getMyClient().getPasterManager().getPasterModelList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView mSdvView;
        ImageView mIvFavorIcon;
        TextView mTvFavorCount;

        ViewHolder(View itemView) {
            super(itemView);
            mSdvView = (SimpleDraweeView)itemView.findViewById(R.id.sdv_paster_view);
            mIvFavorIcon = (ImageView)itemView.findViewById(R.id.iv_paster_icon);
            mTvFavorCount = (TextView)itemView.findViewById(R.id.tv_paster_count);
        }
    }

}
