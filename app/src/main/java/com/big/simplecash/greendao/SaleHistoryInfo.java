package com.big.simplecash.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SaleHistoryInfo {
    @Id(autoincrement = true)
    public Long id;
    public String name;
    public float price;
    public String provider;
    public String size;
    public int number = 1;
    public float realPrice;
    public float salePrice;
    public long createTime;
    public String alias;
    @Generated(hash = 1127430839)
    public SaleHistoryInfo(Long id, String name, float price, String provider,
            String size, int number, float realPrice, float salePrice,
            long createTime, String alias) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.provider = provider;
        this.size = size;
        this.number = number;
        this.realPrice = realPrice;
        this.salePrice = salePrice;
        this.createTime = createTime;
        this.alias = alias;
    }

    public SaleHistoryInfo(SaleInfo saleInfo) {
        this.name = saleInfo.name;
        this.price = saleInfo.price;
        this.provider = saleInfo.provider;
        this.size = saleInfo.size;
        this.number = saleInfo.number;
        this.realPrice = saleInfo.realPrice;
        this.salePrice = saleInfo.salePrice;
    }
    @Generated(hash = 816533848)
    public SaleHistoryInfo() {
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
    public int getNumber() {
        return this.number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public float getRealPrice() {
        return this.realPrice;
    }
    public void setRealPrice(float realPrice) {
        this.realPrice = realPrice;
    }
    public float getSalePrice() {
        return this.salePrice;
    }
    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
