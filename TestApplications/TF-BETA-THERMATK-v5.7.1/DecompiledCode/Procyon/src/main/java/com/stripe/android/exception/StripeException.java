// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.exception;

public abstract class StripeException extends Exception
{
    private String requestId;
    private Integer statusCode;
    
    public StripeException(final String message, final String requestId, final Integer statusCode) {
        super(message, null);
        this.requestId = requestId;
        this.statusCode = statusCode;
    }
    
    public StripeException(final String message, final String requestId, final Integer statusCode, final Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.requestId = requestId;
    }
    
    @Override
    public String toString() {
        String string;
        if (this.requestId != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("; request-id: ");
            sb.append(this.requestId);
            string = sb.toString();
        }
        else {
            string = "";
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(super.toString());
        sb2.append(string);
        return sb2.toString();
    }
}
