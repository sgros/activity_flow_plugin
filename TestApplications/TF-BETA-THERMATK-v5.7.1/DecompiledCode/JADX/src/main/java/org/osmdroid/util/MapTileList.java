package org.osmdroid.util;

public class MapTileList implements MapTileContainer {
    private int mSize;
    private long[] mTileIndices;

    public void clear() {
        this.mSize = 0;
    }

    public int getSize() {
        return this.mSize;
    }

    public long get(int i) {
        return this.mTileIndices[i];
    }

    public void put(long j) {
        ensureCapacity(this.mSize + 1);
        long[] jArr = this.mTileIndices;
        int i = this.mSize;
        this.mSize = i + 1;
        jArr[i] = j;
    }

    public void ensureCapacity(int i) {
        if (i != 0) {
            long[] jArr = this.mTileIndices;
            if (jArr == null || jArr.length < i) {
                synchronized (this) {
                    long[] jArr2 = new long[i];
                    if (this.mTileIndices != null) {
                        System.arraycopy(this.mTileIndices, 0, jArr2, 0, this.mTileIndices.length);
                    }
                    this.mTileIndices = jArr2;
                }
            }
        }
    }

    public boolean contains(long j) {
        if (this.mTileIndices == null) {
            return false;
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mTileIndices[i] == j) {
                return true;
            }
        }
        return false;
    }
}
