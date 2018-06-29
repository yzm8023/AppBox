package com.smonline.appbox.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.smonline.appbox.R;
import com.smonline.virtual.client.core.VirtualCore;
import com.smonline.virtual.client.ipc.VActivityManager;
import com.smonline.virtual.remote.InstalledAppInfo;

/**
 * Created by yzm on 18-6-29.
 */

public class LoadingActivity extends AppCompatActivity{

    private static final String KEY_PKG_NAME = "KEY_PKG_NAME";
    private static final String KEY_INTENT = "KEY_INTENT";
    private static final String KEY_USER = "KEY_USER";

    public static void launchApp(Context context, AppInfo appInfo, int userId){
        Intent intent = VirtualCore.get().getLaunchIntent(appInfo.getPackageName(), userId);
        if (intent != null) {
            if(VActivityManager.get().isAppRunning(appInfo.getPackageName(), userId)){
                VActivityManager.get().startActivity(intent, userId);
            }else {
                Intent loadingPageIntent = new Intent(context, LoadingActivity.class);
                loadingPageIntent.putExtra(KEY_PKG_NAME, appInfo.getPackageName());
                loadingPageIntent.putExtra(KEY_INTENT, intent);
                loadingPageIntent.putExtra(KEY_USER, userId);
                loadingPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(loadingPageIntent);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Intent intent = getIntent();
        String pkg = intent.getStringExtra(KEY_PKG_NAME);
        final int userId = intent.getIntExtra(KEY_USER, 0);
        final Intent targetIntent = intent.getParcelableExtra(KEY_INTENT);
        InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(pkg, 0);
        Drawable appIcon = installedAppInfo.getApplicationInfo(0).loadIcon(VirtualCore.getPM());
        CharSequence appName = installedAppInfo.getApplicationInfo(0).loadLabel(VirtualCore.getPM());
        ImageView appIconImg = (ImageView)findViewById(R.id.launching_app_icon);
        TextView appNameTxt = (TextView)findViewById(R.id.launching_app_name);
        appIconImg.setImageDrawable(appIcon);
        appNameTxt.setText(String.format(getString(R.string.launching_app), appName));
        appIconImg.postDelayed(new Runnable() {
            @Override
            public void run() {
                VActivityManager.get().startActivity(targetIntent, userId);
            }
        }, 500);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
