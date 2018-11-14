package com.xiaozeze.demo.xiaozelaboratory.diffupdate;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.test.xiaozeze.xiaozelaboratory.R;
import com.xiaozeze.appdiffupdate.DiffPatchUtils;
import com.xiaozeze.demo.utils.Utils;

import java.io.File;

/**
 * @Description:
 * @Author: fengzeyuan
 * @Date: 18/1/30 上午1:00
 * @Version: 1.0
 */
public class Page1_DiffUpdateActivity extends Activity {

    private static final String PATH_NEW_APP = Utils.getFilePath("newAPP.apk");
    private static final String PATH_OLD_APP = Utils.getAppPath("com.xiaozhu.xzdz");
    private static final String PATCH_SERVICE_END_FIX = "_diff_service.patch";
    private static final String PATCH_LOCAL_END_FIX = "_diff_local.patch";
    
    private static final File sServicePatchFile = Utils.getServicePatchFile(PATCH_SERVICE_END_FIX);
    // 成功
    private static final int WHAT_SUCCESS = 1;
    // 合成失败
    private static final int WHAT_FAIL_PATCH = 0;

    private TextView tv_cur_state;
    private TextView mTv_newAPP_md5;
    private String tagMd5 = "INVALID";
    private String localPathDiffPatch = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        tv_cur_state = findViewById(R.id.tv_show_new_app_md5_plan);
        mTv_newAPP_md5 = findViewById(R.id.tv_NewAPP_md5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sServicePatchFile != null && sServicePatchFile.exists() && sServicePatchFile.isFile()) {
            tagMd5 = sServicePatchFile.getName().replace(PATCH_SERVICE_END_FIX, "");
            File localPatch = new File(Utils.getFilePath(tagMd5 + PATCH_LOCAL_END_FIX));
            if (localPatch.exists()) {
                localPathDiffPatch = localPatch.getAbsolutePath();
                tv_cur_state.setText("目标APK的MD5：" + sServicePatchFile.getName().replace(PATCH_SERVICE_END_FIX, ""));
            } else {
                tv_cur_state.setText("未下载差量文件");
            }
        }
    }

    public void loadPatch(View v) {
        new LoadPatchTask().execute();
    }

    public void doPatchApk(View v) {
        new PatchTask().execute();
    }

    public void checkApk(View v) {
        new CheckTask().execute(false);
    }

    public void install(View v) {
        new CheckTask().execute(true);
    }

    public void clear(View v) {
        new File(localPathDiffPatch).delete();
        new File(PATH_NEW_APP).delete();
        tv_cur_state.setText("未下载差量文件");
        mTv_newAPP_md5.setText("");
    }



    /**
     * 模拟差量包下载
     */
    private class LoadPatchTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            if (sServicePatchFile != null && sServicePatchFile.exists() && sServicePatchFile.isFile()) {
                final String md5 = sServicePatchFile.getName().replace(PATCH_SERVICE_END_FIX, "");
                final String destPath = Utils.getFilePath(md5 + PATCH_LOCAL_END_FIX);
                boolean isOK = Utils.copyFile(sServicePatchFile.getAbsolutePath(), destPath);
                if (isOK) {
                    tv_cur_state.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            localPathDiffPatch = destPath;
                            Utils.showToast("下载成功!");
                            tv_cur_state.setText("目标APK的MD5:" + md5);
                        }
                    }, 2000);
                    return WHAT_SUCCESS;
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showToast("下载差量包失败");
                }
            });
            return WHAT_FAIL_PATCH;
        }

    }


    /**
     * 合并差量包
     */
    private class PatchTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            try {
                File oldAppFile = new File(PATH_OLD_APP);
                if (!oldAppFile.exists()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showToast("请先安装低版本小猪app...");
                        }
                    });
                    return WHAT_FAIL_PATCH;
                }
                File patchFile = new File(localPathDiffPatch);
                if (!patchFile.exists()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showToast("请先生成差量包...");
                        }
                    });
                    return WHAT_FAIL_PATCH;
                }

                int result = DiffPatchUtils.getInstance().patch(PATH_OLD_APP, PATH_NEW_APP, patchFile.getAbsolutePath());
                if (result == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showToast("合成APK成功...");
                        }
                    });
                    return WHAT_SUCCESS;
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showToast("合成APK失败...");
                        }
                    });
                    return WHAT_FAIL_PATCH;
                }
            } catch (Exception e) {
                Log.i("jw", "error:" + Log.getStackTraceString(e));
            }
            return WHAT_FAIL_PATCH;
        }

    }


    /**
     *APP的MD5检测/安装
     */
    private class CheckTask extends AsyncTask<Boolean, Void, Boolean> {
        @Override
        protected Boolean doInBackground(final Boolean... needInstall) {

            File file = new File(localPathDiffPatch);
            if (!file.exists() || !file.isFile()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showToast("请先下载差量包");
                    }
                });
                return false;
            }
            
            // 比MD5
            final String tagAppMd5 = file.getName().replace(PATCH_LOCAL_END_FIX,"");
            final String newAppMd5 = Utils.getFileMd5(PATH_NEW_APP);
            boolean isSame = tagAppMd5.equalsIgnoreCase(newAppMd5);
            if (isSame) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!needInstall[0]) {
                            Utils.showToast("合成APK和目标APK一致...");
                            mTv_newAPP_md5.setText(newAppMd5);
                        } else {
                            Utils.installAPK(PATH_NEW_APP);
                        }
                    }
                });
                return isSame;
            } else {
                new File(PATH_NEW_APP).delete();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showToast("请先合并差量包");
                }
            });
            return isSame;
        }
    }
}
