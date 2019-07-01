package com.big.simplecash;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.big.simplecash.util.CallBack;
import com.big.simplecash.util.Utils;


/**
 * Created by big on 2019/6/12.
 */

public class SingleEditDialog extends Dialog implements View.OnClickListener {
    public SingleEditDialog(@NonNull Context context) {
        super(context, R.style.input_dialog);
        init();
    }

    private TextView mInput, mName;

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.dialog_single_edit, null);
        setContentView(contentView, new WindowManager.LayoutParams(-1, -1));
        getWindow().setLayout(-1, -1);
        mInput = (TextView) contentView.findViewById(R.id.input_content);
        mName = (TextView) contentView.findViewById(R.id.name);
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    private CallBack<Float> mCallBack;

    public void setCallback(CallBack<Float> callback) {
        mCallBack = callback;
    }

    private TextView mTextView;

    public void show(TextView textView, String name, String value) {
        super.show();
        mInput.setText(value);
        mName.setText(name);
        mTextView = textView;
    }

    @Override
    public void onClick(View view) {
        dismiss();
        if (view.getId() == R.id.confirm) {
            mTextView.setText(String.valueOf(mInput.getText()));
            if (mCallBack != null) {
                mCallBack.onCallBack(Utils.getTextFloat(mInput));
            }
        }
    }
}
