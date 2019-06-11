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

import com.big.simplecash.greendao.MaterialInfo;
import com.big.simplecash.greendao.SaleInfo;
import com.big.simplecash.material.MaterialActivity;
import com.big.simplecash.util.SimpleTextWatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by big on 2019/6/11.
 */

public class SaleActivity extends BaseActivity implements
    View.OnClickListener {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    List<SaleInfo> mList = new ArrayList<>();
    private TextView mSum;
    private TextView mRate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
        mRate = findViewById(R.id.rate_content);
        mSum = findViewById(R.id.sum_content);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList.add(new SaleInfo());
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save) {
            if (TextUtils.isEmpty(mRate.getText())) {
                Toast.makeText(SaleActivity.this, "汇率为空", Toast.LENGTH_LONG).show();
            }
        } else {
            Intent intent = new Intent(this, MaterialActivity.class);
            intent.putExtra("from", "sale");
            startActivityForResult(intent, 101);
        }

    }

    private void sum() {
        float sum = 0;
        for (SaleInfo info : mList) {
            if (info.materialInfo == null) continue;
            sum += info.realPrice * info.number;
        }
        mSum.setText(sum + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == 100) {
            SaleInfo info = new SaleInfo();
            info.materialInfo = Application.mTempInfo;
            info.realPrice = Application.mTempInfo.price;
            mList.add(info);
            Log.d("big", "add");
            mAdapter.notifyDataSetChanged();
            sum();
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
                holder.total.setText(saleInfo.realPrice * saleInfo.number + "");
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
            TextView name, price, provide, size, del, total, realPrice, num;

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
                                sum();
                                notifyDataSetChanged();
                            }
                        }
                    });
                }
                if (realPrice instanceof EditText) {
                    realPrice.addTextChangedListener(new SimpleTextWatch() {
                        @Override
                        public void afterTextChanged(Editable editable) {
                            int position = getAdapterPosition();
                            if (position > 0 && position < mList.size()) {
                                SaleInfo info = mList.get(position);
                                if (TextUtils.isEmpty(realPrice.getText())) {
                                    info.realPrice = 0;
                                } else {
                                    info.realPrice = Float.parseFloat(realPrice.getText().toString());
                                }
                                total.setText(info.realPrice * info.number + "");
                                sum();
                            }
                        }
                    });
                }
                if (num instanceof EditText) {
                    num.addTextChangedListener(new SimpleTextWatch() {
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            int position = getAdapterPosition();
                            if (position > 0 && position < mList.size()) {
                                SaleInfo info = mList.get(position);
                                if (TextUtils.isEmpty(charSequence)) {
                                    info.number = 0;
                                } else {
                                    info.number = Integer.parseInt(charSequence.toString());
                                }
                                total.setText(info.realPrice * info.number + "");
                                sum();
                            }
                        }
                    });
                }

            }

        }
    }
}
