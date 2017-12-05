package com.simplenote.util;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by melon on 2017/7/20.
 */

public class TimeUtils {

    public static String getNowTime(){
        return getNowTime(new Date(),"yyyy-MM-dd");
    }

    public static String getNowTime(Date date,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

}
