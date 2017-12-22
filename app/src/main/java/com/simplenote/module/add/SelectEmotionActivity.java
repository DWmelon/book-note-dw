package com.simplenote.module.add;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.simplenote.R;
import com.simplenote.application.MyClient;

import butterknife.OnClick;

/**
 * Created by melon on 2017/7/17.
 */

public class SelectEmotionActivity extends SelectEmoBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData(){

        indexFirst = MyClient.getMyClient().getAddNoteManager().getEmotionIndex();
        indexSecond = MyClient.getMyClient().getAddNoteManager().getWeatherIndex();
        indexThird = MyClient.getMyClient().getAddNoteManager().getThemeIndex();

        if (indexFirst >= 0){
            mIvFirstList.get(indexFirst).setSelected(true);
        }

        if (indexSecond >= 0){
            mIvSecondList.get(indexSecond).setSelected(true);
        }

        if (indexThird >= 0){
            mIvThirdList.get(indexThird).setSelected(true);
        }

    }

    void handleSelectInfo(){
        MyClient.getMyClient().getAddNoteManager().setEmotion(indexFirst);
        MyClient.getMyClient().getAddNoteManager().setWeather(indexSecond);
        MyClient.getMyClient().getAddNoteManager().setTheme(indexThird);

        finish();
    }

    @OnClick(R.id.iv_bar_left_icon)
    void back(){
        finish();
    }

}
