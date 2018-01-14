package com.smonline.appbox.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
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

    private static final int MSG_SHOW_LIST = 100;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_SHOW_LIST:
                    mAppInfoAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

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
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mAppInfoAdapter = new AppInfoAdapter(mContext);
        mAppInfoAdapter.setOnItemClickListener(mItemClickListener);
        mRecyclerView.setAdapter(mAppInfoAdapter);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                List<ApplicationInfo> apps = mPackageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
                for(ApplicationInfo info : apps){
                    if(isSystemApp(info)){
                        continue;
                    }
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
                    mAllAppInfos.add(appInfo);
                    ABoxLog.d(TAG, "@onActivityCreate, appInfo = " + appInfo.toString());
                }
                ABoxLog.d(TAG, "@onActivityCreate, mAllAppInfos size = " + mAllAppInfos.size());
                mAppInfoAdapter.setInstalledApps(mAllAppInfos);
                mHandler.sendEmptyMessage(MSG_SHOW_LIST);
                return null;
            }
        }.execute();
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int posoition, AppInfo appInfo) {
            ABoxLog.d(TAG, "@onItemClick, app = " + appInfo.getPackageName());
            Intent retIntent = new Intent(CloneAppActivity.this, HomeActivity.class);
            retIntent.putExtra("app_name", appInfo.getAppName());
            retIntent.putExtra("apk_path", appInfo.getApkPath());
            startActivity(retIntent);
            CloneAppActivity.this.finish();
        }
    };

    public boolean isSystemApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            return true;
        }
        return false;
    }

}
