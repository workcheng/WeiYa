package com.workcheng.weiya.common.exception;

/**
 *
 * @author andy
 * @date 2016/12/22
 */
public class ServerInternalException extends Exception {

    public ServerInternalException() {
        super();
    }

    public ServerInternalException(String message) {
        super(message);
    }

    public ServerInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerInternalException(Throwable cause) {
        super(cause);
    }

    protected ServerInternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
