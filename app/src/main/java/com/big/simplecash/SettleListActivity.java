package com.big.simplecash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.big.simplecash.greendao.GreenDaoUtils;
import com.big.simplecash.greendao.Order;
import com.big.simplecash.greendao.SaleInfo;
import com.big.simplecash.util.CallBack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by big on 2019/6/11.
 */

public class SettleListActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlelist);
        findViewById(R.id.add).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(GreenDaoUtils.getSettles());
    }

    private InputDialog mInputDialog;

    @Override
    public void onClick(View view) {
        if (mInputDialog == null) {
            mInputDialog = new InputDialog(this);
            mInputDialog.setCallback(new CallBack<Order>() {
                @Override
                public void onCallBack(Order order) {
                    if (order != null) {
                        GreenDaoUtils.insertSettle(order);
                        mAdapter.setData(GreenDaoUtils.getSettles());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        mInputDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.setData(GreenDaoUtils.getSettles());
            mAdapter.notifyDataSetChanged();
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        List<Order> mList = new ArrayList<>();
        private int mCurPos = -1;

        public void setData(List<Order> list) {
            if (list != null) {
                mList = list;
                notifyDataSetChanged();
            }
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View content = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new MyHolder(content);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            Order info = mList.get(position);
            if (position == mCurPos) {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.itemFocus));
            }else if (position % 2 == 0) {
                holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_item_second_sel));
            } else {
                holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_item_sel));
            }
            if (TextUtils.isEmpty(info.name)) {
                holder.name.setText(Application.mNameDateFormat.format(new Date(info.createDate)));
            } else {
                holder.name.setText(info.name);
            }
            holder.modify.setText(Application.mSimpleDateFormat.format(new Date(info.modifyTime)));
            holder.profit.setText(String.format("%.1f", info.profit));
            holder.total.setText(String.format("%.1f", info.totalPurchase));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.item_order;
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView name, total, del, modify, profit;

            public MyHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.item_name);
                total = (TextView) itemView.findViewById(R.id.item_total);
                del = (TextView) itemView.findViewById(R.id.item_del);
                modify = (TextView) itemView.findViewById(R.id.item_modify);
                profit = (TextView) itemView.findViewById(R.id.item_profit);
                profit.setVisibility(View.VISIBLE);
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (del != null) {
                            del.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notifyItemChanged(mCurPos);
                        mCurPos = getAdapterPosition();
                        notifyItemChanged(mCurPos);
                        if (del.getVisibility() == View.VISIBLE) {
                            if (del != null) {
                                del.setVisibility(View.GONE);
                            }
                        } else {
                            Intent intent = new Intent(SettleListActivity.this, SettlementActivity.class);
                            intent.putExtra("data", mList.get(getAdapterPosition()));
                            startActivity(intent);
                        }
                    }
                });
                if (del != null) {
                    del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = getAdapterPosition();
                            if (position >= 0 && position < mList.size()) {
                                del.setVisibility(View.GONE);
                                Order info = mList.get(position);
                                GreenDaoUtils.deleteSettle(info);
                                mList.remove(getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        }
                    });
                }

            }
        }
    }

}
