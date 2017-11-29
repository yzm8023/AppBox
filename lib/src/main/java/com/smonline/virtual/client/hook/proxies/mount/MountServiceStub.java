package com.smonline.virtual.client.hook.proxies.mount;

import android.os.IInterface;

import com.smonline.virtual.client.hook.base.Inject;
import com.smonline.virtual.client.hook.base.BinderInvocationProxy;
import com.smonline.virtual.helper.compat.BuildCompat;

import mirror.RefStaticMethod;
import mirror.android.os.mount.IMountService;
import mirror.android.os.storage.IStorageManager;

/**
 * @author Lody
 */
@Inject(MethodProxies.class)
public class MountServiceStub extends BinderInvocationProxy {

    public MountServiceStub() {
        super(getInterfaceMethod(), "mount");
    }

    private static RefStaticMethod<IInterface> getInterfaceMethod() {
        if (BuildCompat.isOreo()) {
            return IStorageManager.Stub.asInterface;
        } else {
            return IMountService.Stub.asInterface;
        }
    }
}
