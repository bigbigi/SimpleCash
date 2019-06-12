package com.big.simplecash.util;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by big on 2019/6/12.
 */

public class Utils {
    public static float getTextFloat(TextView textView) {
        if (TextUtils.isEmpty(textView.getText())) {
            return 0;
        } else {
            return Float.parseFloat(String.valueOf(textView.getText()));
        }
    }

    public static String getText(float f) {
        if (f == 0) {
            return "";
        } else {
            return f + "";
        }
    }
}
