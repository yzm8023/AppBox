// IAppManager.aidl
package com.smonline.virtual.server;

import com.smonline.virtual.server.interfaces.IPackageObserver;
import com.smonline.virtual.server.interfaces.IAppRequestListener;
import com.smonline.virtual.remote.InstalledAppInfo;
import com.smonline.virtual.remote.InstallResult;

interface IAppManager {
    int[] getPackageInstalledUsers(String packageName);
    void scanApps();
    void addVisibleOutsidePackage(String pkg);
    void removeVisibleOutsidePackage(String pkg);
    boolean isOutsidePackageVisible(String pkg);
    InstalledAppInfo getInstalledAppInfo(String pkg, int flags);
    InstallResult installPackage(String path, int flags);
    boolean isPackageLaunched(int userId, String packageName);
    void setPackageHidden(int userId, String packageName, boolean hidden);
    boolean installPackageAsUser(int userId, String packageName);
    boolean uninstallPackageAsUser(String packageName, int userId);
    boolean uninstallPackage(String packageName);
    List<InstalledAppInfo> getInstalledApps(int flags);
    List<InstalledAppInfo> getInstalledAppsAsUser(int userId, int flags);
    int getInstalledAppCount();
    boolean isAppInstalled(String packageName);
    boolean isAppInstalledAsUser(int userId, String packageName);

    void registerObserver(IPackageObserver observer);
    void unregisterObserver(IPackageObserver observer);

    void setAppRequestListener(IAppRequestListener listener);
    void clearAppRequestListener();
    IAppRequestListener getAppRequestListener();

}
