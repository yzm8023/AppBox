package com.smonline.virtual.server.interfaces;

import android.os.RemoteException;

import com.smonline.virtual.remote.VDeviceInfo;

/**
 * @author Lody
 */
public interface IDeviceInfoManager {

    VDeviceInfo getDeviceInfo(int userId) throws RemoteException;

    void updateDeviceInfo(int userId, VDeviceInfo info) throws RemoteException;

}
