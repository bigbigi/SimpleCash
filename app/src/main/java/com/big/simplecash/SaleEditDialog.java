package com.big.simplecash;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.big.simplecash.greendao.SaleInfo;
import com.big.simplecash.util.CallBack;
import com.big.simplecash.util.Utils;


/**
 * Created by big on 2019/6/12.
 */

public class SaleEditDialog extends Dialog implements View.OnClickListener {
    public SaleEditDialog(@NonNull Context context) {
        super(context, R.style.input_dialog);
        init();
    }

    private TextView mPrice, mNum, mName;

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.dialog_order_edit, null);
        setContentView(contentView, new WindowManager.LayoutParams(-1, -1));
        getWindow().setLayout(-1, -1);
        mName = (TextView) contentView.findViewById(R.id.name_content);
        mPrice = (TextView) contentView.findViewById(R.id.price_content);
        mNum = (TextView) contentView.findViewById(R.id.num_content);
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    private SaleInfo mSaleInfo;
    private CallBack<SaleInfo> mCallBack;

    public void setCallback(CallBack<SaleInfo> callback) {
        mCallBack = callback;
    }

    public void show(SaleInfo info) {
        super.show();
        mName.setText(String.valueOf(info.name));
        mPrice.setText(String.valueOf(info.realPrice));
        mNum.setText(String.valueOf(info.number));
        mSaleInfo = info;
    }

    @Override
    public void onClick(View view) {
        dismiss();
        if (view.getId() == R.id.confirm) {
            mSaleInfo.name = String.valueOf(mName.getText());
            mSaleInfo.realPrice = Utils.getTextFloat(mPrice);
            mSaleInfo.number = Utils.getTextInt(mNum);
            if (mCallBack != null) {
                mCallBack.onCallBack(mSaleInfo);
            }
        }
    }
}
