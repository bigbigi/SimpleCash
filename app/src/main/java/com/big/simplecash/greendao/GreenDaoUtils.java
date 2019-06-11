package com.big.simplecash.greendao;


import android.util.Log;

import com.big.simplecash.util.ListUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by big on 2019/3/13.
 */

public class GreenDaoUtils {
    private static final String TAG = "GreenDaoUtils";

    private static MaterialInfoDao getMaterialInfoDao() {
        return GreenDaoManager.getInstance().getMaterialInfoDao();
    }

    public static List<MaterialInfo> getAllRecord() {
        List<MaterialInfo> list = new ArrayList<>();
        try {
            QueryBuilder<MaterialInfo> ql = getMaterialInfoDao().queryBuilder();
            list = ql.orderDesc(MaterialInfoDao.Properties.Name).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getRecord list =" + list);
        return list;
    }

    public static List<MaterialInfo> getMaterialInfoByName(String name) {
        List<MaterialInfo> list = new ArrayList<>();
        try {
            QueryBuilder<MaterialInfo> ql = getMaterialInfoDao().queryBuilder();
            ql.where(MaterialInfoDao.Properties.Name.like("%" + name + "%"));
            list = ql.orderDesc(MaterialInfoDao.Properties.Name).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getRecord list =" + list);
        return list;
    }

    public static void insertMaterialInfo(MaterialInfo record) {
        if (record == null) {
            return;
        }
        boolean flag = false;
        try {
            if (!updateVodRecord(record)) {
                Log.i(TAG, "insertRecord real insert ");
                getMaterialInfoDao().insert(record);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i(TAG, "insertRecord flag = " + flag);
        }

    }

    private static boolean updateVodRecord(MaterialInfo record) {
        if (record == null) {
            return false;
        }
        boolean flag = false;
        try {
            QueryBuilder<MaterialInfo> ql = getMaterialInfoDao().queryBuilder();
            ql.where(MaterialInfoDao.Properties.Name.eq(record.name));
            ql.where(MaterialInfoDao.Properties.Size.eq(record.size));
            ql.where(MaterialInfoDao.Properties.Provider.eq(record.provider));
            List<MaterialInfo> list = ql.limit(1).list();
            Log.i(TAG, "updateRecord list1 = " + list);
            if (!ListUtils.isEmpty(list)) {
                record.id = list.get(0).id;
                Log.i(TAG, "updateRecord record._id = " + record.id);
                getMaterialInfoDao().update(record);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i(TAG, "updateRecord flag = " + flag);
        }
        return flag;
    }

    public static void deleteMaterialInfo(MaterialInfo info) {
        getMaterialInfoDao().delete(info);
    }

    /***************************************/
    private static OrderDao getOrderDao() {
        return GreenDaoManager.getInstance().getOrderDao();
    }

    public static List<Order> getOrder() {
        List<Order> list = new ArrayList<>();
        try {
            QueryBuilder<Order> ql = getOrderDao().queryBuilder();
            list = ql.orderDesc(OrderDao.Properties.CreateDate).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getRecord list =" + list);
        return list;
    }

    public static void insertOrder(Order record) {
        if (record == null) {
            return;
        }
        boolean flag = false;
        try {
            if (!updateOrder(record)) {
                Log.i(TAG, "insertRecord real insert ");
                getOrderDao().insert(record);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i(TAG, "insertRecord flag = " + flag);
        }

    }

    private static boolean updateOrder(Order record) {
        if (record == null) {
            return false;
        }
        boolean flag = false;
        try {
            QueryBuilder<Order> ql = getOrderDao().queryBuilder();
            ql.where(OrderDao.Properties.Id.eq(record.id));
            List<Order> list = ql.limit(1).list();
            Log.i(TAG, "updateRecord list1 = " + list);
            if (!ListUtils.isEmpty(list)) {
                record.id = list.get(0).id;
                Log.i(TAG, "updateRecord record._id = " + record.id);
                getOrderDao().update(record);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i(TAG, "updateRecord flag = " + flag);
        }
        return flag;
    }

    public static void deleteOrder(Order info) {
        getOrderDao().delete(info);
    }
}
