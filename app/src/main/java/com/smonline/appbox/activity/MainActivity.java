package com.smonline.appbox.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smonline.appbox.R;
import com.smonline.appbox.utils.ABoxLog;
import com.smonline.virtual.client.core.InstallStrategy;
import com.smonline.virtual.client.core.VirtualCore;
import com.smonline.virtual.client.ipc.VActivityManager;
import com.smonline.virtual.remote.InstallResult;
import com.smonline.virtual.remote.InstalledAppInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.btn_goto_clone)
    Button mGotoInstallBtn;
    @BindView(R.id.tip_app_empty)
    TextView mAppEmptyTip;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private PackageManager mPackageManager;
    private AppInfoAdapter mAppInfoAdapter;
    private List<AppInfo> mInstalledAppInfos = new ArrayList<AppInfo>();

    private static final int MSG_REFRESG_LIST = 100;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_REFRESG_LIST:
                    mAppInfoAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onActivityCreate() {
        mPackageManager = VirtualCore.get().getUnHookPackageManager();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAppInfoAdapter = new AppInfoAdapter(mContext);
        mAppInfoAdapter.setOnItemClickListener(mItemClickListener);

        loadInstalledApps();

        if(mInstalledAppInfos.size() > 0){
            mAppInfoAdapter.setInstalledApps(mInstalledAppInfos);
            mAppEmptyTip.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(mAppInfoAdapter);
        }else {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void loadInstalledApps(){
        mInstalledAppInfos.clear();
        List<InstalledAppInfo> installedApps = VirtualCore.get().getInstalledApps(0);
        for(InstalledAppInfo info : installedApps){
            AppInfo appInfo = new AppInfo();
            appInfo.setAppIcon(info.getApplicationInfo(0).loadIcon(mPackageManager));
            appInfo.setAppName(info.getApplicationInfo(0).loadLabel(mPackageManager).toString());
            appInfo.setAppVersion(info.getPackageInfo(0).versionName);
            appInfo.setPackageName(info.packageName);
            appInfo.setApkPath(info.apkPath);
            mInstalledAppInfos.add(appInfo);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.btn_goto_clone})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_goto_clone:
                Intent intent = new Intent(mContext, CloneAppActivity.class);
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int posoition, AppInfo appInfo) {
            ABoxLog.d(TAG, "@onItemClick, app = " + appInfo.getPackageName());
            Intent intent = VirtualCore.get().getLaunchIntent(appInfo.getPackageName(), 0);
            ABoxLog.d(TAG, "@onItemClick, intent = " + intent.toString());
            VActivityManager.get().startActivity(intent, 0);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200){
            final String appName = data.getStringExtra("app_name");
            final String apkPath = data.getStringExtra("apk_path");
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    Toast.makeText(mContext, "正在安装 : " + appName, Toast.LENGTH_SHORT).show();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    int flags = InstallStrategy.COMPARE_VERSION | InstallStrategy.SKIP_DEX_OPT | InstallStrategy.DEPEND_SYSTEM_IF_EXIST;
                    InstallResult installResult = VirtualCore.get().installPackage(apkPath, flags);
                    ABoxLog.d(TAG, "installResult = " + installResult.isSuccess);
                    if(installResult.isSuccess){
                        loadInstalledApps();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Toast.makeText(mContext, appName + "已安装", Toast.LENGTH_SHORT).show();
                    mAppInfoAdapter.setInstalledApps(mInstalledAppInfos);
                    mAppInfoAdapter.notifyDataSetChanged();
                }
            }.execute();
        }
    }
}
