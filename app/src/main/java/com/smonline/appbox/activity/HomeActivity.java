package com.smonline.appbox.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity{

    private static final String TAG = HomeActivity.class.getSimpleName();

    @BindView(R.id.btn_goto_clone)
    ImageView mGotoInstallImg;
    @BindView(R.id.tip_app_empty)
    TextView mAppEmptyTip;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Dialog mLoadingDialog;
    private PackageManager mPackageManager;
    private AppInfoAdapter mAppInfoAdapter;
    private List<AppInfo> mInstalledAppInfos = new ArrayList<AppInfo>();

    public static void goHome(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onActivityCreate() {
        mPackageManager = VirtualCore.get().getUnHookPackageManager();
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mAppInfoAdapter = new AppInfoAdapter(mContext);
        mAppInfoAdapter.setOnItemClickListener(mItemClickListener);
        mRecyclerView.addItemDecoration(new MyItemDecoration());
        mRecyclerView.setAdapter(mAppInfoAdapter);

        loadInstalledApps();

        if(mInstalledAppInfos.size() > 0){
            mAppInfoAdapter.setInstalledApps(mInstalledAppInfos);
            mAppEmptyTip.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }else {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(1, 1, 1, 1);
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
                CloneAppActivity.gotoClone(this);
                break;
            default:
                break;
        }
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View v, int posoition, final AppInfo appInfo, boolean isMenuIcon) {
            if(isMenuIcon){
                View contentLayout = LayoutInflater.from(mContext).inflate(R.layout.popup_menu_layout,null);
                final  PopupWindow popupWindow = new PopupWindow(contentLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView uninstallText = (TextView) contentLayout.findViewById(R.id.menu_uninstall);
                uninstallText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(VirtualCore.get().uninstallPackage(appInfo.getPackageName())){
                            ListIterator<AppInfo> iterator = mInstalledAppInfos.listIterator();
                            while (iterator.hasNext()){
                                AppInfo info = iterator.next();
                                if(info != null && info.getPackageName().equals(appInfo.getPackageName())){
                                    iterator.remove();
                                    break;
                                }
                            }
                            mAppInfoAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(mContext, mContext.getText(R.string.toast_tip_uninstall_failed), Toast.LENGTH_SHORT).show();
                        }
                        popupWindow.dismiss();
                    }
                });
                popupWindow.setContentView(contentLayout);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(v);
            }else {
                Intent intent = VirtualCore.get().getLaunchIntent(appInfo.getPackageName(), 0);
                VActivityManager.get().startActivity(intent, 0);
            }
        }
    };

    private Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_importing, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        return loadingDialog;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final String appName = intent.getStringExtra("app_name");
        final String apkPath = intent.getStringExtra("apk_path");
        if(TextUtils.isEmpty(apkPath)) return;
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mLoadingDialog = createLoadingDialog(mContext, "正在安装 : " + appName);
                mLoadingDialog.show();
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
                mAppEmptyTip.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mAppInfoAdapter.setInstalledApps(mInstalledAppInfos);
                mAppInfoAdapter.notifyDataSetChanged();
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        }.execute();
    }

    public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.ViewHolder>{

        private Context mContext;
        private List<AppInfo> mApps;
        private OnItemClickListener mListener;

        public AppInfoAdapter(Context context){
            this.mContext = context;
        }

        public void setInstalledApps(List<AppInfo> apps){
            this.mApps = apps;
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.mListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.app_repo_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AppInfo appInfo = mApps.get(position);
            if(appInfo != null){
                Drawable icon = appInfo.getAppIcon();
                CharSequence title = appInfo.getAppName();
                holder.appIcon.setImageDrawable(icon);
                holder.appTitle.setText(title);
            }
        }

        @Override
        public int getItemCount() {
            return mApps != null ? mApps.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageView appIcon;
            TextView appTitle;
            ImageView menuIcon;

            public ViewHolder(View viewItem){
                super(viewItem);
                appIcon = (ImageView) viewItem.findViewById(R.id.app_icon_img);
                appTitle = (TextView) viewItem.findViewById(R.id.app_title_txt);
                menuIcon = (ImageView) viewItem.findViewById(R.id.menu_icon);
                menuIcon.setOnClickListener(this);
                viewItem.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                boolean isMenuIcon = false;
                if(v.getId() == R.id.menu_icon)
                    isMenuIcon = true;
                mListener.onItemClick(v, getLayoutPosition(), mApps.get(getLayoutPosition()), isMenuIcon);
            }
        }
    }
}
