package com.stripe.android.exception;

public class APIException extends StripeException {
    public APIException(String str, String str2, Integer num, Throwable th) {
        super(str, str2, num, th);
    }
}
