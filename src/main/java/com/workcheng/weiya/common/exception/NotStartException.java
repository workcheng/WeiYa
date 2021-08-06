package com.workcheng.weiya.common.exception;

/**
 *
 * @author andy
 * @date 2016/12/29
 */
public class NotStartException extends Exception {
    public NotStartException() {
        super();
    }

    public NotStartException(String message) {
        super(message);
    }

    public NotStartException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotStartException(Throwable cause) {
        super(cause);
    }

    protected NotStartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
