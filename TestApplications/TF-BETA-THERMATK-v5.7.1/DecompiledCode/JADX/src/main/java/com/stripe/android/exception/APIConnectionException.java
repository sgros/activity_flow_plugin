package com.stripe.android.exception;

public class APIConnectionException extends StripeException {
    public APIConnectionException(String str) {
        super(str, null, Integer.valueOf(0));
    }

    public APIConnectionException(String str, Throwable th) {
        super(str, null, Integer.valueOf(0), th);
    }
}
