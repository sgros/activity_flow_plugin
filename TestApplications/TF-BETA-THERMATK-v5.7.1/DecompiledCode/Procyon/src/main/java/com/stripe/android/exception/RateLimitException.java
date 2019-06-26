// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.exception;

public class RateLimitException extends InvalidRequestException
{
    public RateLimitException(final String s, final String s2, final String s3, final Integer n, final Throwable t) {
        super(s, s2, s3, n, t);
    }
}
