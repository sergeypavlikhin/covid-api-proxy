package com.pvsrg.covidapi.es.covidapi.exception;

/**
 * Root exception for CovidAPI client
 */
public class CAException extends Exception {

    public CAException() {
        super();
    }

    public CAException(String message) {
        super(message);
    }

    public CAException(String message, Throwable cause) {
        super(message, cause);
    }

    public CAException(Throwable cause) {
        super(cause);
    }

    protected CAException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
