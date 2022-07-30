package com.netflix.demo.exceptions;

import java.net.HttpURLConnection;

public class FasInternalErrorException extends FasException {
    public FasInternalErrorException(final String message) {
        super(HttpURLConnection.HTTP_INTERNAL_ERROR, message);
    }

    public FasInternalErrorException(final String message, final Throwable throwable) {
        super(HttpURLConnection.HTTP_NOT_FOUND, message, throwable);
    }
}
