package com.big.simplecash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.big.simplecash.greendao.GreenDaoUtils;
import com.big.simplecash.greendao.MaterialInfo;
import com.big.simplecash.greendao.SaleInfo;
import com.big.simplecash.material.MaterialActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by big on 2019/6/11.
 */

public class SaleActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    List<SaleInfo> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        findViewById(R.id.add).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList.add(new SaleInfo());
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

//        mAdapter.setData(new ArrayList<MaterialInfo>());
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MaterialActivity.class);
        intent.putExtra("from", "sale");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == 100) {
            SaleInfo info = new SaleInfo();
            info.materialInfo = Application.mTempInfo;
            info.realPrice = Application.mTempInfo.price;
            info.total = info.number * info.realPrice;
            mList.add(info);
            Log.d("big", "add");
            mAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
            MaterialInfo info = saleInfo.materialInfo;
            if (position % 2 != 0) {
                holder.itemView.setBackgroundColor(0xffdddddd);
            } else {
                holder.itemView.setBackgroundColor(0xffffffff);
            }
            if (position != 0) {
                holder.name.setText(info.name);
                holder.size.setText(info.size);
                holder.price.setText(String.valueOf(info.price));
                holder.provide.setText(info.provider);
                holder.realPrice.setText(saleInfo.realPrice + "");
                holder.num.setText(saleInfo.number + "");
                holder.total.setText(saleInfo.total + "");
                Log.d("big", "id:" + info.id);
            }

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return R.layout.item_sale_title;
            }
            return R.layout.item_sale;
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView name, price, provide, size, del, total,realPrice, num;

            public MyHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.item_name);
                size = itemView.findViewById(R.id.item_size);
                price = itemView.findViewById(R.id.item_price);
                provide = itemView.findViewById(R.id.item_provider);
                del = itemView.findViewById(R.id.item_del);
                realPrice = itemView.findViewById(R.id.item_real_price);
                num = itemView.findViewById(R.id.item_num);
                total = itemView.findViewById(R.id.item_total);
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
                        }
                    }
                });
                if (del != null) {
                    del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = getAdapterPosition();
                            if (position > 0 && position < mList.size()) {
                                mList.remove(position);
                                notifyDataSetChanged();
                            }
                        }
                    });
                }

            }
        }
    }
}
