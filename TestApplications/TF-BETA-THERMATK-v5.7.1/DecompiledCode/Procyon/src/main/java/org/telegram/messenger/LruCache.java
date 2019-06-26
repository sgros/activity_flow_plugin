// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LruCache<T>
{
    private final LinkedHashMap<String, T> map;
    private final LinkedHashMap<String, ArrayList<String>> mapFilters;
    private int maxSize;
    private int size;
    
    public LruCache(final int maxSize) {
        if (maxSize > 0) {
            this.maxSize = maxSize;
            this.map = new LinkedHashMap<String, T>(0, 0.75f, true);
            this.mapFilters = new LinkedHashMap<String, ArrayList<String>>();
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }
    
    private int safeSizeOf(final String str, final T obj) {
        final int size = this.sizeOf(str, obj);
        if (size >= 0) {
            return size;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Negative size: ");
        sb.append(str);
        sb.append("=");
        sb.append(obj);
        throw new IllegalStateException(sb.toString());
    }
    
    private void trimToSize(final int n, final String s) {
        synchronized (this) {
            final Iterator<Map.Entry<String, T>> iterator = this.map.entrySet().iterator();
            while (iterator.hasNext() && this.size > n && !this.map.isEmpty()) {
                final Map.Entry<String, T> entry = iterator.next();
                final String anObject = entry.getKey();
                if (s != null && s.equals(anObject)) {
                    continue;
                }
                final T value = entry.getValue();
                this.size -= this.safeSizeOf(anObject, value);
                iterator.remove();
                final String[] split = anObject.split("@");
                if (split.length > 1) {
                    final ArrayList<String> list = this.mapFilters.get(split[0]);
                    if (list != null) {
                        list.remove(split[1]);
                        if (list.isEmpty()) {
                            this.mapFilters.remove(split[0]);
                        }
                    }
                }
                this.entryRemoved(true, anObject, value, null);
            }
        }
    }
    
    public boolean contains(final String key) {
        return this.map.containsKey(key);
    }
    
    protected void entryRemoved(final boolean b, final String s, final T t, final T t2) {
    }
    
    public final void evictAll() {
        this.trimToSize(-1, null);
    }
    
    public final T get(final String key) {
        if (key != null) {
            synchronized (this) {
                final T value = this.map.get(key);
                if (value != null) {
                    return value;
                }
                return null;
            }
        }
        throw new NullPointerException("key == null");
    }
    
    public ArrayList<String> getFilterKeys(final String key) {
        final ArrayList<String> c = this.mapFilters.get(key);
        if (c != null) {
            return new ArrayList<String>(c);
        }
        return null;
    }
    
    public final int maxSize() {
        synchronized (this) {
            return this.maxSize;
        }
    }
    
    public T put(final String key, final T value) {
        if (key != null && value != null) {
            synchronized (this) {
                this.size += this.safeSizeOf(key, value);
                final T put = this.map.put(key, value);
                if (put != null) {
                    this.size -= this.safeSizeOf(key, put);
                }
                // monitorexit(this)
                final String[] split = key.split("@");
                if (split.length > 1) {
                    ArrayList<String> value2;
                    if ((value2 = this.mapFilters.get(split[0])) == null) {
                        value2 = new ArrayList<String>();
                        this.mapFilters.put(split[0], value2);
                    }
                    if (!value2.contains(split[1])) {
                        value2.add(split[1]);
                    }
                }
                if (put != null) {
                    this.entryRemoved(false, key, put, value);
                }
                this.trimToSize(this.maxSize, key);
                return put;
            }
        }
        throw new NullPointerException("key == null || value == null");
    }
    
    public final T remove(final String key) {
        if (key != null) {
            synchronized (this) {
                final T remove = this.map.remove(key);
                if (remove != null) {
                    this.size -= this.safeSizeOf(key, remove);
                }
                // monitorexit(this)
                if (remove != null) {
                    final String[] split = key.split("@");
                    if (split.length > 1) {
                        final ArrayList<String> list = this.mapFilters.get(split[0]);
                        if (list != null) {
                            list.remove(split[1]);
                            if (list.isEmpty()) {
                                this.mapFilters.remove(split[0]);
                            }
                        }
                    }
                    this.entryRemoved(false, key, remove, null);
                }
                return remove;
            }
        }
        throw new NullPointerException("key == null");
    }
    
    public final int size() {
        synchronized (this) {
            return this.size;
        }
    }
    
    protected int sizeOf(final String s, final T t) {
        return 1;
    }
}
