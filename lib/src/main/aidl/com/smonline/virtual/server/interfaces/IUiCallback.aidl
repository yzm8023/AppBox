// IUiCallback.aidl
package com.smonline.virtual.server.interfaces;

interface IUiCallback {
    void onAppOpened(in String packageName, in int userId);
}
