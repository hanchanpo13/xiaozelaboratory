package com.test.xiaozeze.xiaozelaboratory;

import android.app.Application;

/**
 * Description:
 * Author: fengzeyuan
 * Date: 2018/8/1 下午8:12
 * Version: 1.0
 */
public class XZApp extends Application {
    private static XZApp mInstance;

    public static XZApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
