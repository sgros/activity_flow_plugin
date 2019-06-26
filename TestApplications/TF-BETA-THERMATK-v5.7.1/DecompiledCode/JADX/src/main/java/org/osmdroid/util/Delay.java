package org.osmdroid.util;

public class Delay {
    private long mDuration;
    private final long[] mDurations;
    private int mIndex;
    private long mNextTime;

    public Delay(long[] jArr) {
        if (jArr == null || jArr.length == 0) {
            throw new IllegalArgumentException();
        }
        this.mDurations = jArr;
        next();
    }

    public long next() {
        long j;
        long[] jArr = this.mDurations;
        if (jArr == null) {
            j = this.mDuration;
        } else {
            int i = this.mIndex;
            long j2 = jArr[i];
            if (i < jArr.length - 1) {
                this.mIndex = i + 1;
            }
            j = j2;
        }
        this.mNextTime = now() + j;
        return j;
    }

    public boolean shouldWait() {
        return now() < this.mNextTime;
    }

    private long now() {
        return System.nanoTime() / 1000000;
    }
}
