package com.xiaozeze.appdiffupdate;

/**
 * Description: 增量更新工具
 * Author: fengzeyuan
 * Date: 2018/7/20 下午5:19
 * Version: 1.0
 */
public class DiffPatchUtils {

    static DiffPatchUtils instance;

    public static DiffPatchUtils getInstance() {
        if (instance == null)
            instance = new DiffPatchUtils();
        return instance;
    }

    static {
        System.loadLibrary("xz_diff_patch");
    }

    /**
     * 获取差分文件
     *
     * @param oldApkPath 老文件地址
     * @param newApkPath 新文件地址
     * @param patchPath  差分包生成地址
     * @return 正常返回0
     */
    public native int genDiff(String oldApkPath, String newApkPath, String patchPath);

    /**
     * 合并差量包
     * @param oldApkPath  老文件地址
     * @param newApkPath  新文件地址
     * @param patchPath   补丁地址
     * @return 正常返回0
     */
    public native int patch(String oldApkPath, String newApkPath, String patchPath);

}