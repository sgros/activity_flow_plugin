// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.FormatHolder;
import java.io.IOException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.source.SampleStream;

final class HlsSampleStream implements SampleStream
{
    private int sampleQueueIndex;
    private final HlsSampleStreamWrapper sampleStreamWrapper;
    private final int trackGroupIndex;
    
    public HlsSampleStream(final HlsSampleStreamWrapper sampleStreamWrapper, final int trackGroupIndex) {
        this.sampleStreamWrapper = sampleStreamWrapper;
        this.trackGroupIndex = trackGroupIndex;
        this.sampleQueueIndex = -1;
    }
    
    private boolean hasValidSampleQueueIndex() {
        final int sampleQueueIndex = this.sampleQueueIndex;
        return sampleQueueIndex != -1 && sampleQueueIndex != -3 && sampleQueueIndex != -2;
    }
    
    public void bindSampleQueue() {
        Assertions.checkArgument(this.sampleQueueIndex == -1);
        this.sampleQueueIndex = this.sampleStreamWrapper.bindSampleQueueToSampleStream(this.trackGroupIndex);
    }
    
    @Override
    public boolean isReady() {
        return this.sampleQueueIndex == -3 || (this.hasValidSampleQueueIndex() && this.sampleStreamWrapper.isReady(this.sampleQueueIndex));
    }
    
    @Override
    public void maybeThrowError() throws IOException {
        if (this.sampleQueueIndex != -2) {
            this.sampleStreamWrapper.maybeThrowError();
            return;
        }
        throw new SampleQueueMappingException(this.sampleStreamWrapper.getTrackGroups().get(this.trackGroupIndex).getFormat(0).sampleMimeType);
    }
    
    @Override
    public int readData(final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b) {
        int data;
        if (this.hasValidSampleQueueIndex()) {
            data = this.sampleStreamWrapper.readData(this.sampleQueueIndex, formatHolder, decoderInputBuffer, b);
        }
        else {
            data = -3;
        }
        return data;
    }
    
    @Override
    public int skipData(final long n) {
        int skipData;
        if (this.hasValidSampleQueueIndex()) {
            skipData = this.sampleStreamWrapper.skipData(this.sampleQueueIndex, n);
        }
        else {
            skipData = 0;
        }
        return skipData;
    }
    
    public void unbindSampleQueue() {
        if (this.sampleQueueIndex != -1) {
            this.sampleStreamWrapper.unbindSampleQueue(this.trackGroupIndex);
            this.sampleQueueIndex = -1;
        }
    }
}
