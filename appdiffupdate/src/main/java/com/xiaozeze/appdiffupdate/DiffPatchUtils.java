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

	public native int genDiff(String oldApkPath, String newApkPath, String patchPath);

	public native int patch(String oldApkPath, String newApkPath, String patchPath);

}