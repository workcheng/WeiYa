package com.zoe.weiya.comm.exception;

/**
 * Created by chenghui on 2016/12/29.
 */
public class HasVoteException  extends Exception{
    public HasVoteException() {
        super();
    }

    public HasVoteException(String message) {
        super(message);
    }

    public HasVoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public HasVoteException(Throwable cause) {
        super(cause);
    }

    protected HasVoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
