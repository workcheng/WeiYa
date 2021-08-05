package com.workcheng.weiya.common.exception;

/**
 * Created by chenghui on 2017/1/18.
 */
public class LotteryException extends Exception {
    public LotteryException() {
    }

    public LotteryException(String message) {
        super(message);
    }

    public LotteryException(String message, Throwable cause) {
        super(message, cause);
    }

    public LotteryException(Throwable cause) {
        super(cause);
    }

    public LotteryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
