package com.test.xiaozeze.annotation.aspect;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 时间统计
 * Author: fengzeyuan
 * Date: 2018/11/8 下午4:49
 * Version: 1.0
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface TimeLog {
}
