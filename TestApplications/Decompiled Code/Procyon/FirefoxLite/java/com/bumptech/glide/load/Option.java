// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load;

import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public final class Option<T>
{
    private static final CacheKeyUpdater<Object> EMPTY_UPDATER;
    private final CacheKeyUpdater<T> cacheKeyUpdater;
    private final T defaultValue;
    private final String key;
    private volatile byte[] keyBytes;
    
    static {
        EMPTY_UPDATER = (CacheKeyUpdater)new CacheKeyUpdater<Object>() {
            @Override
            public void update(final byte[] array, final Object o, final MessageDigest messageDigest) {
            }
        };
    }
    
    Option(final String s, final T defaultValue, final CacheKeyUpdater<T> cacheKeyUpdater) {
        this.key = Preconditions.checkNotEmpty(s);
        this.defaultValue = defaultValue;
        this.cacheKeyUpdater = Preconditions.checkNotNull(cacheKeyUpdater);
    }
    
    public static <T> Option<T> disk(final String s, final T t, final CacheKeyUpdater<T> cacheKeyUpdater) {
        return new Option<T>(s, t, cacheKeyUpdater);
    }
    
    private static <T> CacheKeyUpdater<T> emptyUpdater() {
        return (CacheKeyUpdater<T>)Option.EMPTY_UPDATER;
    }
    
    private byte[] getKeyBytes() {
        if (this.keyBytes == null) {
            this.keyBytes = this.key.getBytes(Key.CHARSET);
        }
        return this.keyBytes;
    }
    
    public static <T> Option<T> memory(final String s) {
        return new Option<T>(s, null, emptyUpdater());
    }
    
    public static <T> Option<T> memory(final String s, final T t) {
        return new Option<T>(s, t, emptyUpdater());
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Option && this.key.equals(((Option)o).key);
    }
    
    public T getDefaultValue() {
        return this.defaultValue;
    }
    
    @Override
    public int hashCode() {
        return this.key.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Option{key='");
        sb.append(this.key);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
    
    public void update(final T t, final MessageDigest messageDigest) {
        this.cacheKeyUpdater.update(this.getKeyBytes(), t, messageDigest);
    }
    
    public interface CacheKeyUpdater<T>
    {
        void update(final byte[] p0, final T p1, final MessageDigest p2);
    }
}
