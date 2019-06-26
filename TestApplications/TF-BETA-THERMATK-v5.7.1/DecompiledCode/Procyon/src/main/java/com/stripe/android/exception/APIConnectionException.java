// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.exception;

public class APIConnectionException extends StripeException
{
    public APIConnectionException(final String s) {
        super(s, null, 0);
    }
    
    public APIConnectionException(final String s, final Throwable t) {
        super(s, null, 0, t);
    }
}
