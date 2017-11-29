package com.smonline.virtual.client.hook.proxies.wifi_scanner;

import com.smonline.virtual.client.hook.base.BinderInvocationProxy;

/**
 * @author Lody
 */

public class WifiScannerStub extends BinderInvocationProxy {

    public WifiScannerStub() {
        super(new GhostWifiScannerImpl(), "wifiscanner");
    }

}
