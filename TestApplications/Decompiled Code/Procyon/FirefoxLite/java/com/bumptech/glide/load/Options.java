// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load;

import java.util.Iterator;
import java.util.Map;
import android.support.v4.util.SimpleArrayMap;
import java.security.MessageDigest;
import android.support.v4.util.ArrayMap;

public final class Options implements Key
{
    private final ArrayMap<Option<?>, Object> values;
    
    public Options() {
        this.values = new ArrayMap<Option<?>, Object>();
    }
    
    private static <T> void updateDiskCacheKey(final Option<T> option, final Object o, final MessageDigest messageDigest) {
        option.update((T)o, messageDigest);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Options && this.values.equals(((Options)o).values);
    }
    
    public <T> T get(final Option<T> option) {
        Object o;
        if (this.values.containsKey(option)) {
            o = this.values.get(option);
        }
        else {
            o = option.getDefaultValue();
        }
        return (T)o;
    }
    
    @Override
    public int hashCode() {
        return this.values.hashCode();
    }
    
    public void putAll(final Options options) {
        this.values.putAll((SimpleArrayMap<?, ?>)options.values);
    }
    
    public <T> Options set(final Option<T> option, final T t) {
        this.values.put(option, t);
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Options{values=");
        sb.append(this.values);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        for (final Map.Entry<Option<?>, Object> entry : this.values.entrySet()) {
            updateDiskCacheKey((Option<Object>)entry.getKey(), entry.getValue(), messageDigest);
        }
    }
}
