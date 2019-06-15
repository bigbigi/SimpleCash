package com.big.simplecash;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.big.simplecash.util.Base64;
import com.big.simplecash.util.SimpleTextWatch;
import com.big.simplecash.util.Utils;

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
    private TextView mRate, mCost, mTransIn, mTransOut, mProfit, mTotalSale, mDiscount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.output).setOnClickListener(this);
        mRate = (TextView) findViewById(R.id.rate_content);
        mCost = (TextView) findViewById(R.id.cost_content);
        mSum = (TextView) findViewById(R.id.sum_content);
        mTransIn = (TextView) findViewById(R.id.trans_in_content);
        mTransOut = (TextView) findViewById(R.id.trans_out_content);
        mDiscount = (TextView) findViewById(R.id.discount_content);
        mProfit = (TextView) findViewById(R.id.profit_content);
        mTotalSale = (TextView) findViewById(R.id.total_sale_content);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList.add(new SaleInfo());
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mOrder = Application.mSettlementOrder;
        mOrder.parseList(mList);
        mRate.setText(mOrder.rate + "");
        mCost.setText(mOrder.cost + "");
        mSum.setText(mOrder.totalPurchase + "");
        mTransIn.setText(Utils.getText(mOrder.transIn));
        mTransOut.setText(Utils.getText(mOrder.transOut));
        mDiscount.setText(Utils.getText(mOrder.discount));
        mAdapter.notifyDataSetChanged();
        mTransIn.addTextChangedListener(new SimpleTextWatch() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                super.onTextChanged(charSequence, i, i1, i2);
                mOrder.transIn = Utils.getTextFloat(mTransIn);
                sum();
            }
        });
        mTransOut.addTextChangedListener(new SimpleTextWatch() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                super.onTextChanged(charSequence, i, i1, i2);
                mOrder.transOut = Utils.getTextFloat(mTransOut);
                sum();
            }
        });
        sum();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save) {
            if (save()) {
                Toast.makeText(this, "结算保存成功", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.output) {
            if (save()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("text", Utils.compress(mOrder.outPut())));
                Toast.makeText(this, "导出成功", Toast.LENGTH_LONG).show();
            }
        } else {
            Intent intent = new Intent(this, MaterialActivity.class);
            intent.putExtra("from", "sale");
            startActivityForResult(intent, 101);
        }

    }

    private Order mOrder;

    private boolean save() {
        if (TextUtils.isEmpty(mRate.getText()) || mList.size() <= 1) {
            Toast.makeText(SettlementActivity.this, "汇率或订单为空", Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (mOrder == null) {
                mOrder = new Order();
                mOrder.createDate = System.currentTimeMillis();
            }
            mOrder.createContent(mList);
            mOrder.totalPurchase = Float.parseFloat(String.valueOf(mSum.getText()));
            mOrder.rate = Float.parseFloat(String.valueOf(mRate.getText()));
            mOrder.cost = Float.parseFloat(String.valueOf(mCost.getText()));

            mOrder.transIn = Utils.getTextFloat(mTransIn);
            mOrder.transOut = Utils.getTextFloat(mTransOut);
            mOrder.discount = Utils.getTextFloat(mDiscount);

            GreenDaoUtils.insertSettle(mOrder);
            return true;
        }
    }

    private void sum() {
        float sum = 0;
        for (SaleInfo info : mList) {
            if (info.price == 0) continue;
            sum += info.salePrice * info.number;
        }
        float profit = sum + mOrder.transIn +mOrder.discount*mOrder.rate- mOrder.transOut - mOrder.cost - mOrder.rate * mOrder.totalPurchase;
        mTotalSale.setText(String.format("%.1f", sum));
        mProfit.setText(String.format("%.1f", profit));
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
                holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_item_second_sel));
            } else {
                holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_item_sel));
            }
            if (position != 0) {
                holder.name.setText(saleInfo.name);
                holder.size.setText(saleInfo.size);
                holder.salePrice.setText(Utils.getText(saleInfo.salePrice));
                holder.provide.setText(saleInfo.provider);
                holder.realPrice.setText(saleInfo.realPrice + "");
                holder.num.setText(saleInfo.number + "");
                holder.profit.setText(String.format("%.1f", (saleInfo.salePrice - mOrder.rate * saleInfo.realPrice) * saleInfo.number));
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
                name = (TextView) itemView.findViewById(R.id.item_name);
                size = (TextView) itemView.findViewById(R.id.item_size);
                salePrice = (TextView) itemView.findViewById(R.id.item_sale_price);
                provide = (TextView) itemView.findViewById(R.id.item_provider);
                realPrice = (TextView) itemView.findViewById(R.id.item_real_price);
                num = (TextView) itemView.findViewById(R.id.item_num);
                profit = (TextView) itemView.findViewById(R.id.item_profit);


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
                                profit.setText(String.format("%.1f", (info.salePrice - mOrder.rate * info.realPrice) * info.number) + "");
                                sum();
                            }
                        }
                    });
                }


            }

        }
    }
}
