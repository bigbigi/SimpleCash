package com.big.simplecash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.big.simplecash.material.MaterialActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.item_1).setOnClickListener(this);
        findViewById(R.id.item_2).setOnClickListener(this);
        findViewById(R.id.item_3).setOnClickListener(this);
        findViewById(R.id.item_4).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item_1) {
            startActivity(new Intent(this, SaleActivity.class));
        } else if (view.getId() == R.id.item_2) {
            startActivity(new Intent(this, MaterialActivity.class));
        } else if (view.getId() == R.id.item_3) {
            startActivity(new Intent(this, OrderListActivity.class));
        }
    }
}
