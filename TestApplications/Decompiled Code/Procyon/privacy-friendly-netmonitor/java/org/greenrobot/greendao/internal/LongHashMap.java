// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.internal;

import org.greenrobot.greendao.DaoLog;
import java.util.Arrays;

public final class LongHashMap<T>
{
    private int capacity;
    private int size;
    private Entry<T>[] table;
    private int threshold;
    
    public LongHashMap() {
        this(16);
    }
    
    public LongHashMap(final int capacity) {
        this.capacity = capacity;
        this.threshold = capacity * 4 / 3;
        this.table = (Entry<T>[])new Entry[capacity];
    }
    
    public void clear() {
        this.size = 0;
        Arrays.fill(this.table, null);
    }
    
    public boolean containsKey(final long n) {
        for (Entry<T> next = this.table[(((int)(n >>> 32) ^ (int)n) & Integer.MAX_VALUE) % this.capacity]; next != null; next = next.next) {
            if (next.key == n) {
                return true;
            }
        }
        return false;
    }
    
    public T get(final long n) {
        for (Entry<T> next = this.table[(((int)(n >>> 32) ^ (int)n) & Integer.MAX_VALUE) % this.capacity]; next != null; next = next.next) {
            if (next.key == n) {
                return next.value;
            }
        }
        return null;
    }
    
    public void logStats() {
        final Entry<T>[] table = this.table;
        int i = 0;
        final int length = table.length;
        int j = 0;
        while (i < length) {
            for (Entry<T> next = table[i]; next != null && next.next != null; next = next.next) {
                ++j;
            }
            ++i;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("load: ");
        sb.append(this.size / (float)this.capacity);
        sb.append(", size: ");
        sb.append(this.size);
        sb.append(", capa: ");
        sb.append(this.capacity);
        sb.append(", collisions: ");
        sb.append(j);
        sb.append(", collision ratio: ");
        sb.append(j / (float)this.size);
        DaoLog.d(sb.toString());
    }
    
    public T put(final long n, final T value) {
        final int n2 = (((int)(n >>> 32) ^ (int)n) & Integer.MAX_VALUE) % this.capacity;
        Entry<T> next;
        Entry<T> entry;
        for (entry = (next = this.table[n2]); next != null; next = next.next) {
            if (next.key == n) {
                final T value2 = next.value;
                next.value = value;
                return value2;
            }
        }
        this.table[n2] = new Entry<T>(n, value, (Entry<Object>)entry);
        ++this.size;
        if (this.size > this.threshold) {
            this.setCapacity(2 * this.capacity);
        }
        return null;
    }
    
    public T remove(final long n) {
        final int n2 = (((int)(n >>> 32) ^ (int)n) & Integer.MAX_VALUE) % this.capacity;
        Entry<T> entry = this.table[n2];
        Entry<T> entry2 = null;
        while (entry != null) {
            final Entry<T> next = entry.next;
            if (entry.key == n) {
                if (entry2 == null) {
                    this.table[n2] = next;
                }
                else {
                    entry2.next = next;
                }
                --this.size;
                return entry.value;
            }
            entry2 = entry;
            entry = next;
        }
        return null;
    }
    
    public void reserveRoom(final int n) {
        this.setCapacity(n * 5 / 3);
    }
    
    public void setCapacity(final int capacity) {
        final Entry[] table = new Entry[capacity];
        final Entry<T>[] table2 = this.table;
        for (int i = 0; i < table2.length; ++i) {
            Entry<T> next;
            for (Entry<T> entry = this.table[i]; entry != null; entry = next) {
                final long key = entry.key;
                final int n = (((int)key ^ (int)(key >>> 32)) & Integer.MAX_VALUE) % capacity;
                next = entry.next;
                entry.next = (Entry<T>)table[n];
                table[n] = entry;
            }
        }
        this.table = (Entry<T>[])table;
        this.capacity = capacity;
        this.threshold = capacity * 4 / 3;
    }
    
    public int size() {
        return this.size;
    }
    
    static final class Entry<T>
    {
        final long key;
        Entry<T> next;
        T value;
        
        Entry(final long key, final T value, final Entry<T> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
