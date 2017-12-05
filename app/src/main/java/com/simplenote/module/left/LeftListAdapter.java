package com.simplenote.module.left;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.simplenote.R;
import com.simplenote.model.ImageModel;
import com.simplenote.util.ImageUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/7/19.
 */

public class LeftListAdapter extends BaseAdapter {

    private Context context;

    private List<String> imageResList;

    public LeftListAdapter(Context context, List<String> imageResList){
        this.context = context;
        this.imageResList = imageResList;
    }



    private int getResId(String name){
        int id = 0;

        try {
            Field field =R.drawable.class.getField(name);
            id= field.getInt(new R.drawable());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    private Drawable getDrawable(int res){
        Drawable drawable = null;

        Resources resources = context.getResources();
        XmlResourceParser xrp = resources.getXml(res);
        try {
            drawable = Drawable.createFromXml(context.getResources(), xrp);
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return drawable;
    }


    @Override
    public int getCount() {
        return imageResList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageResList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.adapter_select_emotion,viewGroup,false);
        ImageView imageView = (ImageView) linearLayout.findViewById(R.id.iv_select_emotion);
        imageView.setImageResource(ImageUtils.getResource((String)getItem(i),context));
//        imageView.setImageResource(R.drawable.selector_c1_00);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
            }
        });

        return linearLayout;
    }
}
