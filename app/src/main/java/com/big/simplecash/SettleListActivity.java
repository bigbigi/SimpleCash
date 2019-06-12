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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by big on 2019/6/11.
 */

public class SettleListActivity extends BaseActivity {
    private static final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm", Locale.CHINA);
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlelist);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(GreenDaoUtils.getSettles());
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
            if (position % 2 != 0) {
                holder.itemView.setBackgroundColor(0xffdddddd);
            } else {
                holder.itemView.setBackgroundColor(0xffffffff);
            }
            holder.name.setText(mSimpleDateFormat.format(new Date(info.createDate)));
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
            TextView name, total, del;

            public MyHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.item_name);
                total = itemView.findViewById(R.id.item_total);
                del = itemView.findViewById(R.id.item_del);
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
