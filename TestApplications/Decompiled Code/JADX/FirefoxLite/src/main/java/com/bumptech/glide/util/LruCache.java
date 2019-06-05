package com.bumptech.glide.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LruCache<T, Y> {
    private final LinkedHashMap<T, Y> cache = new LinkedHashMap(100, 0.75f, true);
    private int currentSize = 0;
    private final int initialMaxSize;
    private int maxSize;

    /* Access modifiers changed, original: protected */
    public int getSize(Y y) {
        return 1;
    }

    /* Access modifiers changed, original: protected */
    public void onItemEvicted(T t, Y y) {
    }

    public LruCache(int i) {
        this.initialMaxSize = i;
        this.maxSize = i;
    }

    public synchronized int getCurrentSize() {
        return this.currentSize;
    }

    public synchronized Y get(T t) {
        return this.cache.get(t);
    }

    public synchronized Y put(T t, Y y) {
        if (getSize(y) >= this.maxSize) {
            onItemEvicted(t, y);
            return null;
        }
        Object put = this.cache.put(t, y);
        if (y != null) {
            this.currentSize += getSize(y);
        }
        if (put != null) {
            this.currentSize -= getSize(put);
        }
        evict();
        return put;
    }

    public synchronized Y remove(T t) {
        Object remove;
        remove = this.cache.remove(t);
        if (remove != null) {
            this.currentSize -= getSize(remove);
        }
        return remove;
    }

    public void clearMemory() {
        trimToSize(0);
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void trimToSize(int i) {
        while (this.currentSize > i) {
            Entry entry = (Entry) this.cache.entrySet().iterator().next();
            Object value = entry.getValue();
            this.currentSize -= getSize(value);
            Object key = entry.getKey();
            this.cache.remove(key);
            onItemEvicted(key, value);
        }
    }

    private void evict() {
        trimToSize(this.maxSize);
    }
}
