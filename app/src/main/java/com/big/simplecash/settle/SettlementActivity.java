package com.big.simplecash.settle;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.big.simplecash.Application;
import com.big.simplecash.BaseActivity;
import com.big.simplecash.OutputDialog;
import com.big.simplecash.R;
import com.big.simplecash.SingleEditDialog;
import com.big.simplecash.greendao.GreenDaoUtils;
import com.big.simplecash.greendao.Order;
import com.big.simplecash.greendao.SaleHistoryInfo;
import com.big.simplecash.greendao.SaleInfo;
import com.big.simplecash.util.CallBack;
import com.big.simplecash.util.ExcelUtil;
import com.big.simplecash.util.SimpleTextWatch;
import com.big.simplecash.util.Utils;

import java.util.ArrayList;
import java.util.Date;
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
    private TextView mRate, mCost, mTransIn, mTransOut, mProfit, mTotalSale,
            mDiscount, mSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        findViewById(R.id.output).setOnClickListener(this);
        mRate = (TextView) findViewById(R.id.rate_content);
        mCost = (TextView) findViewById(R.id.cost_content);
        mSum = (TextView) findViewById(R.id.sum_content);
        mTransIn = (TextView) findViewById(R.id.trans_in_content);
        mTransOut = (TextView) findViewById(R.id.trans_out_content);
        mDiscount = (TextView) findViewById(R.id.discount_content);
        mProfit = (TextView) findViewById(R.id.profit_content);
        mTotalSale = (TextView) findViewById(R.id.total_sale_content);
        mSave = (TextView) findViewById(R.id.save);

        mSave.setOnClickListener(this);
        mTransIn.setOnClickListener(this);
        mTransOut.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mOrder = (Order) getIntent().getSerializableExtra("data");
        mOrder.parseList(mList);
        mRate.setText(mOrder.rate + "");
        mCost.setText(mOrder.cost + "");
        mSum.setText(String.format("%.1f", mOrder.totalPurchase));
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
            if (mIsEditMode && save()) {
                Toast.makeText(this, "结算保存成功", Toast.LENGTH_LONG).show();
            }
            mIsEditMode = !mIsEditMode;
            mSave.setText(mIsEditMode ? R.string.save : R.string.edit);
        } else if (view.getId() == R.id.output) {
            showOutDialog();
        } else if (view.getId() == R.id.excel) {
            new Thread() {
                @Override
                public void run() {
                    final String name = doExcel();
                    if (!TextUtils.isEmpty(name)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SettlementActivity.this, "导出成功:\n" + name, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }.start();
        } else if (view.getId() == R.id.data) {
            if (save()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("text", Utils.compress(mOrder.outPut())));
                Toast.makeText(this, "导出成功\n已复制到剪切板", Toast.LENGTH_LONG).show();
            }
        } else if (mIsEditMode) {
            final TextView inputTxt = (TextView) view;
            String name = "";
            if (view.getId() == R.id.trans_in_content) {
                name = "运费收入￥";
            } else if (view.getId() == R.id.trans_out_content) {
                name = "运费支出￥";
            }
            showSingleDialog(inputTxt, name, String.valueOf(inputTxt.getText()), null);
        }
    }

    private Order mOrder;
    private OutputDialog mOutputDialog;
    private boolean mIsEditMode;

    private void showOutDialog() {
        if (mOutputDialog == null) {
            mOutputDialog = new OutputDialog(this);
            mOutputDialog.setCallback(new CallBack<View>() {
                @Override
                public void onCallBack(View view) {
                    onClick(view);
                }
            });
        }
        mOutputDialog.show();
    }

    private SingleEditDialog mSingleEditDialog;

    private void showSingleDialog(TextView textView, String name, String value, CallBack callBack) {
        if (mSingleEditDialog == null) {
            mSingleEditDialog = new SingleEditDialog(this);
        }
        mSingleEditDialog.setCallback(callBack);
        mSingleEditDialog.show(textView, name, value);
    }

    private boolean save() {
        if (TextUtils.isEmpty(mRate.getText()) || mList.size() <= 0) {
            Toast.makeText(SettlementActivity.this, "汇率或订单为空", Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (mOrder == null) {
                mOrder = new Order();
                mOrder.createDate = System.currentTimeMillis();
            }
            mOrder.createContent(mList);
            mOrder.rate = Float.parseFloat(String.valueOf(mRate.getText()));
            mOrder.cost = Float.parseFloat(String.valueOf(mCost.getText()));

            mOrder.transIn = Utils.getTextFloat(mTransIn);
            mOrder.transOut = Utils.getTextFloat(mTransOut);
            mOrder.discount = Utils.getTextFloat(mDiscount);
            mOrder.profit = Utils.getTextFloat(mProfit);

            GreenDaoUtils.insertSettle(mOrder);
            new Thread() {
                @Override
                public void run() {
                    for (SaleInfo info : mList) {
                        SaleHistoryInfo historyInfo = new SaleHistoryInfo(info);
                        historyInfo.createTime = mOrder.createDate;
                        GreenDaoUtils.insertHistoryInfo(historyInfo);
                    }
                }
            }.start();
            return true;
        }
    }

    private void sum() {
        float sum = 0;
        for (SaleInfo info : mList) {
            if (info.price == 0) continue;
            sum += info.salePrice * info.number;
        }
        float profit = sum + mOrder.transIn + mOrder.discount * mOrder.rate - mOrder.transOut - mOrder.cost - mOrder.rate * mOrder.totalPurchase;
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
            if (position == mCurPos) {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.itemFocus));
            } else if (position % 2 != 0) {
                holder.itemView.setBackgroundColor(0xffffffff);
            } else {
                holder.itemView.setBackgroundColor(0xffdddddd);
            }
            holder.name.setText(saleInfo.name);
            holder.size.setText(saleInfo.size);
            holder.salePrice.setText(Utils.getText(saleInfo.salePrice));
            holder.provide.setText(saleInfo.provider);
            holder.realPrice.setText(saleInfo.realPrice + "");
            holder.num.setText(saleInfo.number + "");
            holder.profit.setText(String.format("%.1f", (saleInfo.salePrice - mOrder.rate * saleInfo.realPrice) * saleInfo.number));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.item_settle;
        }

        private int mCurPos = -1;

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
                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        notifyItemChanged(mCurPos);
                        mCurPos = getAdapterPosition();
                        notifyItemChanged(mCurPos);
                        if (mIsEditMode && mCurPos >= 0 && mCurPos < mList.size()) {
                            final SaleInfo info = mList.get(mCurPos);
                            showSingleDialog(salePrice, info.name + " 售价￥", info.salePrice + "", new CallBack<Float>() {
                                @Override
                                public void onCallBack(Float o) {
                                    info.salePrice = o;
                                    notifyItemChanged(mCurPos);
                                    sum();
                                }
                            });
                        }
                    }
                });

            }

        }
    }

    private String doExcel() {
        ArrayList<ArrayList<String>> recordList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            SaleInfo info = mList.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(info.name);
            beanList.add(info.size);
            beanList.add(info.price + "");
            beanList.add(info.realPrice + "");
            beanList.add(info.salePrice + "");
            beanList.add(info.provider);
            beanList.add(info.number + "");
            beanList.add(info.realPrice * info.number + "");
            beanList.add(String.format("%.1f", (info.salePrice - mOrder.rate * info.realPrice) * info.number));
            recordList.add(beanList);
        }
        recordList.add(new ArrayList<String>());
        //add rate
        ArrayList<String> rate = new ArrayList<>();
        rate.add("汇率：");
        rate.add(mOrder.rate + "");
        recordList.add(rate);

        ArrayList<String> purchase = new ArrayList<>();
        purchase.add("采购支出$：");
        purchase.add(mOrder.totalPurchase + "");
        recordList.add(purchase);

        //discount and
        ArrayList<String> discount = new ArrayList<>();
        discount.add("优惠$：");
        discount.add(mOrder.discount + "");
        recordList.add(discount);

        ArrayList<String> temp = new ArrayList<>();
        temp.add("优惠后采购$：");
        temp.add((mOrder.totalPurchase - mOrder.discount) + "");

        temp.add("*对应人民币：");
        temp.add(String.format("%.1f", (mOrder.totalPurchase - mOrder.discount) * mOrder.rate));
        recordList.add(temp);

        //total purchase and sale
        ArrayList<String> sale = new ArrayList<>();
        sale.add("货款收入￥：");
        sale.add(String.valueOf(mTotalSale.getText()));
        sale.add("*其他成本￥：");
        sale.add(mOrder.cost + "");
        recordList.add(sale);


        //add transOut and transIn
        ArrayList<String> transOut = new ArrayList<>();
        transOut.add("运费收入￥：");
        transOut.add(mOrder.transIn + "");

        transOut.add("*运费支出￥：");
        transOut.add(mOrder.transOut + "");
        recordList.add(transOut);


        //add profit
        ArrayList<String> profit = new ArrayList<>();
        profit.add("*总收款￥：");
        profit.add(String.valueOf(mOrder.transIn + Utils.getTextFloat(mTotalSale)));
        profit.add("利润￥：");
        profit.add(String.valueOf(mProfit.getText()));
        recordList.add(profit);

        //write
        String name = !TextUtils.isEmpty(mOrder.name) ? mOrder.name :
                Application.mNameDateFormat.format(new Date(mOrder.createDate));
        String fileName = ExcelUtil.getFileName(name);
        String titles[] = {"名称", "规格", "原价$", "进价$", "售价￥", "店铺", "数量", "小计$", "利润￥"};
        ExcelUtil.initExcel(fileName, name, titles);
        if (ExcelUtil.writeObjListToExcel(recordList, fileName, this)) {
            return fileName;
        } else {
            return null;
        }
    }
}
