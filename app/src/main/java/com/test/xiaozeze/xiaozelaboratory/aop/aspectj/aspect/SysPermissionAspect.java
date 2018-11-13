package com.test.xiaozeze.xiaozelaboratory.aop.aspectj.aspect;

import android.Manifest;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.NonNull;

import com.test.xiaozeze.XZApp;
import com.xiaozeze.annotation.aspect.XZPermissionChecker;
import com.xiaozeze.permissionchecker.XZPermissionUtils;
import com.xiaozeze.permissionchecker.listener.PermissionListener;
import com.xiaozeze.staginglib.utils.ToastUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Description: 权限检测
 * Author: fengzeyuan
 * Date: 2018/11/8 下午6:56
 * Version: 1.0
 */
@Aspect
public class SysPermissionAspect {

    @Around("execution(@com.app.annotation.aspect.Permission * *(..)) && @annotation(permission)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, final XZPermissionChecker checker) throws Throwable {
        final XZApp app = XZApp.getInstance();
        XZPermissionUtils.checkPermission(app, checker.value(), new PermissionListener() {

            @Override
            public void onSucceed(int requestCode, @NonNull String[] grantPermissions) {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onFailed(int requestCode, @NonNull String[] deniedPermissions) {
                if (!checker.isForce) {
                    String funName = XZPermissionUtils.getPermissionGroupLabelStr(app, Manifest.permission.SYSTEM_ALERT_WINDOW);
                    ToastUtils.showShort("权限请求失败，您将无法使用" + funName + "功能");
                } else {
                    ToastUtils.showLong("权限请求失败，无法正常运行，即将退出");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            app.exit();
                            Process.killProcess(Process.myPid());
                            System.exit(0);
                        }
                    }, 3000);
                }
            }
        });
    }
}


