package com.big.simplecash.util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.util.Base64;

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

    public static int getTextInt(TextView textView) {
        if (TextUtils.isEmpty(textView.getText())) {
            return 0;
        } else {
            return Integer.parseInt(String.valueOf(textView.getText()));
        }
    }

    public static String getText(float f) {
        if (f == 0) {
            return "";
        } else {
            return f + "";
        }
    }

    public static String compress(String str) {
        if (null == str || str.length() <= 0) {
            return str;
        }
        try {
            // 创建一个新的 byte 数组输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 使用默认缓冲区大小创建新的输出流
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            // 将 b.length 个字节写入此输出流
            gzip.write(str.getBytes());
            gzip.close();
            // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
//            return out.toString("ISO-8859-1");
            return Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 字符串的解压
     *
     * @param str 对字符串解压
     * @return 返回解压缩后的字符串
     * @throws IOException
     */
    public static String unCompress(String str) {
        if (null == str || str.length() <= 0) {
            return str;
        }
        try {
//            str=decode(str);
            // 创建一个新的 byte 数组输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
            ByteArrayInputStream in = new ByteArrayInputStream(Base64.decode(str, Base64.NO_WRAP));
            // 使用默认缓冲区大小创建新的输入流
            GZIPInputStream gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n = 0;
            while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
                // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
                out.write(buffer, 0, n);
            }
            // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
            return out.toString("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 加密
     * oldWord：需要加密的文字/比如密码
     */
    public static String encode(String oldWord) {
        String encodeWord = "";
        try {
            encodeWord = Base64.encodeToString(oldWord.getBytes("utf-8"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeWord;
    }

    /**
     * 解密
     * encodeWord：加密后的文字/比如密码
     */
    public static String decode(String encodeWord) {
        String decodeWord = "";
        try {
            decodeWord = new String(Base64.decode(encodeWord, Base64.NO_WRAP), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodeWord;

    }

}
