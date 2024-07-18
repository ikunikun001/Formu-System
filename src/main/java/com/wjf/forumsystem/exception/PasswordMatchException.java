package com.wjf.forumsystem.exception;

public class PasswordMatchException extends ServiceException{
    public PasswordMatchException() {
        super();
    }

    public PasswordMatchException(String message) {
        super(message);
    }

    public PasswordMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordMatchException(Throwable cause) {
        super(cause);
    }

    protected PasswordMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
