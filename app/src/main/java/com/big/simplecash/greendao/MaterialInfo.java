package com.big.simplecash.greendao;

import org.greenrobot.greendao.annotation.Id;

/**
 * Created by big on 2019/6/10.
 */

public class MaterialInfo {
    @Id(autoincrement = true)
    public Long _id;
    String name;
    String price;
    String original;
    String provider;
    String size;
}
