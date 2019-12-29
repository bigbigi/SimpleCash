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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MaterialHistory extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private TextView mTitleTxt;
    private List<SaleHistoryInfo> mList;

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
        mList = GreenDaoUtils.getHistorys(name, provider, size);
        mAdapter.setData(mList);
        mTitleTxt.setText(name);
        findViewById(R.id.date).setOnClickListener(this);
        findViewById(R.id.provider).setOnClickListener(this);
        findViewById(R.id.size).setOnClickListener(this);
        findViewById(R.id.price).setOnClickListener(this);
        findViewById(R.id.salePrice).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (mList == null || mList.isEmpty()) return;
        if (v.getId() == R.id.date) {
            Collections.sort(mList, mTimeComparetor);
        } else if (v.getId() == R.id.provider) {
            Collections.sort(mList, mProviderComparetor);
        } else if (v.getId() == R.id.size) {
            Collections.sort(mList, mSizeComparetor);
        } else if (v.getId() == R.id.price) {
            Collections.sort(mList, mPriceComparetor);
        } else if (v.getId() == R.id.salePrice) {
            Collections.sort(mList, mSaleComparetor);
        }
        mAdapter.notifyDataSetChanged();
        REVER = !REVER;
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

    private static boolean REVER = false;
    public static Comparator mTimeComparetor = new Comparator<SaleHistoryInfo>() {
        @Override
        public int compare(SaleHistoryInfo o1, SaleHistoryInfo o2) {
            if (o1.createTime < o2.createTime) {
                return REVER ? 1 : -1;
            } else if (o1.createTime > o2.createTime) {
                return REVER ? -1 : 1;
            } else {
                return 0;
            }
        }
    };
    public static Comparator mProviderComparetor = new Comparator<SaleHistoryInfo>() {
        @Override
        public int compare(SaleHistoryInfo o1, SaleHistoryInfo o2) {
            if (o1.provider.compareTo(o2.provider) < 0) {
                return REVER ? 1 : -1;
            } else if (o1.provider.compareTo(o2.provider) > 0) {
                return REVER ? -1 : 1;
            } else {
                return 0;
            }
        }
    };

    public static Comparator mSizeComparetor = new Comparator<SaleHistoryInfo>() {
        @Override
        public int compare(SaleHistoryInfo o1, SaleHistoryInfo o2) {
            if (o1.size.compareTo(o2.size) < 0) {
                return REVER ? 1 : -1;
            } else if (o1.size.compareTo(o2.size) > 0) {
                return REVER ? -1 : 1;
            } else {
                return 0;
            }
        }
    };
    public static Comparator mPriceComparetor = new Comparator<SaleHistoryInfo>() {
        @Override
        public int compare(SaleHistoryInfo o1, SaleHistoryInfo o2) {
            if (o1.realPrice < o2.realPrice) {
                return REVER ? 1 : -1;
            } else if (o1.realPrice > o2.realPrice) {
                return REVER ? -1 : 1;
            } else {
                return 0;
            }
        }
    };
    public static Comparator mSaleComparetor = new Comparator<SaleHistoryInfo>() {
        @Override
        public int compare(SaleHistoryInfo o1, SaleHistoryInfo o2) {
            if (o1.salePrice < o2.salePrice) {
                return REVER ? 1 : -1;
            } else if (o1.salePrice > o2.salePrice) {
                return REVER ? -1 : 1;
            } else {
                return 0;
            }
        }
    };
}
