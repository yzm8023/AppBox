package com.smonline.appbox.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;

import com.smonline.appbox.R;
import com.smonline.appbox.utils.ABoxLog;
import com.smonline.virtual.client.core.VirtualCore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yzm on 17-11-30.
 */

public class CloneAppActivity extends BaseActivity {

    private static final String TAG = CloneAppActivity.class.getSimpleName();

    @BindView(R.id.clone_app_recycler_view)
    RecyclerView mRecyclerView;

    private PackageManager mPackageManager;
    private AppInfoAdapter mAppInfoAdapter;
    private List<AppInfo> mAllAppInfos = new ArrayList<AppInfo>();

    public static void gotoClone(Context context){
        Intent intent = new Intent(context, CloneAppActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setContentLayout() {
        return R.layout.activity_clone_app;
    }

    @Override
    public void onActivityCreate() {
        mPackageManager = VirtualCore.get().getUnHookPackageManager();
        mAppInfoAdapter = new AppInfoAdapter(mContext);
        mAppInfoAdapter.setOnItemClickListener(mItemClickListener);

        List<ApplicationInfo> apps = mPackageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        ABoxLog.d(TAG, "@onActivityCreate, apps size = " + apps.size());
        for(ApplicationInfo info : apps){
            AppInfo appInfo = new AppInfo();
            appInfo.setAppIcon(info.loadIcon(mPackageManager));
            appInfo.setAppName(info.loadLabel(mPackageManager).toString());
            try{
                PackageInfo pkgInfo = mPackageManager.getPackageInfo(info.packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                appInfo.setAppVersion(pkgInfo.versionName);
            }catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }
            appInfo.setPackageName(info.packageName);
            appInfo.setApkPath(info.sourceDir);
            appInfo.setUserApp(filterApp(info));
            mAllAppInfos.add(appInfo);
        }
        ABoxLog.d(TAG, "@onActivityCreate, mAllAppInfos size = " + mAllAppInfos.size());
        mAppInfoAdapter.setInstalledApps(mAllAppInfos);
        mRecyclerView.setAdapter(mAppInfoAdapter);
    }


    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int posoition, AppInfo appInfo) {
            ABoxLog.d(TAG, "@onItemClick2222222, app = " + appInfo.getPackageName());
        }
    };

    //判断应用程序是否是用户程序
    public boolean filterApp(ApplicationInfo info) {
        //原来是系统应用，用户手动升级
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //用户自己安装的应用程序
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }

}
