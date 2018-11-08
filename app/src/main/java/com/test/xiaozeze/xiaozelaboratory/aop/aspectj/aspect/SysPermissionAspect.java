package com.test.xiaozeze.xiaozelaboratory.aop.aspectj.aspect;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


import com.test.xiaozeze.XZApp;
import com.test.xiaozeze.annotation.aspect.Permission;

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
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, Permission permission) throws Throwable {
        AppCompatActivity ac = (AppCompatActivity) XZApp.getInstance().getCurActivity();
        new AlertDialog.Builder(ac)
                .setTitle("提示")
                .setMessage("为了应用可以正常使用，请您点击确认申请权限。")
                .setNegativeButton("取消", null)
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        MPermissionUtils.requestPermissionsResult(ac, 1, Manifest.permission.value()
//                                , new MPermissionUtils.OnPermissionListener() {
//                                    @Override
//                                    public void onPermissionGranted() {
//                                        try {
//                                            joinPoint.proceed();//获得权限，执行原方法
//                                        } catch (Throwable e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onPermissionDenied() {
//                                        MPermissionUtils.showTipsDialog(ac);
//                                    }
//                                });
                    }
                })
                .create()
                .show();
    }
}


