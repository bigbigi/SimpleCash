package com.big.simplecash.greendao;

import android.text.TextUtils;

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
    public long modifyTime;
    public String content;
    public float rate;
    public float totalPurchase;
    public float cost;
    public float transIn;
    public float transOut;
    public float discount;

    public String outPut() {
        StringBuilder sb = new StringBuilder();
        sb.append(createDate).append("#^")
            .append(content).append("#^")
            .append(rate).append("#^")
            .append(totalPurchase).append("#^")
            .append(cost).append("#^")
            .append(transIn).append("#^")
            .append(transOut).append("#^")
            .append(modifyTime).append("#^")
            .append(discount).append("#^");
        return sb.toString();
    }

    public static Order intPut(String s) {
        Order order = new Order();
        try {
            String fields[] = s.split("#\\^");
            order.createDate = Long.parseLong(fields[0]);
            order.content = fields[1];
            order.rate = Float.parseFloat(fields[2]);
            order.totalPurchase = Float.parseFloat(fields[3]);
            order.cost = Float.parseFloat(fields[4]);
            order.transIn = Float.parseFloat(fields[5]);
            order.transOut = Float.parseFloat(fields[6]);
            order.modifyTime = Long.parseLong(fields[7]);
            order.discount = Float.parseFloat(fields[8]);
        } catch (Exception e) {
            return null;
        }
        return order;
    }

    @Generated(hash = 1903335733)
    public Order(Long id, long createDate, long modifyTime, String content,
            float rate, float totalPurchase, float cost, float transIn,
            float transOut, float discount) {
        this.id = id;
        this.createDate = createDate;
        this.modifyTime = modifyTime;
        this.content = content;
        this.rate = rate;
        this.totalPurchase = totalPurchase;
        this.cost = cost;
        this.transIn = transIn;
        this.transOut = transOut;
        this.discount = discount;
    }

    @Generated(hash = 1105174599)
    public Order() {
    }

    public void createContent(List<SaleInfo> list) {
        StringBuilder sb = new StringBuilder();
        for (SaleInfo info : list) {
            if (TextUtils.isEmpty(info.name)) continue;
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

    public void parseList(List<SaleInfo> list) {
        String[] sales = content.split("&&");
        for (String sale : sales) {
            String[] fields = sale.split("\\|");
            SaleInfo info = new SaleInfo();
            info.name = fields[0];
            info.size = fields[1];
            info.price = Float.parseFloat(fields[2]);
            info.provider = fields[3];
            info.realPrice = Float.parseFloat(fields[4]);
            info.salePrice = Float.parseFloat(fields[5]);
            info.number = Integer.parseInt(fields[6]);
            list.add(info);
        }
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

    public float getCost() {
        return this.cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getTransIn() {
        return this.transIn;
    }

    public void setTransIn(float transIn) {
        this.transIn = transIn;
    }

    public float getTransOut() {
        return this.transOut;
    }

    public void setTransOut(float transOut) {
        this.transOut = transOut;
    }

    public long getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public float getDiscount() {
        return this.discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

}
