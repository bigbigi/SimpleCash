package com.big.simplecash;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;

import com.big.simplecash.util.CallBack;


/**
 * Created by big on 2019/6/12.
 */

public class OutputDialog extends Dialog implements View.OnClickListener {
    public OutputDialog(@NonNull Context context) {
        super(context, R.style.input_dialog);
        init();
    }


    private void init() {
        View contentView = View.inflate(getContext(), R.layout.dialog_ouput, null);
        setContentView(contentView, new WindowManager.LayoutParams(-1, -1));
        getWindow().setLayout(-1, -1);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.excel).setOnClickListener(this);
        findViewById(R.id.data).setOnClickListener(this);
    }

    private CallBack<View> mCallBack;

    public void setCallback(CallBack<View> callback) {
        mCallBack = callback;
    }

    @Override
    public void onClick(View view) {
        dismiss();
        if (mCallBack != null) {
            mCallBack.onCallBack(view);
        }
    }
}
