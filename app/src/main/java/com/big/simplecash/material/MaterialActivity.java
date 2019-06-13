package com.big.simplecash.material;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.big.simplecash.AddActivity;
import com.big.simplecash.Application;
import com.big.simplecash.BaseActivity;
import com.big.simplecash.R;
import com.big.simplecash.greendao.GreenDaoUtils;
import com.big.simplecash.greendao.MaterialInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by big on 2019/6/10.
 */

public class MaterialActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private EditText mSearchEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        findViewById(R.id.add).setOnClickListener(this);
        mSearchEdit = findViewById(R.id.search_name);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(GreenDaoUtils.getAllRecord());
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(mSearchEdit.getText())) {
                    mAdapter.setData(GreenDaoUtils.getAllRecord());
                } else {
                    mAdapter.setData(GreenDaoUtils.getMaterialInfoByName(mSearchEdit.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 100) {
            Log.d("big", "result");
            if (TextUtils.isEmpty(mSearchEdit.getText())) {
                mAdapter.setData(GreenDaoUtils.getAllRecord());
            } else {
                mAdapter.setData(GreenDaoUtils.getMaterialInfoByName(mSearchEdit.getText().toString()));
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        List<MaterialInfo> mList = new ArrayList<>();

        public void setData(List<MaterialInfo> list) {
            if (list != null) {
                mList = list;
                mList.add(0, new MaterialInfo());
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
            MaterialInfo info = mList.get(position);
            if (position % 2 != 0) {
                holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_item_second_sel));
            } else {
                holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_item_sel));
            }
            if (position != 0) {
                holder.name.setText(info.name);
                holder.size.setText(info.size);
                holder.price.setText(String.valueOf(info.price));
                holder.provide.setText(info.provider);
            }
            Log.d("big", "id:" + info.id);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return R.layout.item_material_title;
            }
            return R.layout.item_material;
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView name, price, provide, size, del;

            public MyHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.item_name);
                size = itemView.findViewById(R.id.item_size);
                price = itemView.findViewById(R.id.item_price);
                provide = itemView.findViewById(R.id.item_provider);
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
                        } else if ("sale".equals(getIntent().getStringExtra("from"))) {
                            Application.mTempInfo = mList.get(getAdapterPosition());
                            setResult(100);
                            finish();
                        }
                    }
                });
                if (del != null) {
                    del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = getAdapterPosition();
                            if (position > 0 && position < mList.size()) {
                                del.setVisibility(View.GONE);
                                MaterialInfo info = mList.get(position);
                                GreenDaoUtils.deleteMaterialInfo(info);
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
