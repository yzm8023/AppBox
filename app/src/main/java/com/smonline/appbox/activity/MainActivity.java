package com.smonline.appbox.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smonline.appbox.R;
import com.smonline.appbox.utils.ABoxLog;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.btn) Button mBtn;

    @Override
    public int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                ABoxLog.d(TAG, "onClick");
                break;
            default:
                break;
        }
    }
}
