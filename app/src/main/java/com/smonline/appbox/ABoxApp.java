package com.smonline.appbox;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.smonline.appbox.delegate.MyTaskDescriptionDelegate;
import com.smonline.virtual.client.core.VirtualCore;
import com.smonline.virtual.client.stub.VASettings;

import jonathanfinerty.once.Once;

/**
 * Created by yzm on 17-11-29.
 */

public class ABoxApp extends MultiDexApplication {

    public static String APP_NAME;

    @Override
    public void onCreate() {
        super.onCreate();
        final VirtualCore virtualCore = VirtualCore.get();
        virtualCore.initialize(new VirtualCore.VirtualInitializer() {
            @Override
            public void onMainProcess() {
                Once.initialise(ABoxApp.this);
            }

            @Override
            public void onVirtualProcess() {
                //fake task description's icon and title
                virtualCore.setTaskDescriptionDelegate(new MyTaskDescriptionDelegate());
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
