package com.simplenote.module.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.simplenote.R;
import com.simplenote.application.BaseActivity;
import com.simplenote.application.MyClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by melon on 2017/7/18.
 */

public abstract class SelectEmoBaseActivity extends BaseActivity implements View.OnClickListener {

    private final int LINE_LENGTH = 5;

    @BindView(R.id.ll_select_emotion_first)
    LinearLayout mLlFirstContent;
    protected List<ImageView> mIvFirstList = new ArrayList<>();
    protected int indexFirst = -1;

    @BindView(R.id.ll_select_emotion_second)
    LinearLayout mLlSecondContent;
    protected List<ImageView> mIvSecondList = new ArrayList<>();
    protected int indexSecond = -1;

    @BindView(R.id.ll_select_emotion_third)
    LinearLayout mLlThirdContent;
    protected List<ImageView> mIvThirdList = new ArrayList<>();
    protected int indexThird = -1;

    @BindView(R.id.ll_bar_right_content)
    LinearLayout mLlRightContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_emotion);
        ButterKnife.bind(this);
        initRightBar();
        initFirstData();
        initSecondData();
        initThirdData();
    }

    private void initRightBar(){
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.icon_arrow_right_red);
        int padding = getResources().getDimensionPixelOffset(R.dimen.margin_10);
        iv.setPadding(padding,padding,padding,padding);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSelectInfo();
            }
        });
        mLlRightContent.addView(iv);
    }

    abstract void handleSelectInfo();

    private void initFirstData(){
        View view;
        ImageView iv;
        for (int i = 0;i<5;i++){
            view = LayoutInflater.from(this).inflate(R.layout.adapter_select_emotion,mLlFirstContent,false);
            iv = (ImageView) view.findViewById(R.id.iv_select_emotion);
            switch (i){
                case 0:{
                    iv.setImageResource(R.drawable.selector_c1_00);
                    break;
                }
                case 1:{
                    iv.setImageResource(R.drawable.selector_c1_01);
                    break;
                }
                case 2:{
                    iv.setImageResource(R.drawable.selector_c1_02);
                    break;
                }
                case 3:{
                    iv.setImageResource(R.drawable.selector_c1_03);
                    break;
                }
                case 4:{
                    iv.setImageResource(R.drawable.selector_c1_04);
                    break;
                }
            }
            iv.setSelected(false);
            iv.setOnClickListener(new OnClickFirst(i));
            mIvFirstList.add(iv);
            mLlFirstContent.addView(view);
        }

    }

    private void initSecondData(){
        View view;
        ImageView iv;
        for (int i = 0;i<5;i++){
            view = LayoutInflater.from(this).inflate(R.layout.adapter_select_emotion,mLlSecondContent,false);
            iv = (ImageView) view.findViewById(R.id.iv_select_emotion);
            switch (i){
                case 0:{
                    iv.setImageResource(R.drawable.selector_c0_00);
                    break;
                }
                case 1:{
                    iv.setImageResource(R.drawable.selector_c0_01);
                    break;
                }
                case 2:{
                    iv.setImageResource(R.drawable.selector_c0_02);
                    break;
                }
                case 3:{
                    iv.setImageResource(R.drawable.selector_c0_03);
                    break;
                }
                case 4:{
                    iv.setImageResource(R.drawable.selector_c0_04);
                    break;
                }
            }
            iv.setSelected(false);
            iv.setOnClickListener(new OnClickSecond(i));
            mIvSecondList.add(iv);
            mLlSecondContent.addView(view);
        }

    }

    private void initThirdData(){
        View view;
        ImageView iv;
        LinearLayout mLlContent;

        for (int j = 0;j<4;j++){

            mLlContent = new LinearLayout(this);
            mLlContent.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,getResources().getDimensionPixelOffset(R.dimen.margin_15));
            mLlThirdContent.addView(mLlContent,params);

            for (int i = 0;i<5;i++){
                view = LayoutInflater.from(this).inflate(R.layout.adapter_select_emotion,mLlContent,false);
                iv = (ImageView) view.findViewById(R.id.iv_select_emotion);
                setupThirdItem(j+""+i,iv);
                iv.setSelected(false);
                iv.setOnClickListener(new OnClickThird(i,j));
                mIvThirdList.add(iv);
                mLlContent.addView(view);
            }

        }

    }

    private void setupThirdItem(String index,ImageView iv){
        switch (index){
            case "00":{
                iv.setImageResource(R.drawable.selector_c2_00);
                break;
            }
            case "01":{
                iv.setImageResource(R.drawable.selector_c2_01);
                break;
            }
            case "02":{
                iv.setImageResource(R.drawable.selector_c2_02);
                break;
            }
            case "03":{
                iv.setImageResource(R.drawable.selector_c2_03);
                break;
            }
            case "04":{
                iv.setImageResource(R.drawable.selector_c2_04);
                break;
            }
            case "10":{
                iv.setImageResource(R.drawable.selector_c2_05);
                break;
            }
            case "11":{
                iv.setImageResource(R.drawable.selector_c2_06);
                break;
            }
            case "12":{
                iv.setImageResource(R.drawable.selector_c2_07);
                break;
            }
            case "13":{
                iv.setImageResource(R.drawable.selector_c2_08);
                break;
            }
            case "14":{
                iv.setImageResource(R.drawable.selector_c2_09);
                break;
            }
            case "20":{
                iv.setImageResource(R.drawable.selector_c2_10);
                break;
            }
            case "21":{
                iv.setImageResource(R.drawable.selector_c2_11);
                break;
            }
            case "22":{
                iv.setImageResource(R.drawable.selector_c2_12);
                break;
            }
            case "23":{
                iv.setImageResource(R.drawable.selector_c2_13);
                break;
            }
            case "24":{
                iv.setImageResource(R.drawable.selector_c2_14);
                break;
            }
            case "30":{
                iv.setImageResource(R.drawable.selector_c2_15);
                break;
            }
            case "31":{
                iv.setImageResource(R.drawable.selector_c2_16);
                break;
            }
            case "32":{
                iv.setImageResource(R.drawable.selector_c2_17);
                break;
            }
        }

    }

    public class OnClickFirst implements View.OnClickListener{

        int index = -1;

        public OnClickFirst(int index){
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            if (view.isSelected()){
                indexFirst = -1;
                view.setSelected(false);
            }else {
                for (ImageView iv : mIvFirstList){
                    iv.setSelected(false);
                }
                view.setSelected(true);
                indexFirst = index;
            }
        }
    }

    public class OnClickSecond implements View.OnClickListener{

        int index = -1;

        public OnClickSecond(int index){
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            if (view.isSelected()){
                indexSecond = -1;
                view.setSelected(false);
            }else {
                for (ImageView iv : mIvSecondList){
                    iv.setSelected(false);
                }
                view.setSelected(true);
                indexSecond = index;
            }
        }
    }

    public class OnClickThird implements View.OnClickListener{

        int index = -1;

        public OnClickThird(int indexI,int indexJ){
            index = indexJ * LINE_LENGTH + indexI;
        }

        @Override
        public void onClick(View view) {
            if (view.isSelected()){
                indexThird = -1;
                view.setSelected(false);
            }else {
                for (ImageView iv : mIvThirdList){
                    iv.setSelected(false);
                }
                view.setSelected(true);
                indexThird = index;
            }
        }
    }

    @Override
    public void onClick(View view) {
        view.setSelected(!view.isSelected());
    }
}
