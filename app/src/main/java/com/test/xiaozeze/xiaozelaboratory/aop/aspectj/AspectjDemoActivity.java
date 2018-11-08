package com.test.xiaozeze.xiaozelaboratory.aop.aspectj;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.test.xiaozeze.utils.AppUtils;
import com.test.xiaozeze.utils.SPUtils;
import com.test.xiaozeze.xiaozelaboratory.R;

import java.util.List;

/**
 * @Description:
 * @Author: fengzeyuan
 * @Date: 18/1/30 上午1:00
 * @Version: 1.0
 */
public class AspectjDemoActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        findViewById(R.id.btn_change_icon).setOnClickListener(this);
    }

    private EditText getEtIconType(){
        return (EditText) findViewById(R.id.et_icon_type);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_icon:
                String tagName = getEtIconType().getText().toString();
                if (!TextUtils.isEmpty(tagName)) {
                    checkAppIcon(view.getContext());
                }
                break;
        }
    }

    /**
     * 设置入口Icon
     */
    public void checkAppIcon(Context ctx) {

        final String localIconKey = "DynamicIcon_" + AppUtils.getInstance().getAppVersion();
        final String appId = ctx.getPackageName();
        final String defLauncherName = String.format("%s.XZLauncher_XZAlias%s", appId, "0");

        // 服务端下发启动图类型
        String serviceLauncherType = getEtIconType().getText().toString();
        // 本地启动图类型
        String localLauncherType = SPUtils.normal().getString(localIconKey, "");
        if (!serviceLauncherType.equals(localLauncherType)) {// 激活icon替换逻辑
            PackageManager pm = ctx.getPackageManager();
            // 默认启动入口
            try {
                //待激活的Component
                ComponentName enableName = new ComponentName(ctx, String.format("%s.XZLauncher_XZAlias%s", appId, serviceLauncherType));

                // 关闭目前需要关闭的入口
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.setPackage(ctx.getPackageName());
                List<ResolveInfo> infoList = pm.queryIntentActivities(intent, 0);
                for (ResolveInfo info : infoList) {
                    if (!enableName.getClassName().equals(info.activityInfo.name)) {
                        // 关闭目前激活的启动入口
                        pm.setComponentEnabledSetting(new ComponentName(ctx, info.activityInfo.name), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    }
                }

                // 存在性检测
                pm.getActivityInfo(enableName, PackageManager.MATCH_DISABLED_COMPONENTS);
                // 激活指定的入口
                pm.setComponentEnabledSetting(enableName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                // 记录本次激活的入口
                SPUtils.normal().edit().putString(localIconKey, serviceLauncherType).commit();
            } catch (Exception e) {
                // 激活默认入口
                pm.setComponentEnabledSetting(new ComponentName(ctx, defLauncherName), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                // 记录本次激活的入口
                SPUtils.normal().edit().remove(localIconKey).commit();
            }
        }
    }
}
