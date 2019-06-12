package com.big.simplecash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.big.simplecash.greendao.GreenDaoUtils;
import com.big.simplecash.greendao.Order;
import com.big.simplecash.greendao.SaleInfo;
import com.big.simplecash.material.MaterialActivity;
import com.big.simplecash.util.SimpleTextWatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by big on 2019/6/11.
 */

public class SettlementActivity extends BaseActivity implements
    View.OnClickListener {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    List<SaleInfo> mList = new ArrayList<>();
    private TextView mSum;
    private TextView mRate, mCost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        findViewById(R.id.save).setOnClickListener(this);
        mRate = findViewById(R.id.rate_content);
        mCost = findViewById(R.id.cost_content);
        mSum = findViewById(R.id.sum_content);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList.add(new SaleInfo());
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mOrder = Application.mSettlementOrder;
        mOrder.parseList(mList);
        mRate.setText(mOrder.rate + "");
        mCost.setText(mOrder.cost+"");
        mSum.setText(mOrder.totalPurchase+"");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save) {
            if (TextUtils.isEmpty(mRate.getText()) || mList.size() <= 1) {
                Toast.makeText(SettlementActivity.this, "汇率或订单为空", Toast.LENGTH_LONG).show();
            } else {
                save();
            }
        } else {
            Intent intent = new Intent(this, MaterialActivity.class);
            intent.putExtra("from", "sale");
            startActivityForResult(intent, 101);
        }

    }

    private Order mOrder;

    private void save() {
        if (mOrder == null) {
            mOrder = new Order();
            mOrder.createDate = System.currentTimeMillis();
        }
        mOrder.createContent(mList);
        mOrder.totalPurchase = Float.parseFloat(String.valueOf(mSum.getText()));
        mOrder.rate = Float.parseFloat(String.valueOf(mRate.getText()));
        GreenDaoUtils.insertOrder(mOrder);
        Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
    }

    private void sum() {
        float sum = 0;
        for (SaleInfo info : mList) {
            if (info.price == 0) continue;
            sum += info.realPrice * info.number;
        }
        mSum.setText(sum + "");
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {


        @Override
        public MyAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View content = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new MyAdapter.MyHolder(content);
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyHolder holder, int position) {
            SaleInfo saleInfo = mList.get(position);
            if (position % 2 != 0) {
                holder.itemView.setBackgroundColor(0xffdddddd);
            } else {
                holder.itemView.setBackgroundColor(0xffffffff);
            }
            if (position != 0) {
                holder.name.setText(saleInfo.name);
                holder.size.setText(saleInfo.size);
                holder.salePrice.setText(String.valueOf(saleInfo.salePrice));
                holder.provide.setText(saleInfo.provider);
                holder.realPrice.setText(saleInfo.realPrice + "");
                holder.num.setText(saleInfo.number + "");
                holder.profit.setText((saleInfo.salePrice - mOrder.rate * saleInfo.realPrice) * saleInfo.number + "");
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return R.layout.item_settle_title;
            }
            return R.layout.item_settle;
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView name, salePrice, provide, size, profit, realPrice, num;

            public MyHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.item_name);
                size = itemView.findViewById(R.id.item_size);
                salePrice = itemView.findViewById(R.id.item_sale_price);
                provide = itemView.findViewById(R.id.item_provider);
                realPrice = itemView.findViewById(R.id.item_real_price);
                num = itemView.findViewById(R.id.item_num);
                profit = itemView.findViewById(R.id.item_profit);


                if (salePrice instanceof EditText) {
                    salePrice.addTextChangedListener(new SimpleTextWatch() {
                        @Override
                        public void afterTextChanged(Editable editable) {
                            int position = getAdapterPosition();
                            if (position > 0 && position < mList.size()) {
                                SaleInfo info = mList.get(position);
                                if (TextUtils.isEmpty(salePrice.getText())) {
                                    info.salePrice = 0;
                                } else {
                                    info.salePrice = Float.parseFloat(salePrice.getText().toString());
                                }
                                profit.setText((info.salePrice - mOrder.rate * info.realPrice) * info.number + "");
                            }
                        }
                    });
                }


            }

        }
    }
}
