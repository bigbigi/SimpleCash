package com.big.simplecash.util;

import java.util.List;
import java.util.Map;

public class ListUtils {


    /**
     * list 是否为null
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @param list    的长度是否满足这个position去获取
     * @param positon
     * @return
     */
    public static boolean contansPosition(List list, int positon) {
        if (!isEmpty(list) && positon < list.size()) {
            return true;
        }
        return false;
    }


    /**
     * @param map 是否为null
     * @return
     */
    public static boolean isEmpty(Map map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }
}
