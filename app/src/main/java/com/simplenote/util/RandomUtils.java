package com.simplenote.util;

import java.util.Random;

/**
 * Created by melon on 2017/8/12.
 */

public class RandomUtils {

    /**
     * 生成六位随机整数
     *
     * @return
     */
    public static String genRandomCode() {
        int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < 6; i++)
            result.append(array[i]);

        return String.valueOf(result);
    }

}
