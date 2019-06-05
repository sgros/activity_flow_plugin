// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.util;

import java.util.Map;
import java.util.LinkedHashMap;

public class LruCache<T, Y>
{
    private final LinkedHashMap<T, Y> cache;
    private int currentSize;
    private final int initialMaxSize;
    private int maxSize;
    
    public LruCache(final int n) {
        this.cache = new LinkedHashMap<T, Y>(100, 0.75f, true);
        this.currentSize = 0;
        this.initialMaxSize = n;
        this.maxSize = n;
    }
    
    private void evict() {
        this.trimToSize(this.maxSize);
    }
    
    public void clearMemory() {
        this.trimToSize(0);
    }
    
    public Y get(final T key) {
        synchronized (this) {
            return this.cache.get(key);
        }
    }
    
    public int getCurrentSize() {
        synchronized (this) {
            return this.currentSize;
        }
    }
    
    protected int getSize(final Y y) {
        return 1;
    }
    
    protected void onItemEvicted(final T t, final Y y) {
    }
    
    public Y put(final T key, final Y value) {
        synchronized (this) {
            if (this.getSize(value) >= this.maxSize) {
                this.onItemEvicted(key, value);
                return null;
            }
            final Y put = this.cache.put(key, value);
            if (value != null) {
                this.currentSize += this.getSize(value);
            }
            if (put != null) {
                this.currentSize -= this.getSize(put);
            }
            this.evict();
            return put;
        }
    }
    
    public Y remove(final T key) {
        synchronized (this) {
            final Y remove = this.cache.remove(key);
            if (remove != null) {
                this.currentSize -= this.getSize(remove);
            }
            return remove;
        }
    }
    
    protected void trimToSize(final int n) {
        synchronized (this) {
            while (this.currentSize > n) {
                final Map.Entry<T, Y> entry = this.cache.entrySet().iterator().next();
                final Y value = entry.getValue();
                this.currentSize -= this.getSize(value);
                final T key = entry.getKey();
                this.cache.remove(key);
                this.onItemEvicted(key, value);
            }
        }
    }
}
