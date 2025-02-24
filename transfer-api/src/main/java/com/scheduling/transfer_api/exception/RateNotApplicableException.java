package com.scheduling.transfer_api.exception;

public class RateNotApplicableException extends RuntimeException{
    public RateNotApplicableException(String message) {
        super(message);
    }
}
