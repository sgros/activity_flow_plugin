// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

public class MapTileList implements MapTileContainer
{
    private int mSize;
    private long[] mTileIndices;
    
    public void clear() {
        this.mSize = 0;
    }
    
    @Override
    public boolean contains(final long n) {
        if (this.mTileIndices == null) {
            return false;
        }
        for (int i = 0; i < this.mSize; ++i) {
            if (this.mTileIndices[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    public void ensureCapacity(final int n) {
        if (n == 0) {
            return;
        }
        final long[] mTileIndices = this.mTileIndices;
        if (mTileIndices != null && mTileIndices.length >= n) {
            return;
        }
        synchronized (this) {
            final long[] mTileIndices2 = new long[n];
            if (this.mTileIndices != null) {
                System.arraycopy(this.mTileIndices, 0, mTileIndices2, 0, this.mTileIndices.length);
            }
            this.mTileIndices = mTileIndices2;
        }
    }
    
    public long get(final int n) {
        return this.mTileIndices[n];
    }
    
    public int getSize() {
        return this.mSize;
    }
    
    public void put(final long n) {
        this.ensureCapacity(this.mSize + 1);
        this.mTileIndices[this.mSize++] = n;
    }
}
