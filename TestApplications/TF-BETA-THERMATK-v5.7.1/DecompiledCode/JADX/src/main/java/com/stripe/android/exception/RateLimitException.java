package com.stripe.android.exception;

public class RateLimitException extends InvalidRequestException {
    public RateLimitException(String str, String str2, String str3, Integer num, Throwable th) {
        super(str, str2, str3, num, th);
    }
}
