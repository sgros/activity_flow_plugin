// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Assertions;
import android.net.Uri;

public final class DataSpec
{
    public final long absoluteStreamPosition;
    public final int flags;
    public final byte[] httpBody;
    public final int httpMethod;
    public final String key;
    public final long length;
    public final long position;
    @Deprecated
    public final byte[] postBody;
    public final Uri uri;
    
    public DataSpec(final Uri uri, final int n) {
        this(uri, 0L, -1L, null, n);
    }
    
    public DataSpec(final Uri uri, final int httpMethod, byte[] httpBody, final long absoluteStreamPosition, final long position, final long length, final String key, final int flags) {
        final boolean b = true;
        Assertions.checkArgument(absoluteStreamPosition >= 0L);
        Assertions.checkArgument(position >= 0L);
        boolean b2 = b;
        if (length <= 0L) {
            b2 = (length == -1L && b);
        }
        Assertions.checkArgument(b2);
        this.uri = uri;
        this.httpMethod = httpMethod;
        if (httpBody == null || httpBody.length == 0) {
            httpBody = null;
        }
        this.httpBody = httpBody;
        this.postBody = this.httpBody;
        this.absoluteStreamPosition = absoluteStreamPosition;
        this.position = position;
        this.length = length;
        this.key = key;
        this.flags = flags;
    }
    
    public DataSpec(final Uri uri, final long n, final long n2, final long n3, final String s, final int n4) {
        this(uri, null, n, n2, n3, s, n4);
    }
    
    public DataSpec(final Uri uri, final long n, final long n2, final String s) {
        this(uri, n, n, n2, s, 0);
    }
    
    public DataSpec(final Uri uri, final long n, final long n2, final String s, final int n3) {
        this(uri, n, n, n2, s, n3);
    }
    
    public DataSpec(final Uri uri, final byte[] array, final long n, final long n2, final long n3, final String s, final int n4) {
        int n5;
        if (array != null) {
            n5 = 2;
        }
        else {
            n5 = 1;
        }
        this(uri, n5, array, n, n2, n3, s, n4);
    }
    
    public static String getStringForHttpMethod(final int detailMessage) {
        if (detailMessage == 1) {
            return "GET";
        }
        if (detailMessage == 2) {
            return "POST";
        }
        if (detailMessage == 3) {
            return "HEAD";
        }
        throw new AssertionError(detailMessage);
    }
    
    public final String getHttpMethodString() {
        return getStringForHttpMethod(this.httpMethod);
    }
    
    public boolean isFlagSet(final int n) {
        return (this.flags & n) == n;
    }
    
    public DataSpec subrange(final long n) {
        final long length = this.length;
        long n2 = -1L;
        if (length != -1L) {
            n2 = length - n;
        }
        return this.subrange(n, n2);
    }
    
    public DataSpec subrange(final long n, final long n2) {
        if (n == 0L && this.length == n2) {
            return this;
        }
        return new DataSpec(this.uri, this.httpMethod, this.httpBody, this.absoluteStreamPosition + n, this.position + n, n2, this.key, this.flags);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DataSpec[");
        sb.append(this.getHttpMethodString());
        sb.append(" ");
        sb.append(this.uri);
        sb.append(", ");
        sb.append(Arrays.toString(this.httpBody));
        sb.append(", ");
        sb.append(this.absoluteStreamPosition);
        sb.append(", ");
        sb.append(this.position);
        sb.append(", ");
        sb.append(this.length);
        sb.append(", ");
        sb.append(this.key);
        sb.append(", ");
        sb.append(this.flags);
        sb.append("]");
        return sb.toString();
    }
}
