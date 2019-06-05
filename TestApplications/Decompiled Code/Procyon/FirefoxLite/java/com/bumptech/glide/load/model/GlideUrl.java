// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import java.security.MessageDigest;
import java.util.Map;
import java.net.MalformedURLException;
import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.util.Preconditions;
import java.net.URL;
import com.bumptech.glide.load.Key;

public class GlideUrl implements Key
{
    private volatile byte[] cacheKeyBytes;
    private int hashCode;
    private final Headers headers;
    private String safeStringUrl;
    private URL safeUrl;
    private final String stringUrl;
    private final URL url;
    
    public GlideUrl(final String s) {
        this(s, Headers.DEFAULT);
    }
    
    public GlideUrl(final String s, final Headers headers) {
        this.url = null;
        this.stringUrl = Preconditions.checkNotEmpty(s);
        this.headers = Preconditions.checkNotNull(headers);
    }
    
    public GlideUrl(final URL url) {
        this(url, Headers.DEFAULT);
    }
    
    public GlideUrl(final URL url, final Headers headers) {
        this.url = Preconditions.checkNotNull(url);
        this.stringUrl = null;
        this.headers = Preconditions.checkNotNull(headers);
    }
    
    private byte[] getCacheKeyBytes() {
        if (this.cacheKeyBytes == null) {
            this.cacheKeyBytes = this.getCacheKey().getBytes(GlideUrl.CHARSET);
        }
        return this.cacheKeyBytes;
    }
    
    private String getSafeStringUrl() {
        if (TextUtils.isEmpty((CharSequence)this.safeStringUrl)) {
            String s;
            if (TextUtils.isEmpty((CharSequence)(s = this.stringUrl))) {
                s = this.url.toString();
            }
            this.safeStringUrl = Uri.encode(s, "@#&=*+-_.,:!?()/~'%");
        }
        return this.safeStringUrl;
    }
    
    private URL getSafeUrl() throws MalformedURLException {
        if (this.safeUrl == null) {
            this.safeUrl = new URL(this.getSafeStringUrl());
        }
        return this.safeUrl;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof GlideUrl;
        final boolean b2 = false;
        if (b) {
            final GlideUrl glideUrl = (GlideUrl)o;
            boolean b3 = b2;
            if (this.getCacheKey().equals(glideUrl.getCacheKey())) {
                b3 = b2;
                if (this.headers.equals(glideUrl.headers)) {
                    b3 = true;
                }
            }
            return b3;
        }
        return false;
    }
    
    public String getCacheKey() {
        String s;
        if (this.stringUrl != null) {
            s = this.stringUrl;
        }
        else {
            s = this.url.toString();
        }
        return s;
    }
    
    public Map<String, String> getHeaders() {
        return this.headers.getHeaders();
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.getCacheKey().hashCode();
            this.hashCode = this.hashCode * 31 + this.headers.hashCode();
        }
        return this.hashCode;
    }
    
    @Override
    public String toString() {
        return this.getCacheKey();
    }
    
    public URL toURL() throws MalformedURLException {
        return this.getSafeUrl();
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        messageDigest.update(this.getCacheKeyBytes());
    }
}
