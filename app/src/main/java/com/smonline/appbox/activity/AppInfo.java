package com.smonline.appbox.activity;

/**
 * Created by yzm on 17-12-1.
 */

import android.graphics.drawable.Drawable;

/**
 * 获取的应用基本信息实体类
 * @author liuyazhuang
 *
 */
public class AppInfo {
    //图标
    private Drawable appIcon;
    //应用名称
    private String appName;
    //应用版本号
    private String appVersion;
    //应用包名
    private String packageName;
    //apk路径
    private String apkPath;


    public AppInfo() {
        super();
        // TODO Auto-generated constructor stub
    }
    public AppInfo(Drawable app_icon, String app_name, String app_version,
                   String packagename) {
        super();
        this.appIcon = app_icon;
        this.appName = app_name;
        this.appVersion = app_version;
        this.packageName = packagename;
    }


    public AppInfo(Drawable app_icon, String app_name, String app_version,
                   String packagename, String apkPath) {
        super();
        this.appIcon = app_icon;
        this.appName = app_name;
        this.appVersion = app_version;
        this.packageName = packagename;
        this.apkPath = apkPath;
    }
    public Drawable getAppIcon() {
        return appIcon;
    }
    public void setAppIcon(Drawable app_icon) {
        this.appIcon = app_icon;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String app_name) {
        this.appName = app_name;
    }
    public String getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(String app_version) {
        this.appVersion = app_version;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packagename) {
        this.packageName = packagename;
    }
    public void setApkPath(String apkPath){
        this.apkPath = apkPath;
    }
    public String getApkPath(){
        return this.apkPath;
    }

    @Override
    public String toString() {
        return "AppInfo [appIcon=" + appIcon + ", appName=" + appName
                + ", appVersion=" + appVersion + ", packageName="
                + packageName + "]";
    }
}
