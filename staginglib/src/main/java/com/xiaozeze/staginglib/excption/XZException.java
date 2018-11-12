package com.xiaozeze.staginglib.excption;

/**
 * Description:
 * Author: fengzeyuan
 * Date: 2018/11/12 下午5:10
 * Version: 1.0
 */
public class XZException extends RuntimeException {

    public XZException(String message) {
        super(message);
    }

    public XZException(String message, Throwable cause) {
        super(message, cause);
    }

    public XZException(Throwable cause) {
        super(cause);
    }
}
