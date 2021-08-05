package com.workcheng.weiya.common.exception;

/**
 * Created by chenghui on 2016/12/29.
 */
public class SignException extends Exception {
    public SignException() {
        super();
    }

    public SignException(String message) {
        super(message);
    }

    public SignException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignException(Throwable cause) {
        super(cause);
    }

    protected SignException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
