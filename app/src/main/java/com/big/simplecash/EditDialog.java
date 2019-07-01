package com.big.simplecash;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.big.simplecash.greendao.GreenDaoUtils;
import com.big.simplecash.greendao.Order;
import com.big.simplecash.greendao.SaleInfo;
import com.big.simplecash.util.CallBack;
import com.big.simplecash.util.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by big on 2019/6/12.
 */

public class EditDialog extends Dialog implements View.OnClickListener {
    public EditDialog(@NonNull Context context) {
        super(context, R.style.input_dialog);
        init();
    }

    private TextView mInput;

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.dialog_edit, null);
        setContentView(contentView, new WindowManager.LayoutParams(-1, -1));
        getWindow().setLayout(-1, -1);
        mInput = (TextView) contentView.findViewById(R.id.input_content);
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    private Order mOrder;
    CallBack<Order> mCallBack;

    public void setCallback(CallBack<Order> callback) {
        mCallBack = callback;
    }

    public void show(Order order) {
        super.show();
        if (mInput != null) {
            mInput.setText(order.name);
            if (mCallBack != null) {
                mCallBack.onCallBack(null);
            }
        }
        mOrder = order;
    }

    @Override
    public void onClick(View view) {
        dismiss();
        if (view.getId() == R.id.confirm) {
            if (!TextUtils.isEmpty(mInput.getText())) {
                mOrder.name = mInput.getText().toString();
            }
        }
    }
}
