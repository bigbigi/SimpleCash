package com.big.simplecash;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.big.simplecash.greendao.Order;
import com.big.simplecash.util.Base64;
import com.big.simplecash.util.CallBack;


/**
 * Created by big on 2019/6/12.
 */

public class InputDialog extends Dialog implements View.OnClickListener {
    public InputDialog(@NonNull Context context) {
        super(context, R.style.input_dialog);
        init();
    }

    private TextView mInput;

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.dialog_input, null);
        setContentView(contentView, new WindowManager.LayoutParams(-1, -1));
        getWindow().setLayout(-1, -1);
        mInput = contentView.findViewById(R.id.input_content);
        findViewById(R.id.confirm).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
    }

    CallBack<Order> mCallBack;

    public void setCallback(CallBack<Order> callback) {
        mCallBack = callback;
    }

    @Override
    public void onClick(View view) {
        dismiss();
        if(view.getId()==R.id.confirm){
            if (!TextUtils.isEmpty(mInput.getText())) {
                Order order = Order.intPut(Base64.decode(String.valueOf(mInput.getText())));
                if (mCallBack != null) {
                    mCallBack.onCallBack(order);
                }
            }
        }
    }
}
