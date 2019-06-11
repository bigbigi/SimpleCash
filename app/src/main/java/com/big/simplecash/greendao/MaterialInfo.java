package com.big.simplecash.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by big on 2019/6/10.
 */

@Entity
public class MaterialInfo {
    @Id(autoincrement = true)
    public Long id;
    public String name;
    public float price;
    public String provider;
    public String size;
    @Generated(hash = 1713483006)
    public MaterialInfo(Long id, String name, float price, String provider,
            String size) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.provider = provider;
        this.size = size;
    }
    @Generated(hash = 749381293)
    public MaterialInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getPrice() {
        return this.price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public String getProvider() {
        return this.provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public String getSize() {
        return this.size;
    }
    public void setSize(String size) {
        this.size = size;
    }

}
