package com.big.simplecash.material;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.big.simplecash.BaseActivity;
import com.big.simplecash.R;
import com.big.simplecash.greendao.MaterialInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by big on 2019/6/10.
 */

public class MaterialActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        List<MaterialInfo> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new MaterialInfo());
        }
        mAdapter.setData(list);
    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        List<MaterialInfo> mList = new ArrayList<>();

        public void setData(List<MaterialInfo> list) {
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View content = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new MyHolder(content);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            if (position % 2 != 0) {
                holder.itemView.setBackgroundColor(0xffdddddd);
            } else {
                holder.itemView.setBackgroundColor(0xffffffff);
            }

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(position==0){
                return R.layout.item_material_title;
            }
        return R.layout.item_material;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView name, price, provide;

        public MyHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
            provide = itemView.findViewById(R.id.item_provider);
        }
    }
}
