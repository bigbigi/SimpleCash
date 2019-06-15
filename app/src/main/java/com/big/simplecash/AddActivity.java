package com.big.simplecash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.big.simplecash.greendao.GreenDaoUtils;
import com.big.simplecash.greendao.MaterialInfo;

/**
 * Created by big on 2019/6/11.
 */

public class AddActivity extends BaseActivity implements View.OnClickListener {

    EditText mName, mSize, mPrice, mProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        findViewById(R.id.confirm).setOnClickListener(this);
        mName = (EditText) findViewById(R.id.name);
        mSize = (EditText) findViewById(R.id.size);
        mPrice = (EditText) findViewById(R.id.price);
        mProvider = (EditText) findViewById(R.id.provider);
    }

    @Override
    public void onClick(View view) {
        if (isEmpty(mName) || isEmpty(mSize) || isEmpty(mPrice) || isEmpty(mProvider)) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        save();
        setResult(100);
        finish();
    }

    boolean isEmpty(EditText edit) {
        return TextUtils.isEmpty(edit.getText());
    }

    private void save() {
        MaterialInfo info = new MaterialInfo();
        info.name = mName.getText().toString();
        info.size = mSize.getText().toString();
        info.price = Float.parseFloat(mPrice.getText().toString());
        info.provider = mProvider.getText().toString();
        GreenDaoUtils.insertMaterialInfo(info);
    }
}
