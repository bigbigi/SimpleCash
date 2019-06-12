package com.big.simplecash;

import com.big.simplecash.greendao.GreenDaoManager;
import com.big.simplecash.greendao.MaterialInfo;
import com.big.simplecash.greendao.Order;

/**
 * Created by big on 2019/6/11.
 */

public class Application extends android.app.Application {

    public static MaterialInfo mTempInfo;
    public static Order mTempOrder;
    public static Order mSettlementOrder;

    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoManager.getInstance().init(this);
    }
}
