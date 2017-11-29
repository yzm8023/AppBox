// IBinderDelegateService.aidl
package com.smonline.virtual.server;

import android.content.ComponentName;

interface IBinderDelegateService {

   ComponentName getComponent();

   IBinder getService();

}
