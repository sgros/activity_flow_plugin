// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

public final class FlacStreamInfo
{
    public final int bitsPerSample;
    public final int channels;
    public final int maxBlockSize;
    public final int maxFrameSize;
    public final int minBlockSize;
    public final int minFrameSize;
    public final int sampleRate;
    public final long totalSamples;
    
    public FlacStreamInfo(final int minBlockSize, final int maxBlockSize, final int minFrameSize, final int maxFrameSize, final int sampleRate, final int channels, final int bitsPerSample, final long totalSamples) {
        this.minBlockSize = minBlockSize;
        this.maxBlockSize = maxBlockSize;
        this.minFrameSize = minFrameSize;
        this.maxFrameSize = maxFrameSize;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.bitsPerSample = bitsPerSample;
        this.totalSamples = totalSamples;
    }
    
    public FlacStreamInfo(final byte[] array, final int n) {
        final ParsableBitArray parsableBitArray = new ParsableBitArray(array);
        parsableBitArray.setPosition(n * 8);
        this.minBlockSize = parsableBitArray.readBits(16);
        this.maxBlockSize = parsableBitArray.readBits(16);
        this.minFrameSize = parsableBitArray.readBits(24);
        this.maxFrameSize = parsableBitArray.readBits(24);
        this.sampleRate = parsableBitArray.readBits(20);
        this.channels = parsableBitArray.readBits(3) + 1;
        this.bitsPerSample = parsableBitArray.readBits(5) + 1;
        this.totalSamples = (((long)parsableBitArray.readBits(4) & 0xFL) << 32 | ((long)parsableBitArray.readBits(32) & 0xFFFFFFFFL));
    }
    
    public int bitRate() {
        return this.bitsPerSample * this.sampleRate;
    }
    
    public long durationUs() {
        return this.totalSamples * 1000000L / this.sampleRate;
    }
    
    public long getApproxBytesPerFrame() {
        final int maxFrameSize = this.maxFrameSize;
        long n;
        long n2;
        if (maxFrameSize > 0) {
            n = (maxFrameSize + (long)this.minFrameSize) / 2L;
            n2 = 1L;
        }
        else {
            final int minBlockSize = this.minBlockSize;
            long n3;
            if (minBlockSize == this.maxBlockSize && minBlockSize > 0) {
                n3 = minBlockSize;
            }
            else {
                n3 = 4096L;
            }
            n = n3 * this.channels * this.bitsPerSample / 8L;
            n2 = 64L;
        }
        return n + n2;
    }
    
    public long getSampleIndex(final long n) {
        return Util.constrainValue(n * this.sampleRate / 1000000L, 0L, this.totalSamples - 1L);
    }
    
    public int maxDecodedFrameSize() {
        return this.maxBlockSize * this.channels * (this.bitsPerSample / 8);
    }
}
