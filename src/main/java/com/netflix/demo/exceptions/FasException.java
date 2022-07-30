package com.netflix.demo.exceptions;

import lombok.Getter;

public class FasException extends Exception {

    @Getter
    private final int errorCode;

    public FasException(final int errorCode, final String message, final Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    public FasException(final int errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
