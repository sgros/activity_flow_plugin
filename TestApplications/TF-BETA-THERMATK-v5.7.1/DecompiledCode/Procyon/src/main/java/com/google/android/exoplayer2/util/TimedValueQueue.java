// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.util.Arrays;

public final class TimedValueQueue<V>
{
    private static final int INITIAL_BUFFER_SIZE = 10;
    private int first;
    private int size;
    private long[] timestamps;
    private V[] values;
    
    public TimedValueQueue() {
        this(10);
    }
    
    public TimedValueQueue(final int n) {
        this.timestamps = new long[n];
        this.values = newArray(n);
    }
    
    private void addUnchecked(final long n, final V v) {
        final int first = this.first;
        final int size = this.size;
        final V[] values = this.values;
        final int n2 = (first + size) % values.length;
        this.timestamps[n2] = n;
        values[n2] = v;
        this.size = size + 1;
    }
    
    private void clearBufferOnTimeDiscontinuity(final long n) {
        final int size = this.size;
        if (size > 0 && n <= this.timestamps[(this.first + size - 1) % this.values.length]) {
            this.clear();
        }
    }
    
    private void doubleCapacityIfFull() {
        final int length = this.values.length;
        if (this.size < length) {
            return;
        }
        final int n = length * 2;
        final long[] timestamps = new long[n];
        final Object[] array = newArray(n);
        final int first = this.first;
        final int n2 = length - first;
        System.arraycopy(this.timestamps, first, timestamps, 0, n2);
        System.arraycopy(this.values, this.first, array, 0, n2);
        final int first2 = this.first;
        if (first2 > 0) {
            System.arraycopy(this.timestamps, 0, timestamps, n2, first2);
            System.arraycopy(this.values, 0, array, n2, this.first);
        }
        this.timestamps = timestamps;
        this.values = (V[])array;
        this.first = 0;
    }
    
    private static <V> V[] newArray(final int n) {
        return (V[])new Object[n];
    }
    
    private V poll(final long n, final boolean b) {
        long n2 = Long.MAX_VALUE;
        V v = null;
        while (this.size > 0) {
            final long n3 = n - this.timestamps[this.first];
            if (n3 < 0L) {
                if (b) {
                    break;
                }
                if (-n3 >= n2) {
                    break;
                }
            }
            final V[] values = this.values;
            final int first = this.first;
            v = values[first];
            values[first] = null;
            this.first = (first + 1) % values.length;
            --this.size;
            n2 = n3;
        }
        return v;
    }
    
    public void add(final long n, final V v) {
        synchronized (this) {
            this.clearBufferOnTimeDiscontinuity(n);
            this.doubleCapacityIfFull();
            this.addUnchecked(n, v);
        }
    }
    
    public void clear() {
        synchronized (this) {
            this.first = 0;
            this.size = 0;
            Arrays.fill(this.values, null);
        }
    }
    
    public V poll(final long n) {
        synchronized (this) {
            return this.poll(n, false);
        }
    }
    
    public V pollFloor(final long n) {
        synchronized (this) {
            return this.poll(n, true);
        }
    }
    
    public int size() {
        synchronized (this) {
            return this.size;
        }
    }
}
