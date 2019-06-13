package com.big.simplecash.greendao;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * Created by big on 2019/6/13.
 */

public class MyOpenHelper extends DaoMaster.DevOpenHelper {
    public MyOpenHelper(Context context, String name) {
        super(new GreenDaoContext(context), name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
