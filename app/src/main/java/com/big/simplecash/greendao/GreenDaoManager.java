package com.big.simplecash.greendao;


import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by big on 2019/3/13.
 */

public class GreenDaoManager {
    public static final String sDBRECORD = "material.db";
    public static final String ORDER_DB = "order.db";
    public static final String SETTLE_DB = "settle.db";


    private static GreenDaoManager mInstance;

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                    setDebug();
                }
            }
        }
        return mInstance;
    }

    Context mContext;

    public void init(Context context) {
        mContext = context;
    }

    /**
     * 打开输出日志，默认关闭
     */
    public static void setDebug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public MaterialInfoDao getMaterialInfoDao() {
        try {
            return getDaoSession(sDBRECORD).getMaterialInfoDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public OrderDao getOrderDao() {
        try {
            return getDaoSession(ORDER_DB).getOrderDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public OrderDao getSettleDao() {
        try {
            return getDaoSession(SETTLE_DB).getOrderDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Map<String, DaoMaster.DevOpenHelper> mapOpenHelper = new HashMap<>();

    private Map<String, DaoMaster> mapDaoMaster = new HashMap<>();

    private Map<String, DaoSession> mapDaoSession = new HashMap<>();

    /**
     * 通过数据库的名称来分别拿到数据库回话操作类
     *
     * @param dbName
     * @return
     */
    public DaoSession getDaoSession(String dbName) {
        DaoSession daoSession = null;
        try {
            if (mapDaoSession.containsKey(dbName)) {
                daoSession = mapDaoSession.get(dbName);
            } else {
                if (mapDaoMaster.containsKey(dbName)) {
                    daoSession = mapDaoMaster.get(dbName).newSession();
                    mapDaoSession.put(dbName, daoSession);
                } else {
                    if (mapOpenHelper.containsKey(dbName)) {
                        DaoMaster daoMaster = new DaoMaster(mapOpenHelper.get(dbName).getWritableDatabase());
                        mapDaoMaster.put(dbName, daoMaster);
                        daoSession = daoMaster.newSession();
                        mapDaoSession.put(dbName, daoSession);
                    } else {
                        DaoMaster.DevOpenHelper helper = new MyOpenHelper(mContext, dbName);
                        mapOpenHelper.put(dbName, helper);
                        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
                        mapDaoMaster.put(dbName, daoMaster);
                        daoSession = daoMaster.newSession();
                        mapDaoSession.put(dbName, daoSession);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return daoSession;
    }
}
