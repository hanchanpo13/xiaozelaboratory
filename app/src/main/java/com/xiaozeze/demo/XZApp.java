package com.xiaozeze.demo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


import com.xiaozeze.demo.utils.Utils;

import java.util.Stack;

/**
 * Description:
 * Author: fengzeyuan
 * Date: 2018/8/1 下午8:12
 * Version: 1.0
 */
public class XZApp extends Application {
    private static XZApp mInstance;
    public Stack<Activity> store = new Stack<>();

    public static XZApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Utils.init(this);
        store = new Stack<>();
        registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
    }

    /**
     * 退出APP
     */
    public void exit() {
        while (store.empty()){
            Activity ac = store.pop();
            ac.finish();
        }
    }

    private class SwitchBackgroundCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            store.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            store.remove(activity);
        }
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurActivity() {
        return store.lastElement();
    }
}
