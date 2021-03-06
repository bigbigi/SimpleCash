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

    /*******************订单********************/
    private static OrderDao getOrderDao() {
        return GreenDaoManager.getInstance().getOrderDao();
    }

    public static List<Order> getOrder() {
        List<Order> list = new ArrayList<>();
        try {
            QueryBuilder<Order> ql = getOrderDao().queryBuilder();
            list = ql.orderDesc(OrderDao.Properties.CreateDate).orderDesc(OrderDao.Properties.ModifyTime).list();
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
                record.modifyTime = System.currentTimeMillis();
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


   /* public static int getOrderSize(long date) {
        int size = 0;
        long oneDate = 24 * 60 * 60 * 1000;
        long dateBegin = date - date % oneDate;
        long dateEnd = dateBegin + oneDate;
        Log.d("big", "begin:" + dateBegin + "," + dateEnd);
        try {
            QueryBuilder<Order> ql = getOrderDao().queryBuilder();
            ql.where(OrderDao.Properties.CreateDate.between(dateBegin, dateEnd));
            size = ql.list().size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }*/

    public static void deleteOrder(Order info) {
        getOrderDao().delete(info);
    }

    /*******************结算************************/

    private static OrderDao getSettleDao() {
        return GreenDaoManager.getInstance().getSettleDao();
    }

    public static List<Order> getSettles() {
        List<Order> list = new ArrayList<>();
        try {
            QueryBuilder<Order> ql = getSettleDao().queryBuilder();
            list = ql.orderDesc(OrderDao.Properties.CreateDate).orderDesc(OrderDao.Properties.ModifyTime).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getRecord list =" + list);
        return list;
    }

    public static void insertSettle(Order record) {
        if (record == null) {
            return;
        }
        boolean flag = false;
        try {
            if (!updateSettle(record)) {
                Log.i(TAG, "insertRecord real insert ");
                getSettleDao().insert(record);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i(TAG, "insertRecord flag = " + flag);
        }

    }

    private static boolean updateSettle(Order record) {
        if (record == null) {
            return false;
        }
        boolean flag = false;
        try {
            QueryBuilder<Order> ql = getSettleDao().queryBuilder();
            ql.where(OrderDao.Properties.Id.eq(record.id));
            List<Order> list = ql.limit(1).list();
            Log.i(TAG, "updateRecord list1 = " + list);
            if (!ListUtils.isEmpty(list)) {
                record.id = list.get(0).id;
                record.modifyTime = System.currentTimeMillis();
                Log.i(TAG, "updateRecord record._id = " + record.id);
                getSettleDao().update(record);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i(TAG, "updateRecord flag = " + flag);
        }
        return flag;
    }

    public static void deleteSettle(Order info) {
        getSettleDao().delete(info);
    }

    /******************商品进出历史********************/
    private static SaleHistoryInfoDao getHistoryDao() {
        return GreenDaoManager.getInstance().getHistoryDao();
    }


    public static List<SaleHistoryInfo> getHistorys(String name, String provider, String size) {
        List<SaleHistoryInfo> list = new ArrayList<>();
        try {
            QueryBuilder<SaleHistoryInfo> ql = getHistoryDao().queryBuilder();
            list = ql.where(SaleHistoryInfoDao.Properties.Name.eq(name))
                    .where(SaleHistoryInfoDao.Properties.Size.eq(size))
                    .orderDesc(SaleHistoryInfoDao.Properties.CreateTime)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getRecord list =" + list);
        return list;
    }

    public static void insertHistoryInfo(SaleHistoryInfo record) {
        if (record == null) {
            return;
        }
        boolean flag = false;
        try {
            if (!updateHistory(record)) {
                Log.i(TAG, "insertRecord real insert ");
                getHistoryDao().insert(record);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i(TAG, "insertRecord flag = " + flag);
        }

    }

    private static boolean updateHistory(SaleHistoryInfo record) {
        if (record == null) {
            return false;
        }
        boolean flag = false;
        try {
            QueryBuilder<SaleHistoryInfo> ql = getHistoryDao().queryBuilder();
            ql.where(SaleHistoryInfoDao.Properties.CreateTime.eq(record.createTime))
                    .where(SaleHistoryInfoDao.Properties.Name.eq(record.name))
                    .where(SaleHistoryInfoDao.Properties.Provider.eq(record.provider))
                    .where(SaleHistoryInfoDao.Properties.Size.eq(record.size));
            List<SaleHistoryInfo> list = ql.limit(1).list();
            Log.i(TAG, "updateRecord list1 = " + list);
            if (!ListUtils.isEmpty(list)) {
                record.id = list.get(0).id;
                Log.i(TAG, "updateRecord record._id = " + record.id);
                getHistoryDao().update(record);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i(TAG, "updateRecord flag = " + flag);
        }
        return flag;
    }

    public static void delHistory(SaleHistoryInfo info) {
        getHistoryDao().delete(info);
    }
}
