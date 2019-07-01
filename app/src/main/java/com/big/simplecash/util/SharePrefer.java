package com.big.simplecash.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.big.simplecash.Application;

/**
 * Created by big on 2019/7/1.
 */

public class SharePrefer {
    private static SharedPreferences getSp() {
        return Application.context.getSharedPreferences("order", Context.MODE_MULTI_PROCESS | Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = getSp().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(String key, int def) {
        return getSp().getLong(key, def);
    }

    private static final String ORDER_NUM = "orderNum";
    private static final long oneDate = 24 * 60 * 60 * 1000;

    public static int getDateNum(long date) {
        int count;
        long history = getLong(ORDER_NUM, 0);
        long dateBegin = date - date % oneDate;
        if (Math.abs(history - dateBegin) >= oneDate) {
            count = 1;
            putLong(ORDER_NUM, dateBegin + 1);
        } else {
            count = (int) (history - dateBegin) + 1;
            putLong(ORDER_NUM, history + 1);
        }
        return count;
    }


}
