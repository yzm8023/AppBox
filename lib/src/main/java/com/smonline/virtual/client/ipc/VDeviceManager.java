package com.smonline.virtual.client.ipc;

import android.os.RemoteException;

import com.smonline.virtual.client.env.VirtualRuntime;
import com.smonline.virtual.helper.ipcbus.IPCSingleton;
import com.smonline.virtual.remote.VDeviceInfo;
import com.smonline.virtual.server.interfaces.IDeviceInfoManager;

/**
 * @author Lody
 */

public class VDeviceManager {

    private static final VDeviceManager sInstance = new VDeviceManager();
    private IPCSingleton<IDeviceInfoManager> singleton = new IPCSingleton<>(IDeviceInfoManager.class);


    public static VDeviceManager get() {
        return sInstance;
    }


    public IDeviceInfoManager getService() {
        return singleton.get();
    }

    public VDeviceInfo getDeviceInfo(int userId) {
        try {
            return getService().getDeviceInfo(userId);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }
}