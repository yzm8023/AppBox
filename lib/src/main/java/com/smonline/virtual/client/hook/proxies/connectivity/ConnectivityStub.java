package com.smonline.virtual.client.hook.proxies.connectivity;

import android.content.Context;

import com.smonline.virtual.client.hook.base.BinderInvocationProxy;
import com.smonline.virtual.client.hook.base.MethodProxy;
import com.smonline.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.smonline.virtual.client.hook.base.StaticMethodProxy;
import com.smonline.virtual.client.ipc.ServiceManagerNative;

import java.lang.reflect.Method;

import mirror.android.net.IConnectivityManager;

/**
 * @author legency
 */
public class ConnectivityStub extends BinderInvocationProxy {

    public ConnectivityStub() {
        super(IConnectivityManager.Stub.asInterface, Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
    }
}
