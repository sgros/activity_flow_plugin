package com.stripe.android.exception;

public abstract class StripeException extends Exception {
    private String requestId;
    private Integer statusCode;

    public StripeException(String str, String str2, Integer num) {
        super(str, null);
        this.requestId = str2;
        this.statusCode = num;
    }

    public StripeException(String str, String str2, Integer num, Throwable th) {
        super(str, th);
        this.statusCode = num;
        this.requestId = str2;
    }

    public String toString() {
        String stringBuilder;
        if (this.requestId != null) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("; request-id: ");
            stringBuilder2.append(this.requestId);
            stringBuilder = stringBuilder2.toString();
        } else {
            stringBuilder = "";
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(super.toString());
        stringBuilder3.append(stringBuilder);
        return stringBuilder3.toString();
    }
}
