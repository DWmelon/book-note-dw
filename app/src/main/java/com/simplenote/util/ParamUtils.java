package com.simplenote.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.simplenote.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by melon on 2017/8/10.
 */

public class ParamUtils {

    public static boolean checkInputPhone(Context context, String phone) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(context, R.string.signup_please_iput_phone, Toast.LENGTH_LONG).show();
            return false;
        }

        boolean isFormatCorrect = ParamUtils.checkIsValidPhoneNum(phone);

        if (!isFormatCorrect) {
            Toast.makeText(context, R.string.signup_phone_format_error, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private static final Pattern CHINA_VALID_PHONE_NUM_PATTERN = Pattern.compile("^[1][3,4,5,7,8]\\d{9}$");
    public static boolean checkIsValidPhoneNum(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        Matcher mc = CHINA_VALID_PHONE_NUM_PATTERN.matcher(phone);
        if (mc.find()) {
            return true;
        }

        return false;
    }

    private static final Pattern SIX_CAPTCHA_PATTERM = Pattern.compile("[0-9]{6}");
    public static boolean checkIsValidCaptcha(String str) {
        if (TextUtils.isEmpty(str)) {

            return false;
        }

        Matcher mc = SIX_CAPTCHA_PATTERM.matcher(str);
        if (mc.find()) {
            return true;
        }

        return false;

    }

}
