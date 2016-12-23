package com.zoe.weiya.comm.exception;

/**
 * Created by chenghui on 2016/12/22.
 */
public class HasSignException extends Exception {

    public HasSignException() {
    }

    public HasSignException(String message) {
        super(message);
    }

    public HasSignException(String message, Throwable cause) {
        super(message, cause);
    }

    public HasSignException(Throwable cause) {
        super(cause);
    }

    public HasSignException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
