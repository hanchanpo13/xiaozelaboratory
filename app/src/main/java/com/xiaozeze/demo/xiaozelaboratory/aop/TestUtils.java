package com.xiaozeze.demo.xiaozelaboratory.aop;

import java.util.Random;

/**
 * Description:
 * Author: fengzeyuan
 * Date: 2018/11/8 下午4:25
 * Version: 1.0
 */
public class TestUtils {
    public static boolean isLogin() {
        return new Random().nextBoolean();
    }
}
