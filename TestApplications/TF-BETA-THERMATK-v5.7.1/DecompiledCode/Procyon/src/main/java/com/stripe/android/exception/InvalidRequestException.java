// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.exception;

public class InvalidRequestException extends StripeException
{
    private final String param;
    
    public InvalidRequestException(final String s, final String param, final String s2, final Integer n, final Throwable t) {
        super(s, s2, n, t);
        this.param = param;
    }
}
