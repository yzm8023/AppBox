package com.smonline.appbox.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yzm on 17-11-30.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentLayout());
        mContext = this;
        mUnbinder = ButterKnife.bind(this);
        onActivityCreate();
    }

    public abstract int setContentLayout();

    public abstract void onActivityCreate();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
    }
}
