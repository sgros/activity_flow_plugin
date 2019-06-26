// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.util.UriUtil;
import android.net.Uri;

public final class RangedUri
{
    private int hashCode;
    public final long length;
    private final String referenceUri;
    public final long start;
    
    public RangedUri(final String s, final long start, final long length) {
        String referenceUri = s;
        if (s == null) {
            referenceUri = "";
        }
        this.referenceUri = referenceUri;
        this.start = start;
        this.length = length;
    }
    
    public RangedUri attemptMerge(final RangedUri rangedUri, final String s) {
        final String resolveUriString = this.resolveUriString(s);
        if (rangedUri != null) {
            if (resolveUriString.equals(rangedUri.resolveUriString(s))) {
                final long length = this.length;
                long n = -1L;
                if (length != -1L) {
                    final long start = this.start;
                    if (start + length == rangedUri.start) {
                        final long length2 = rangedUri.length;
                        if (length2 != -1L) {
                            n = length + length2;
                        }
                        return new RangedUri(resolveUriString, start, n);
                    }
                }
                final long length3 = rangedUri.length;
                if (length3 != -1L) {
                    final long start2 = rangedUri.start;
                    if (start2 + length3 == this.start) {
                        final long length4 = this.length;
                        if (length4 != -1L) {
                            n = length3 + length4;
                        }
                        return new RangedUri(resolveUriString, start2, n);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && RangedUri.class == o.getClass()) {
            final RangedUri rangedUri = (RangedUri)o;
            if (this.start != rangedUri.start || this.length != rangedUri.length || !this.referenceUri.equals(rangedUri.referenceUri)) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = ((527 + (int)this.start) * 31 + (int)this.length) * 31 + this.referenceUri.hashCode();
        }
        return this.hashCode;
    }
    
    public Uri resolveUri(final String s) {
        return UriUtil.resolveToUri(s, this.referenceUri);
    }
    
    public String resolveUriString(final String s) {
        return UriUtil.resolve(s, this.referenceUri);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RangedUri(referenceUri=");
        sb.append(this.referenceUri);
        sb.append(", start=");
        sb.append(this.start);
        sb.append(", length=");
        sb.append(this.length);
        sb.append(")");
        return sb.toString();
    }
}
