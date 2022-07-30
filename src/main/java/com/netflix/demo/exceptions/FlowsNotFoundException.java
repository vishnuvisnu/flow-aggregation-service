package com.netflix.demo.exceptions;

import java.net.HttpURLConnection;

public class FlowsNotFoundException extends FasException {

    public FlowsNotFoundException(final String message) {
        super(HttpURLConnection.HTTP_NOT_FOUND, message);
    }

    public FlowsNotFoundException(final String message, final Throwable throwable) {
        super(HttpURLConnection.HTTP_NOT_FOUND, message, throwable);
    }

}
