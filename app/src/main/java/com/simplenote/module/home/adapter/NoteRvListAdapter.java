package com.simplenote.module.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.simplenote.constants.Constant;
import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.application.MyClient;
import com.simplenote.module.add.AddNoteManager;
import com.simplenote.module.detail.NoteDetailActivity;
import com.simplenote.util.RecycleViewUtil;
import com.simplenote.model.NoteModel;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by melon on 2017/1/3.
 */

public class NoteRvListAdapter extends RecyclerView.Adapter<NoteRvListAdapter.ViewHolder>{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context mContext;

    private List<Object> noteModels;
    private HashSet<Integer> headerIndexSet;

    public NoteRvListAdapter(Context context){
        this.mContext = context;
        initData();
    }

    public void initData(){
        this.noteModels = RecycleViewUtil.getNoteList();
        this.headerIndexSet = RecycleViewUtil.getNoteChildHeaderIndex();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_HEADER){
            v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.layout_home_list_header, parent, false);
        }else{
            v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.layout_note_list_item, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER){
            Date date = (Date) noteModels.get(position);
            holder.mTvHeader.setText(RecycleViewUtil.getHeaderDataStr(mContext,date));
        }else if (getItemViewType(position) == TYPE_ITEM){
            handleData(holder,(NoteModel) noteModels.get(position));
        }
    }

    private void handleData(ViewHolder holder, final NoteModel model){
        //图片
        if (model.getImageNameList().isEmpty()){
            holder.mImageContent.setVisibility(View.GONE);
        }else{
            holder.mImageContent.setVisibility(View.VISIBLE);
            String uriStr = "file://"+ MyClient.getMyClient().getAddNoteManager().getImageLocalPath(model.getImageNameList().get(0));
            Uri uri = Uri.parse(uriStr);
            holder.mSdvImg.setImageURI(uri);
        }

        //标题和内容
        holder.mTvTitle.setText(model.getTitle());
        holder.mTvContent.setText(model.getContent());

        //心情天气主题
        holder.mLlFlagContent.removeAllViews();
        boolean flag = false;
        int imageSize = mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);
        int emotionImgRes = MyClient.getMyClient().getAddNoteManager().getImageResES(AddNoteManager.TYPE_EMOTION,model);
        if (emotionImgRes > 0){
            flag = true;
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
            params.setMargins(0,0,mContext.getResources().getDimensionPixelOffset(R.dimen.margin_10),0);
            iv.setImageResource(emotionImgRes);
            holder.mLlFlagContent.addView(iv,params);
        }

        int weatherImgRes = MyClient.getMyClient().getAddNoteManager().getImageResES(AddNoteManager.TYPE_WEATHER,model);
        if (weatherImgRes > 0){
            flag = true;
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
            params.setMargins(0,0,mContext.getResources().getDimensionPixelOffset(R.dimen.margin_10),0);
            iv.setImageResource(weatherImgRes);
            holder.mLlFlagContent.addView(iv,params);
        }

        int themeImgRes = MyClient.getMyClient().getAddNoteManager().getImageResES(AddNoteManager.TYPE_THEME,model);
        if (themeImgRes > 0){
            flag = true;
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
            params.setMargins(0,0,mContext.getResources().getDimensionPixelOffset(R.dimen.margin_10),0);
            iv.setImageResource(themeImgRes);
            holder.mLlFlagContent.addView(iv,params);
        }
        holder.mLlFlagContent.setVisibility(flag?View.VISIBLE:View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BUNDEL.NOTE_ID,model.getNoteId());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((BaseActivity)mContext).showCommonAlert(R.string.dialog_delete_note, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        MyClient.getMyClient().getNoteV1Manager().handleDeleteNote(model);
                        Toast.makeText(mContext,R.string.dialog_delete_success,Toast.LENGTH_LONG).show();

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((BaseActivity)mContext).hideDialog();
                    }
                });
                return true;
            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        return headerIndexSet.contains(position)? TYPE_HEADER :TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return noteModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //type_header
        public TextView mTvHeader;

        //type_item
        public TextView mTvTitle;
        public TextView mTvContent;

        public LinearLayout mLlFlagContent;

        public View mImageContent;
        public SimpleDraweeView mSdvImg;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvHeader = (TextView)itemView.findViewById(R.id.tv_home_list_title);

            mTvTitle = (TextView)itemView.findViewById(R.id.tv_note_list_title);
            mTvContent = (TextView)itemView.findViewById(R.id.tv_note_list_content);

            mLlFlagContent = (LinearLayout)itemView.findViewById(R.id.ll_note_item_emotion);

            mImageContent = itemView.findViewById(R.id.rl_note_list_bg);
            mSdvImg = (SimpleDraweeView)itemView.findViewById(R.id.sdv_note_list);
        }

    }

}