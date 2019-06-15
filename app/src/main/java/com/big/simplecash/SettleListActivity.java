package com.big.simplecash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.big.simplecash.greendao.GreenDaoUtils;
import com.big.simplecash.greendao.Order;
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
    private static final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("YY/MM/dd-HH:mm", Locale.CHINA);
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

    InputDialog mInputDialog;

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
            mAdapter.notifyDataSetChanged();
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        List<Order> mList = new ArrayList<>();

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
            if (position % 2 == 0) {
                holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_item_second_sel));
            } else {
                holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_item_sel));
            }
            holder.name.setText(mSimpleDateFormat.format(new Date(info.createDate)));
            holder.modify.setText(mSimpleDateFormat.format(new Date(info.modifyTime)));
            holder.total.setText("HK$ " + info.totalPurchase);
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
            TextView name, total, del, modify;

            public MyHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.item_name);
                total = (TextView) itemView.findViewById(R.id.item_total);
                del = (TextView) itemView.findViewById(R.id.item_del);
                modify = (TextView) itemView.findViewById(R.id.item_modify);
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
                        if (del.getVisibility() == View.VISIBLE) {
                            if (del != null) {
                                del.setVisibility(View.GONE);
                            }
                        } else {
                            Intent intent = new Intent(SettleListActivity.this, SettlementActivity.class);
                            Application.mSettlementOrder = mList.get(getAdapterPosition());
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
