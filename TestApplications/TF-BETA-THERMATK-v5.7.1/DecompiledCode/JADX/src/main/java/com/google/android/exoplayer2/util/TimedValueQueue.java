package com.google.android.exoplayer2.util;

import java.util.Arrays;

public final class TimedValueQueue<V> {
    private static final int INITIAL_BUFFER_SIZE = 10;
    private int first;
    private int size;
    private long[] timestamps;
    private V[] values;

    public TimedValueQueue() {
        this(10);
    }

    public TimedValueQueue(int i) {
        this.timestamps = new long[i];
        this.values = newArray(i);
    }

    public synchronized void add(long j, V v) {
        clearBufferOnTimeDiscontinuity(j);
        doubleCapacityIfFull();
        addUnchecked(j, v);
    }

    public synchronized void clear() {
        this.first = 0;
        this.size = 0;
        Arrays.fill(this.values, null);
    }

    public synchronized int size() {
        return this.size;
    }

    public synchronized V pollFloor(long j) {
        return poll(j, true);
    }

    public synchronized V poll(long j) {
        return poll(j, false);
    }

    private V poll(long j, boolean z) {
        long j2 = TimestampAdjuster.DO_NOT_OFFSET;
        V v = null;
        while (this.size > 0) {
            long j3 = j - this.timestamps[this.first];
            if (j3 < 0 && (z || (-j3) >= j2)) {
                break;
            }
            Object[] objArr = this.values;
            int i = this.first;
            v = objArr[i];
            objArr[i] = null;
            this.first = (i + 1) % objArr.length;
            this.size--;
            j2 = j3;
        }
        return v;
    }

    private void clearBufferOnTimeDiscontinuity(long j) {
        int i = this.size;
        if (i > 0) {
            if (j <= this.timestamps[((this.first + i) - 1) % this.values.length]) {
                clear();
            }
        }
    }

    private void doubleCapacityIfFull() {
        int length = this.values.length;
        if (this.size >= length) {
            int i = length * 2;
            long[] jArr = new long[i];
            Object[] newArray = newArray(i);
            int i2 = this.first;
            length -= i2;
            System.arraycopy(this.timestamps, i2, jArr, 0, length);
            System.arraycopy(this.values, this.first, newArray, 0, length);
            i2 = this.first;
            if (i2 > 0) {
                System.arraycopy(this.timestamps, 0, jArr, length, i2);
                System.arraycopy(this.values, 0, newArray, length, this.first);
            }
            this.timestamps = jArr;
            this.values = newArray;
            this.first = 0;
        }
    }

    private void addUnchecked(long j, V v) {
        int i = this.first;
        int i2 = this.size;
        i += i2;
        Object[] objArr = this.values;
        i %= objArr.length;
        this.timestamps[i] = j;
        objArr[i] = v;
        this.size = i2 + 1;
    }

    private static <V> V[] newArray(int i) {
        return new Object[i];
    }
}
