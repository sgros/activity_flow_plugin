// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load;

import java.io.IOException;

public final class HttpException extends IOException
{
    private final int statusCode;
    
    public HttpException(final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Http request failed with status code: ");
        sb.append(i);
        this(sb.toString(), i);
    }
    
    public HttpException(final String s) {
        this(s, -1);
    }
    
    public HttpException(final String s, final int n) {
        this(s, n, null);
    }
    
    public HttpException(final String message, final int statusCode, final Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
