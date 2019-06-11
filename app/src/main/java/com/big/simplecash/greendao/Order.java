package com.big.simplecash.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by big on 2019/6/11.
 */
@Entity
public class Order {
    @Id(autoincrement = true)
    public Long id;
    public long createDate;
    public String content;
    public float rate;
    public float totalPurchase;

    @Generated(hash = 1448860270)
    public Order(Long id, long createDate, String content, float rate,
            float totalPurchase) {
        this.id = id;
        this.createDate = createDate;
        this.content = content;
        this.rate = rate;
        this.totalPurchase = totalPurchase;
    }

    @Generated(hash = 1105174599)
    public Order() {
    }

    public void createContent(List<SaleInfo> list) {
        StringBuilder sb = new StringBuilder();
        for (SaleInfo info : list) {
            sb.append(info.name).append("|")
                .append(info.size).append("|")
                .append(info.price).append("|")
                .append(info.provider).append("|")
                .append(info.realPrice).append("|")
                .append(info.salePrice).append("|")
                .append(info.number).append("|")
                .append("&&");
        }
        content = sb.toString();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getRate() {
        return this.rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getTotalPurchase() {
        return this.totalPurchase;
    }

    public void setTotalPurchase(float totalPurchase) {
        this.totalPurchase = totalPurchase;
    }
}
