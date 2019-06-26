// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

public final class TimestampAdjuster
{
    public static final long DO_NOT_OFFSET = Long.MAX_VALUE;
    private static final long MAX_PTS_PLUS_ONE = 8589934592L;
    private long firstSampleTimestampUs;
    private volatile long lastSampleTimestampUs;
    private long timestampOffsetUs;
    
    public TimestampAdjuster(final long firstSampleTimestampUs) {
        this.lastSampleTimestampUs = -9223372036854775807L;
        this.setFirstSampleTimestampUs(firstSampleTimestampUs);
    }
    
    public static long ptsToUs(final long n) {
        return n * 1000000L / 90000L;
    }
    
    public static long usToPts(final long n) {
        return n * 90000L / 1000000L;
    }
    
    public long adjustSampleTimestamp(final long n) {
        if (n == -9223372036854775807L) {
            return -9223372036854775807L;
        }
        Label_0064: {
            if (this.lastSampleTimestampUs != -9223372036854775807L) {
                this.lastSampleTimestampUs = n;
                break Label_0064;
            }
            final long firstSampleTimestampUs = this.firstSampleTimestampUs;
            if (firstSampleTimestampUs != Long.MAX_VALUE) {
                this.timestampOffsetUs = firstSampleTimestampUs - n;
            }
            synchronized (this) {
                this.lastSampleTimestampUs = n;
                this.notifyAll();
                // monitorexit(this)
                return n + this.timestampOffsetUs;
            }
        }
    }
    
    public long adjustTsTimestamp(long n) {
        if (n == -9223372036854775807L) {
            return -9223372036854775807L;
        }
        long n2 = n;
        if (this.lastSampleTimestampUs != -9223372036854775807L) {
            final long usToPts = usToPts(this.lastSampleTimestampUs);
            final long n3 = (4294967296L + usToPts) / 8589934592L;
            final long n4 = (n3 - 1L) * 8589934592L + n;
            n = (n2 = n + n3 * 8589934592L);
            if (Math.abs(n4 - usToPts) < Math.abs(n - usToPts)) {
                n2 = n4;
            }
        }
        return this.adjustSampleTimestamp(ptsToUs(n2));
    }
    
    public long getFirstSampleTimestampUs() {
        return this.firstSampleTimestampUs;
    }
    
    public long getLastAdjustedTimestampUs() {
        final long lastSampleTimestampUs = this.lastSampleTimestampUs;
        long n = -9223372036854775807L;
        if (lastSampleTimestampUs != -9223372036854775807L) {
            n = this.timestampOffsetUs + this.lastSampleTimestampUs;
        }
        else {
            final long firstSampleTimestampUs = this.firstSampleTimestampUs;
            if (firstSampleTimestampUs != Long.MAX_VALUE) {
                n = firstSampleTimestampUs;
            }
        }
        return n;
    }
    
    public long getTimestampOffsetUs() {
        final long firstSampleTimestampUs = this.firstSampleTimestampUs;
        long timestampOffsetUs = -9223372036854775807L;
        if (firstSampleTimestampUs == Long.MAX_VALUE) {
            timestampOffsetUs = 0L;
        }
        else if (this.lastSampleTimestampUs != -9223372036854775807L) {
            timestampOffsetUs = this.timestampOffsetUs;
        }
        return timestampOffsetUs;
    }
    
    public void reset() {
        this.lastSampleTimestampUs = -9223372036854775807L;
    }
    
    public void setFirstSampleTimestampUs(final long firstSampleTimestampUs) {
        synchronized (this) {
            Assertions.checkState(this.lastSampleTimestampUs == -9223372036854775807L);
            this.firstSampleTimestampUs = firstSampleTimestampUs;
        }
    }
    
    public void waitUntilInitialized() throws InterruptedException {
        synchronized (this) {
            while (this.lastSampleTimestampUs == -9223372036854775807L) {
                this.wait();
            }
        }
    }
}
