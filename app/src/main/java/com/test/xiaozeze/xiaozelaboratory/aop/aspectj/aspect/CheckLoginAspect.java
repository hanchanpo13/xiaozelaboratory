package com.test.xiaozeze.xiaozelaboratory.aop.aspectj.aspect;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.test.xiaozeze.XZApp;
import com.test.xiaozeze.utils.Utils;
import com.test.xiaozeze.xiaozelaboratory.aop.TestUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


/**
 * Description: 登录检测
 * Author: fengzeyuan
 * Date: 2018/11/8 下午6:56
 * Version: 1.0
 */
@Aspect
public class CheckLoginAspect {

    @Pointcut("execution(@com.app.annotation.aspect.CheckLogin * *(..))")//方法切入点
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")//在连接点进行方法替换
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!TestUtils.isLogin()) {
            Snackbar.make(XZApp.getInstance().getCurActivity().getWindow().getDecorView(), "请先登录!", Snackbar.LENGTH_LONG)
                    .setAction("登录", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utils.showToast("跳登录页");
                        }
                    }).show();
            return;
        }
        joinPoint.proceed();//执行原方法
    }
}

