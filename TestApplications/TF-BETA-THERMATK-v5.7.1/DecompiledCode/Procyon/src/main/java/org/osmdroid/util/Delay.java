// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

public class Delay
{
    private long mDuration;
    private final long[] mDurations;
    private int mIndex;
    private long mNextTime;
    
    public Delay(final long[] mDurations) {
        if (mDurations != null && mDurations.length != 0) {
            this.mDurations = mDurations;
            this.next();
            return;
        }
        throw new IllegalArgumentException();
    }
    
    private long now() {
        return System.nanoTime() / 1000000L;
    }
    
    public long next() {
        final long[] mDurations = this.mDurations;
        long mDuration;
        if (mDurations == null) {
            mDuration = this.mDuration;
        }
        else {
            final int mIndex = this.mIndex;
            mDuration = mDurations[mIndex];
            if (mIndex < mDurations.length - 1) {
                this.mIndex = mIndex + 1;
            }
        }
        this.mNextTime = this.now() + mDuration;
        return mDuration;
    }
    
    public boolean shouldWait() {
        return this.now() < this.mNextTime;
    }
}
