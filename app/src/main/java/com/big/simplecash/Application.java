package com.big.simplecash;

import android.content.Context;

import com.big.simplecash.greendao.GreenDaoManager;
import com.big.simplecash.greendao.MaterialInfo;
import com.big.simplecash.greendao.Order;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by big on 2019/6/11.
 */

public class Application extends android.app.Application {

    public static MaterialInfo mTempInfo;
    public static Order mTempOrder;
    public static final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("YYMMdd HH:mm:ss", Locale.CHINA);
    public static final SimpleDateFormat mNameDateFormat = new SimpleDateFormat("YYMMdd", Locale.CHINA);

    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoManager.getInstance().init(this);
        context = this.getApplicationContext();
    }

    public static Context context;
}
