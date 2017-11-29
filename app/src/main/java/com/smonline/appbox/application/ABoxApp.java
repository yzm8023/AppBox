package com.smonline.appbox.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.smonline.virtual.client.core.VirtualCore;
import com.smonline.virtual.client.stub.VASettings;

/**
 * Created by yzm on 17-11-29.
 */

public class ABoxApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        VirtualCore virtualCore = VirtualCore.get();
        virtualCore.initialize(new VirtualCore.VirtualInitializer() {
            @Override
            public void onMainProcess() {
                super.onMainProcess();
            }

            @Override
            public void onVirtualProcess() {
                super.onVirtualProcess();
            }

            @Override
            public void onServerProcess() {
                super.onServerProcess();
            }

            @Override
            public void onChildProcess() {
                super.onChildProcess();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        VASettings.ENABLE_IO_REDIRECT = true;
        VASettings.ENABLE_INNER_SHORTCUT = true;
        try{
            VirtualCore.get().startup(base);
        }catch (Throwable e){
            e.printStackTrace();
        }

    }
}
