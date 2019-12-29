package com.big.simplecash.material;

import android.app.Activity;
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

import com.big.simplecash.Application;
import com.big.simplecash.BaseActivity;
import com.big.simplecash.R;
import com.big.simplecash.greendao.GreenDaoUtils;
import com.big.simplecash.greendao.SaleHistoryInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MaterialHistory extends BaseActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private TextView mTitleTxt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meterial_history);
        mTitleTxt = (TextView) findViewById(R.id.title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        String name = getIntent().getStringExtra("name");
        String provider = getIntent().getStringExtra("provider");
        String size = getIntent().getStringExtra("size");
        mAdapter.setData(GreenDaoUtils.getHistorys(name, provider, size));
        mTitleTxt.setText(name);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        List<SaleHistoryInfo> mList = new ArrayList<>();

        public void setData(List<SaleHistoryInfo> list) {
            if (list != null) {
                mList = list;
                notifyDataSetChanged();
            }
        }

        @Override
        public MyAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View content = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
            return new MyAdapter.MyHolder(content);
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyHolder holder, int position) {
            SaleHistoryInfo info = mList.get(position);
            if (position % 2 == 0) {
                holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_item_second_sel));
            } else {
                holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_item_sel));
            }

            if (TextUtils.isEmpty(info.alias)) {
                holder.name.setText(Application.mNameDateFormat.format(new Date(info.createTime)));
            } else {
                holder.name.setText(info.alias);
            }
            holder.provide.setText(info.provider);
            holder.size.setText(info.size);
            holder.price.setText(String.format("%.1f", info.realPrice));
            holder.salePrice.setText(String.format("%.1f", info.salePrice));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView name, price, provide, size, salePrice, del;

            public MyHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.item_name);
                size = (TextView) itemView.findViewById(R.id.item_size);
                price = (TextView) itemView.findViewById(R.id.item_price);
                provide = (TextView) itemView.findViewById(R.id.item_provider);
                salePrice = (TextView) itemView.findViewById(R.id.item_sale_price);
                del = (TextView) itemView.findViewById(R.id.item_del);
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (del != null) {
                            del.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });

                if (del != null) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (del.getVisibility() == View.VISIBLE) {
                                if (del != null) {
                                    del.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                    del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = getAdapterPosition();
                            if (position >= 0 && position < mList.size()) {
                                del.setVisibility(View.GONE);
                                SaleHistoryInfo info = mList.get(position);
                                GreenDaoUtils.delHistory(info);
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
