package com.stripe.android.exception;

public class PermissionException extends AuthenticationException {
    public PermissionException(String str, String str2, Integer num) {
        super(str, str2, num);
    }
}
