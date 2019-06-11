package com.big.simplecash;

import com.big.simplecash.greendao.GreenDaoManager;
import com.big.simplecash.greendao.MaterialInfo;

/**
 * Created by big on 2019/6/11.
 */

public class Application extends android.app.Application {

    public static MaterialInfo mTempInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoManager.getInstance().init(this);
    }
}
