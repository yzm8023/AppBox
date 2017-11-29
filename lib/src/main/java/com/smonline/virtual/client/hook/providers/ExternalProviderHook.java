package com.smonline.virtual.client.hook.providers;

import com.smonline.virtual.client.core.VirtualCore;

import java.lang.reflect.Method;

/**
 * @author Lody
 */

public class ExternalProviderHook extends ProviderHook {

    public ExternalProviderHook(Object base) {
        super(base);
    }

    @Override
    protected void processArgs(Method method, Object... args) {
        if (args != null && args.length > 0 && args[0] instanceof String) {
            String pkg = (String) args[0];
            if (VirtualCore.get().isAppInstalled(pkg)) {
                args[0] = VirtualCore.get().getHostPkg();
            }
        }
    }
}
