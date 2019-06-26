// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.wav;

import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.extractor.SeekMap;

final class WavHeader implements SeekMap
{
    private final int averageBytesPerSecond;
    private final int bitsPerSample;
    private final int blockAlignment;
    private long dataSize;
    private long dataStartPosition;
    private final int encoding;
    private final int numChannels;
    private final int sampleRateHz;
    
    public WavHeader(final int numChannels, final int sampleRateHz, final int averageBytesPerSecond, final int blockAlignment, final int bitsPerSample, final int encoding) {
        this.numChannels = numChannels;
        this.sampleRateHz = sampleRateHz;
        this.averageBytesPerSecond = averageBytesPerSecond;
        this.blockAlignment = blockAlignment;
        this.bitsPerSample = bitsPerSample;
        this.encoding = encoding;
    }
    
    public int getBitrate() {
        return this.sampleRateHz * this.bitsPerSample * this.numChannels;
    }
    
    public int getBytesPerFrame() {
        return this.blockAlignment;
    }
    
    public long getDataLimit() {
        long n;
        if (this.hasDataBounds()) {
            n = this.dataStartPosition + this.dataSize;
        }
        else {
            n = -1L;
        }
        return n;
    }
    
    @Override
    public long getDurationUs() {
        return this.dataSize / this.blockAlignment * 1000000L / this.sampleRateHz;
    }
    
    public int getEncoding() {
        return this.encoding;
    }
    
    public int getNumChannels() {
        return this.numChannels;
    }
    
    public int getSampleRateHz() {
        return this.sampleRateHz;
    }
    
    @Override
    public SeekPoints getSeekPoints(long dataSize) {
        final long n = this.averageBytesPerSecond * dataSize / 1000000L;
        final int blockAlignment = this.blockAlignment;
        final long constrainValue = Util.constrainValue(n / blockAlignment * blockAlignment, 0L, this.dataSize - blockAlignment);
        final long n2 = this.dataStartPosition + constrainValue;
        final long timeUs = this.getTimeUs(n2);
        final SeekPoint seekPoint = new SeekPoint(timeUs, n2);
        if (timeUs < dataSize) {
            dataSize = this.dataSize;
            final int blockAlignment2 = this.blockAlignment;
            if (constrainValue != dataSize - blockAlignment2) {
                dataSize = n2 + blockAlignment2;
                return new SeekPoints(seekPoint, new SeekPoint(this.getTimeUs(dataSize), dataSize));
            }
        }
        return new SeekPoints(seekPoint);
    }
    
    public long getTimeUs(final long n) {
        return Math.max(0L, n - this.dataStartPosition) * 1000000L / this.averageBytesPerSecond;
    }
    
    public boolean hasDataBounds() {
        return this.dataStartPosition != 0L && this.dataSize != 0L;
    }
    
    @Override
    public boolean isSeekable() {
        return true;
    }
    
    public void setDataBounds(final long dataStartPosition, final long dataSize) {
        this.dataStartPosition = dataStartPosition;
        this.dataSize = dataSize;
    }
}
