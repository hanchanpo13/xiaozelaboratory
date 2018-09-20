package com.test.xiaozeze.xiaozelaboratory.utils;

import android.app.Activity;
import android.content.SharedPreferences;

public class SPUtils {
    public static String SP_ORDER = "xz_order";  //退出账号时清除

    private SPUtils() {
    }

    /**
     * desc 常规sp，存到xz_sp中，随app卸载或清除数据而删除
     */
    public static SharedPreferences normal() {
        return create("xz_sp");
    }

    /**
     * desc 账号sp，存到xz_a_sp中，随app账号退出或app卸载或清除数据而删除
     */
    public static SharedPreferences account() {
        return create("xz_a_sp");
    }

    /**
     * desc 常规sp，可自定义sp name，随app卸载或清除数据而删除
     */
    public static SharedPreferences create(String name) {
        return Utils.getContext().getSharedPreferences(name, Activity.MODE_PRIVATE);
    }
}
