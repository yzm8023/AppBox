// IDeviceInfoManager.aidl
package com.smonline.virtual.server;

import com.smonline.virtual.remote.VDeviceInfo;

interface IDeviceInfoManager {

    VDeviceInfo getDeviceInfo(int userId);

    void updateDeviceInfo(int userId, in VDeviceInfo info);

}
