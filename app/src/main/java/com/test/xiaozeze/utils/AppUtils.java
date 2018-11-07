package com.test.xiaozeze.utils;

import android.content.pm.PackageManager;
import android.text.TextUtils;

public class AppUtils {
    private PackageManager mPackageManager;

    private AppUtils() {
        mPackageManager = Utils.getContext().getPackageManager();
    }

    private static class Singleton {
        private static final AppUtils INSTANCE = new AppUtils();
    }

    public static AppUtils getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * desc 获取app版本号
     */
    public String getAppVersion() {
        String versionName = "1.0.0";
        try {
            String str = mPackageManager.getPackageInfo(getAppPackage(), 0).versionName;
            if (!TextUtils.isEmpty(str)) {
                versionName = str;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * desc 获取app包名
     */
    public String getAppPackage() {
        String packageName = Utils.getContext().getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            packageName = "com.xiaozhu.xzdz";
        }
        return packageName;
    }

}
