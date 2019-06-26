// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.util.Arrays;

public final class LongArray
{
    private static final int DEFAULT_INITIAL_CAPACITY = 32;
    private int size;
    private long[] values;
    
    public LongArray() {
        this(32);
    }
    
    public LongArray(final int n) {
        this.values = new long[n];
    }
    
    public void add(final long n) {
        final int size = this.size;
        final long[] values = this.values;
        if (size == values.length) {
            this.values = Arrays.copyOf(values, size * 2);
        }
        this.values[this.size++] = n;
    }
    
    public long get(final int i) {
        if (i >= 0 && i < this.size) {
            return this.values[i];
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid index ");
        sb.append(i);
        sb.append(", size is ");
        sb.append(this.size);
        throw new IndexOutOfBoundsException(sb.toString());
    }
    
    public int size() {
        return this.size;
    }
    
    public long[] toArray() {
        return Arrays.copyOf(this.values, this.size);
    }
}
