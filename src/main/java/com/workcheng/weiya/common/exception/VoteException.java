package com.workcheng.weiya.common.exception;

/**
 *
 * @author andy
 * @date 2016/12/29
 */
public class VoteException extends Exception {
    public VoteException() {
        super();
    }

    public VoteException(String message) {
        super(message);
    }

    public VoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public VoteException(Throwable cause) {
        super(cause);
    }

    protected VoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
