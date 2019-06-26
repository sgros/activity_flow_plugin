// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.util.Util;

public class ConstantBitrateSeekMap implements SeekMap
{
    private final int bitrate;
    private final long dataSize;
    private final long durationUs;
    private final long firstFrameBytePosition;
    private final int frameSize;
    private final long inputLength;
    
    public ConstantBitrateSeekMap(final long inputLength, final long firstFrameBytePosition, final int bitrate, final int n) {
        this.inputLength = inputLength;
        this.firstFrameBytePosition = firstFrameBytePosition;
        int frameSize = n;
        if (n == -1) {
            frameSize = 1;
        }
        this.frameSize = frameSize;
        this.bitrate = bitrate;
        if (inputLength == -1L) {
            this.dataSize = -1L;
            this.durationUs = -9223372036854775807L;
        }
        else {
            this.dataSize = inputLength - firstFrameBytePosition;
            this.durationUs = getTimeUsAtPosition(inputLength, firstFrameBytePosition, bitrate);
        }
    }
    
    private long getFramePositionForTimeUs(long constrainValue) {
        constrainValue = constrainValue * this.bitrate / 8000000L;
        final int frameSize = this.frameSize;
        constrainValue = Util.constrainValue(constrainValue / frameSize * frameSize, 0L, this.dataSize - frameSize);
        return this.firstFrameBytePosition + constrainValue;
    }
    
    private static long getTimeUsAtPosition(final long n, final long n2, final int n3) {
        return Math.max(0L, n - n2) * 8L * 1000000L / n3;
    }
    
    @Override
    public long getDurationUs() {
        return this.durationUs;
    }
    
    @Override
    public SeekPoints getSeekPoints(long n) {
        if (this.dataSize == -1L) {
            return new SeekPoints(new SeekPoint(0L, this.firstFrameBytePosition));
        }
        final long framePositionForTimeUs = this.getFramePositionForTimeUs(n);
        final long timeUsAtPosition = this.getTimeUsAtPosition(framePositionForTimeUs);
        final SeekPoint seekPoint = new SeekPoint(timeUsAtPosition, framePositionForTimeUs);
        if (timeUsAtPosition < n) {
            final int frameSize = this.frameSize;
            if (frameSize + framePositionForTimeUs < this.inputLength) {
                n = framePositionForTimeUs + frameSize;
                return new SeekPoints(seekPoint, new SeekPoint(this.getTimeUsAtPosition(n), n));
            }
        }
        return new SeekPoints(seekPoint);
    }
    
    public long getTimeUsAtPosition(final long n) {
        return getTimeUsAtPosition(n, this.firstFrameBytePosition, this.bitrate);
    }
    
    @Override
    public boolean isSeekable() {
        return this.dataSize != -1L;
    }
}
